package com.example.lab1.service;

import com.example.lab1.model.Word;

import java.util.List;
import java.util.Optional;

public interface WordService {
    Optional<Word> findByText(String text);

    Word save(Word word);

    void saveWordDocumentCount(Long docId, Long wordId, Integer count);

    Integer getWordCountInDocument(Long documentId, Long word_id);

    Integer getCountDocumentsWithWord(Long wordId);

    List<Word> findAll();
}
