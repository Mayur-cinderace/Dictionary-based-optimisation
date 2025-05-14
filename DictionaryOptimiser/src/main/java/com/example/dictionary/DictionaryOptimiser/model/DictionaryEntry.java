package com.example.dictionary.DictionaryOptimiser.model;

public class DictionaryEntry {
    private String word;
    private String meaning;

    public DictionaryEntry(String word, String meaning) {
        this.word = word;
        this.meaning = meaning;
    }

    public String getWord() { return word; }
    public String getMeaning() { return meaning; }
}


