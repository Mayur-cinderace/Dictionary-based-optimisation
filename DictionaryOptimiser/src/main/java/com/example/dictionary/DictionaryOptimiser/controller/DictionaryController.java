package com.example.dictionary.DictionaryOptimiser.controller;
import com.example.dictionary.DictionaryOptimiser.service.DictionaryService;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/dictionary")
public class DictionaryController {
    private final DictionaryService dictionaryService;
    public DictionaryController(DictionaryService dictionaryService) {
        this.dictionaryService = dictionaryService;
    }
    @GetMapping("/meaning/{word}")
    public String getMeaning(@PathVariable String word) {
        return dictionaryService.getMeaning(word);
    }
    @GetMapping("/suggestions/{query}")
    public String[] getSuggestions(@PathVariable String query,
                                   @RequestParam(value = "maxDistance", defaultValue = "2") int maxDistance) {
        return dictionaryService.getSuggestions(query, maxDistance);
    }
    @GetMapping("/most-searched/{topN}")
    public String[] getMostSearchedWords(@PathVariable int topN) {
        return dictionaryService.getMostSearchedWords(topN);
    }
}
