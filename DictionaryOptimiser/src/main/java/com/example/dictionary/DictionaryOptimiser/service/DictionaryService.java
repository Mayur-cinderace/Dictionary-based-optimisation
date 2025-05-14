package com.example.dictionary.DictionaryOptimiser.service;

import com.example.dictionary.DictionaryOptimiser.model.*;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Service
public class DictionaryService {

    private TernarySearchTree tst;
    private CuckooFilter cuckooFilter;
    private BKTree bkTree;
    private FrequencyTracker frequencyTracker;

    @PostConstruct
    public void init() {
        tst = new TernarySearchTree();
        cuckooFilter = new CuckooFilter(10000); // Tune capacity based on dictionary size
        bkTree = new BKTree();
        frequencyTracker = new FrequencyTracker();

        // Load dictionary.csv from resources/data folder (ensure it's included in src/main/resources)
        loadDictionaryFromCSV("data/dictionary.csv");
    }

    private void loadDictionaryFromCSV(String filename) {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        Objects.requireNonNull(getClass()
                                .getClassLoader()
                                .getResourceAsStream(filename)),
                        StandardCharsets.UTF_8))) {

            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", 2); // CSV format: word,meaning
                if (parts.length == 2) {
                    String word = parts[0].trim().toLowerCase();
                    String meaning = parts[1].trim();

                    tst.insert(word, meaning);
                    cuckooFilter.insert(word);
                    bkTree.add(word);
                    frequencyTracker.initialize(word);
                }
            }

            System.out.println("✅ Dictionary loaded successfully.");
        } catch (Exception e) {
            System.err.println("❌ Failed to load dictionary: " + e.getMessage());
        }
    }

    public String getMeaning(String word) {
        if (word == null || word.trim().isEmpty()) {
            return "Invalid input: Word cannot be empty.";
        }

        word = word.toLowerCase();

        // Fast-fail using Cuckoo Filter (may rarely have false negatives)
        if (!cuckooFilter.mightContain(word)) {
            return "Word not found (fast fail)";
        }

        String meaning = tst.search(word);
        if (meaning != null) {
            frequencyTracker.countFreq(word); // Count only if the word exists
            return meaning;
        }

        return "Word not found";
    }

    public String[] getSuggestions(String query, int maxDistance) {
        if (query == null || query.trim().isEmpty()) {
            return new String[0];
        }
        return bkTree.search(query.toLowerCase(), maxDistance).toArray(new String[0]);
    }

    public String[] getMostSearchedWords(int topN) {
        return frequencyTracker.KFreq(topN).toArray(new String[0]);
    }
}
