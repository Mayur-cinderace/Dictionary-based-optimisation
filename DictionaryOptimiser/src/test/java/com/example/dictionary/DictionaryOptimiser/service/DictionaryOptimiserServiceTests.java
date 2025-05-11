package com.example.dictionary.DictionaryOptimiser.service;
import com.example.dictionary.DictionaryOptimiser.model.TernarySearchTree;
import com.example.dictionary.DictionaryOptimiser.model.CuckooFilter;
import com.example.dictionary.DictionaryOptimiser.model.BKTree;
import com.example.dictionary.DictionaryOptimiser.model.FrequencyTracker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;
import static org.mockito.Mockito.*;

@SpringBootTest
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
public class DictionaryOptimiserServiceTests {

    @InjectMocks
    private DictionaryService dictionaryService;

    @Mock
    private TernarySearchTree tst;

    @Mock
    private CuckooFilter cuckooFilter;

    @Mock
    private BKTree bkTree;

    @Mock
    private FrequencyTracker frequencyTracker;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void testGetMeaningEmptyWord() {
        // Test case for empty input
        String word = "";

        // Call the method to test
        String actualMeaning = dictionaryService.getMeaning(word);

        // Verify the outcome
        assert(actualMeaning.equals("Word not found"));
    }

    @Test
    public void testGetMeaningNullWord() {
        // Test case for null input
        String word = null;

        // Call the method to test
        String actualMeaning = dictionaryService.getMeaning(word);

        // Verify the outcome
        assert(actualMeaning.equals("Word not found"));
    }

    @Test
    public void testGetMeaningWordNotFoundInTST() {
        String word = "unknown";
        // Mocking the behavior of the cuckooFilter when the word is not present
        when(cuckooFilter.mightContain(word)).thenReturn(true);
        // Mocking that the word is not found in TST
        when(tst.search(word)).thenReturn(null);

        // Call the method to test
        String actualMeaning = dictionaryService.getMeaning(word);

        // Verify the outcome
        assert(actualMeaning.equals("Word not found"));

        // Verify method calls
        verify(cuckooFilter, times(1)).mightContain(word);
        verify(tst, times(1)).search(word);
    }

    @Test
    public void testAutocompleteMultipleSuggestions() {
        String prefix = "auto";
        String[] suggestions = {"automobile", "autonomous", "autograph"};

        // Mocking the behavior of BKTree for autocomplete
        when(bkTree.search(prefix, 3)).thenReturn(List.of(suggestions));

        // Call the method to test
        String[] actualSuggestions = dictionaryService.getSuggestions(prefix, 3);

        // Verify the outcome
        assert(actualSuggestions.length == suggestions.length);
        for (int i = 0; i < suggestions.length; i++) {
            assert(actualSuggestions[i].equals(suggestions[i]));
        }
    }

    @Test
    public void testMostSearchedWords() {
        int topN = 3;
        String[] mostSearched = {"hello", "world", "java"};

        // Mocking the behavior of FrequencyTracker
        when(frequencyTracker.KFreq(topN)).thenReturn(List.of(mostSearched));

        // Call the method to test
        String[] actualMostSearched = dictionaryService.getMostSearchedWords(topN);

        // Verify the outcome
        assert(actualMostSearched.length == mostSearched.length);
        for (int i = 0; i < mostSearched.length; i++) {
            assert(actualMostSearched[i].equals(mostSearched[i]));
        }

        // Verify that frequencyTracker's KFreq method was called
        verify(frequencyTracker, times(1)).KFreq(topN);
    }

    @Test
    public void testGetMeaningWordNotFoundInCuckooFilter() {
        String word = "random";

        // Mocking the behavior when word is not in cuckooFilter
        when(cuckooFilter.mightContain(word)).thenReturn(false);

        // Call the method to test
        String actualMeaning = dictionaryService.getMeaning(word);

        // Verify the outcome
        assert(actualMeaning.equals("Word not found (fast fail)"));

        // Verify method calls
        verify(cuckooFilter, times(1)).mightContain(word);
        verify(tst, times(0)).search(word);  // No need to search in TST as cuckooFilter fails
    }

    @Test
    public void testGetMeaningWordFound() {
        String word = "test";
        String expectedMeaning = "A procedure for accomplishing something";

        // Mocking the behavior of the underlying services
        when(cuckooFilter.mightContain(word)).thenReturn(true);
        when(tst.search(word)).thenReturn(expectedMeaning);

        // Call the method to test
        String actualMeaning = dictionaryService.getMeaning(word);

        // Verify the outcome
        assert(actualMeaning.equals(expectedMeaning));

        // Verify that methods were called
        verify(cuckooFilter, times(1)).mightContain(word);
        verify(tst, times(1)).search(word);
    }

    @Test
    public void testGetMeaningWordNotFound() {
        String word = "unknown";

        // Mocking the behavior when word is not found
        when(cuckooFilter.mightContain(word)).thenReturn(false);

        // Call the method to test
        String actualMeaning = dictionaryService.getMeaning(word);

        // Verify the outcome
        assert(actualMeaning.equals("Word not found (fast fail)"));

        // Verify that methods were called
        verify(cuckooFilter, times(1)).mightContain(word);
        verify(tst, times(0)).search(word);  // No need to search the TST if cuckooFilter fails
    }

    @Test
    public void testAutocomplete() {
        String prefix = "auto";
        String[] suggestions = {"automobile", "autonomous", "autograph"};

        // Mocking the behavior of BKTree for autocomplete
        when(bkTree.search(prefix, 2)).thenReturn(List.of(suggestions));

        // Call the method to test
        String[] actualSuggestions = dictionaryService.getSuggestions(prefix, 2);

        // Verify the outcome
        assert(actualSuggestions.length == suggestions.length);
    }
}



