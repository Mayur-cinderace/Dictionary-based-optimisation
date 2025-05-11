package com.example.dictionary.DictionaryOptimiser.model;
import java.util.*;

class TSTNode {
    char alpha;
    boolean isEndOfWord;
    TSTNode left, middle, right;
    String meaning;  // New field to store meaning

    public TSTNode(char alpha) {
        this.alpha = alpha;
        this.left = this.right = this.middle = null;
        this.isEndOfWord = false;
        this.meaning = null; // Initially, meaning is null
    }
}

public class TernarySearchTree {
    private TSTNode root;

    private TSTNode insertRec(String word, TSTNode node, int index, String meaning) {
        char ch = word.charAt(index);
        if (node == null) node = new TSTNode(ch);

        if (ch < node.alpha) node.left = insertRec(word, node.left, index, meaning);
        else if (ch > node.alpha) node.right = insertRec(word, node.right, index, meaning);
        else {
            if (index + 1 < word.length()) node.middle = insertRec(word, node.middle, index + 1, meaning);
            else {
                node.isEndOfWord = true;
                node.meaning = meaning;
            }
        }
        return node;
    }

    public void insert(String word, String meaning) {
        root = insertRec(word, root, 0, meaning);
    }

    private String searchRec(String word, TSTNode node, int index) {
        char ch = word.charAt(index);
        if (node == null) return null;
        if (ch < node.alpha) return searchRec(word, node.left, index);
        else if (ch > node.alpha) return searchRec(word, node.right, index);
        else {
            if (index == word.length() - 1) {
                return node.isEndOfWord ? node.meaning : null;
            }
            return searchRec(word, node.middle, index + 1);
        }
    }

    public String search(String word) {
        return searchRec(word, root, 0);
    }

    private TSTNode getPrefixNode(String prefix, TSTNode node, int index) {
        char ch = prefix.charAt(index);
        if (node == null) return null;
        if (ch < node.alpha) return getPrefixNode(prefix, node.left, index);
        else if (ch > node.alpha) return getPrefixNode(prefix, node.right, index);
        else {
            if (index == prefix.length()-1) return node;
            return getPrefixNode(prefix, node.middle, index+1);
        }
    }

    private void collect(TSTNode node, String prefix, List<String> result) {
        if (node == null) return;
        collect(node.left, prefix, result);
        if (node.isEndOfWord) result.add(prefix + node.alpha);
        collect(node.middle, prefix + node.alpha, result);
        collect(node.right, prefix, result);
    }

    public List<String> autocomplete(String prefix) {
        List<String> ac = new ArrayList<>();
        TSTNode node = getPrefixNode(prefix, root, 0);
        if (node != null && prefix.length() > 0) collect(node.middle, prefix, ac);
        return ac;
    }
}
