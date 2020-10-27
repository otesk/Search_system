package com.example.lab1.utils.impl;

import com.example.lab1.utils.DocumentUtils;
import com.example.lab1.utils.MorphAnalyzer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.textanalysis.tawt.jmorfsdk.JMorfSdk;
import ru.textanalysis.tawt.ms.storage.OmoFormList;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class DocumentUtilsImpl implements DocumentUtils {
    private final JMorfSdk jMorfSdk;

    @Override
    public Map<String, Integer> getTermOccurrences(String text) {
        String cleanText = text
                .replaceAll("[–,.;:!?\"\n\r\t]", "")
                .replaceAll("  ( )*", " ").toLowerCase();
        String[] words = cleanText.split(" ");
        Map<String, Integer> initialForms = new HashMap<>();

        for (String word : words) {
            OmoFormList formList = jMorfSdk.getAllCharacteristicsOfForm(word.toLowerCase());
            //OmoFormList formList = new OmoFormList();
            if (!formList.isEmpty()) {
                word = formList.getFirst().getInitialFormString();
            }

            if (initialForms.containsKey(word)) {
                initialForms.put(word, initialForms.get(word) + 1);
            } else {
                initialForms.put(word, 1);
            }
        }

        return initialForms;
    }

    @Override
    public Double getLengthVector(List<Double> vectorDocument){
        return Math.sqrt(vectorDocument.stream().mapToDouble(weight -> weight * weight).sum());
    }

    @Override
    public Double getCompositionVectors(List<Double> vec1, List<Double> vec2) {
        Double composition = 0D;
        for(int i = 0; i < vec1.size(); i++){
            composition += vec1.get(i) * vec2.get(i);
        }

        return composition;
    }

    @Override
    public Double getCoincidenceValue(List<Double> vec1, List<Double> vec2){
        return getCompositionVectors(vec1, vec2) / (getLengthVector(vec1) * getLengthVector(vec2));
    }

}
