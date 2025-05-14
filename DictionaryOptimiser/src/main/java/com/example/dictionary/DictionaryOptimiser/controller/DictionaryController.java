package com.example.dictionary.DictionaryOptimiser.controller;

import com.example.dictionary.DictionaryOptimiser.service.DictionaryService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dictionary")
public class DictionaryController {

    private final DictionaryService dictionaryService;

    public DictionaryController(DictionaryService dictionaryService) {
        this.dictionaryService = dictionaryService;
    }

    // Endpoint: GET /api/dictionary/meaning/{word}
    @GetMapping("/meaning/{word}")
    public String getMeaning(@PathVariable String word) {
        return dictionaryService.getMeaning(word);
    }

    // Endpoint: GET /api/dictionary/suggestions/{query}?maxDistance=2
    @GetMapping("/suggestions/{query}")
    public String[] getSuggestions(
            @PathVariable String query,
            @RequestParam(value = "maxDistance", defaultValue = "2") int maxDistance) {
        return dictionaryService.getSuggestions(query, maxDistance);
    }

    // Endpoint: GET /api/dictionary/most-searched/{topN}
    @GetMapping("/most-searched/{topN}")
    public String[] getMostSearchedWords(@PathVariable int topN) {
        return dictionaryService.getMostSearchedWords(topN);
    }
}
