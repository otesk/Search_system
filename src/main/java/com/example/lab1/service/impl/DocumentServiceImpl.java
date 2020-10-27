package com.example.lab1.service.impl;

import com.example.lab1.model.Document;
import com.example.lab1.model.SearchResult;
import com.example.lab1.model.Word;
import com.example.lab1.repository.DocumentRepository;
import com.example.lab1.service.DocumentService;
import com.example.lab1.service.WordService;
import com.example.lab1.utils.DocumentUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class DocumentServiceImpl implements DocumentService {
    private static final int maxLengthSnippet = 300;
    private final String PATH = System.getProperty("user.dir") + "\\files";

    private final DocumentRepository documentRepository;
    private final DocumentUtils documentUtils;
    private final WordService wordService;

    @Override
    public List<Document> findAll() {
        return documentRepository.findAll();
    }

    @Override
    public Integer countAll() {
        return documentRepository.countAll();
    }

    @Override
    public Map<Word, Double> getDictionary() {
        Integer allDocumentCount = countAll();

        return wordService.findAll()
                .stream()
                .collect(Collectors.toMap(word -> word,
                        word -> Math.log(Double.valueOf(allDocumentCount) / wordService.getCountDocumentsWithWord(word.getId()))));
    }

    @Override
    public void upload(MultipartFile file) {
        try {
            String documentContent = new String(file.getBytes());
            Map<String, Integer> wordsCount = documentUtils.getTermOccurrences(documentContent);

            Document document = Document.builder()
                    .text(documentContent)
                    .title(file.getOriginalFilename())
                    .build();

            Document finalDocument = documentRepository.save(document);

            wordsCount.forEach((text, count) -> {
                Word word = wordService.findByText(text).orElseGet(() -> wordService.save(new Word(null, text)));
                wordService.saveWordDocumentCount(finalDocument.getId(), word.getId(), count);
            });

            saveFileStorage(file);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public Document findDocument(Long id) {
        return documentRepository.findDocumentById(id).orElseGet(Document::new);
    }

    @Override
    public List<SearchResult> getSearchDocuments(String text) {
        Map<Word, Double> dictionary = getDictionary();
        List<Double> vectorRequest = getVectorRequest(documentUtils.getTermOccurrences(text), dictionary);

        Map<Document, List<Double>> informationFlow =
                findAll().stream().collect(Collectors.toMap(document -> document,
                        document -> getVectorDocument(document, dictionary)));

        return informationFlow
                .entrySet().stream()
                .map(entry -> SearchResult.builder()
                        .document(entry.getKey())
                        .rank(documentUtils.getCoincidenceValue(vectorRequest, entry.getValue()))
                        .snippet(entry.getKey().getText().substring(0, Math.min(maxLengthSnippet, entry.getKey().getText().length())))
                        .build())
                .filter(searchResult -> searchResult.getRank() > 0)
                .sorted(Comparator.comparingDouble(SearchResult::getRank).reversed())
                .collect(Collectors.toList());
    }

    private List<Double> getVectorDocument(Document document, Map<Word, Double> dictionary) {
        AtomicLong wordKey = new AtomicLong(0);
        try {
            return dictionary.entrySet().stream()
                    .mapToDouble(entry -> {
                        wordKey.set(entry.getKey().getId());

                        return wordService.getWordCountInDocument(document.getId(), entry.getKey().getId())
                                * entry.getValue();
                    })
                    .boxed().collect(Collectors.toList());
        } catch (Exception e) {
            System.out.println(wordKey);
            return new ArrayList<>();
        }
    }

    private List<Double> getVectorRequest(Map<String, Integer> wordsCount, Map<Word, Double> dictionary) {
        return dictionary.entrySet().stream()
                .mapToDouble(entry ->
                        wordsCount.getOrDefault(entry.getKey().getText(), 0) * entry.getValue())
                .boxed().collect(Collectors.toList());
    }


    private void saveFileStorage(MultipartFile file) {
        File fileToSave = new File(PATH + '/' + file.getOriginalFilename());
        try {
            if (!fileToSave.exists()) {
                fileToSave.createNewFile();
            }
            file.transferTo(fileToSave);
        } catch (IOException e) {
            fileToSave.delete();
        }
    }
}
