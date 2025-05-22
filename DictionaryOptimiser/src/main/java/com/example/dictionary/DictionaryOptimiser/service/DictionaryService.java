package com.example.dictionary.DictionaryOptimiser.service;

import com.example.dictionary.DictionaryOptimiser.model.*;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import org.apache.commons.csv.*;
import java.util.*;
import java.io.*;

@Service
public class DictionaryService {

    private TernarySearchTree tst;
    private CuckooFilter cuckooFilter;
    private BKTree bkTree;
    private FrequencyTracker frequencyTracker;

    @PostConstruct
    public void init() {
        // 1) Count how many entries in the CSV
        int wordCount = countDictionaryEntries("data/dictionary.csv");

        // 2) Compute capacity: loadFactor = 0.84 → slots = words/0.84
        int estimatedSlots = (int) (wordCount / 0.84);
        int bucketCount = (int) Math.ceil(estimatedSlots / 4.0);
        // Round up to next power of two:
        bucketCount = Integer.highestOneBit(bucketCount - 1) << 1;
        int capacity = bucketCount * 4;

        // 3) Initialize all data structures with correct sizing
        tst = new TernarySearchTree();
        cuckooFilter = new CuckooFilter(capacity);
        bkTree = new BKTree();
        frequencyTracker = new FrequencyTracker();

        // 4) Now actually load and insert
        int loaded = loadDictionaryFromCSV("data/dictionary.csv");
        System.out.println("✅ Initialized dictionary with "
                + loaded + " words (filter cap=" + capacity + ")");
    }

    /**
     * Single‐pass scan just to count the number of non‐empty lines (words).
     */
    private int countDictionaryEntries(String filename) {
        int count = 0;
        try (InputStream is = Objects.requireNonNull(
                getClass().getClassLoader().getResourceAsStream(filename),
                "Dictionary file not found: " + filename);
             BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8)))
        {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty() && line.contains(",")) {
                    count++;
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("Failed to count dictionary entries", e);
        }
        return count;
    }

    private int loadDictionaryFromCSV(String filename) {
        int count = 0;
        try (InputStream is = Objects.requireNonNull(
                getClass().getClassLoader().getResourceAsStream(filename),
                "Dictionary file not found: " + filename);
             BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8)))
        {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", 2);
                if (parts.length < 2) continue;

                String word    = parts[0].trim().toLowerCase();
                String meaning = parts[1].trim();

                tst.insert(word, meaning);
                cuckooFilter.insert(word);
                bkTree.add(word);
                frequencyTracker.initialize(word);
                count++;
            }
        } catch (IOException e) {
            System.err.println("❌ Failed to load dictionary: " + e.getMessage());
            e.printStackTrace();
        }
        return count;
    }

    public String getMeaning(String word) {
        if (word == null || word.trim().isEmpty()) {
            return "Invalid input: Word cannot be empty.";
        }
        word = word.toLowerCase().trim();

        // Check filter & TST as before...
        String meaning;
        if (!cuckooFilter.mightContain(word)) {
            meaning = tst.search(word);
            if (meaning != null) {
                frequencyTracker.countFreq(word);
                return meaning + " (fallback)";
            }
            return "Word not found";
        }

        meaning = tst.search(word);
        if (meaning != null) {
            frequencyTracker.countFreq(word);
            return meaning;
        } else {
            return "Word not found";
        }
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
