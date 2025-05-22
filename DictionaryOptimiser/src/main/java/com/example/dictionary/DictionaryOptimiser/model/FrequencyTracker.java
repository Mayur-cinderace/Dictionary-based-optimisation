package com.example.dictionary.DictionaryOptimiser.model;
import java.util.*;
import java.util.stream.Collectors;

public class FrequencyTracker {
    private Map<String, Integer> freqMap = new HashMap<>();

    public void initialize(String word) {
        freqMap.putIfAbsent(word, 0);
    }

    public void countFreq(String word) {
        freqMap.put(word, freqMap.getOrDefault(word, 0) + 1);
    }

    public int getFrequency(String word) {
        return freqMap.getOrDefault(word, 0);
    }

    public List<String> KFreq(int k) {
        return freqMap.entrySet()
                .stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .limit(k)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}
