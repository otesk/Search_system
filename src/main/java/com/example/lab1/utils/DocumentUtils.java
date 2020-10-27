package com.example.lab1.utils;

import java.util.List;
import java.util.Map;

public interface DocumentUtils {
    Map<String, Integer> getTermOccurrences(String text);

    Double getLengthVector(List<Double> vectorDocument);

    Double getCompositionVectors(List<Double> vec1, List<Double> vec2);

    Double getCoincidenceValue(List<Double> vec1, List<Double> vec2);
}
