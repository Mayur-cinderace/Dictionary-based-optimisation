package com.example.dictionary.DictionaryOptimiser.model;
import java.util.*;

public class BKTree {
    private Node root;

    private static class Node {
        String word;
        Map<Integer, Node> children = new HashMap<>();

        Node(String word) {
            this.word = word;
        }
    }

    private int levenshtein(String a, String b) {
        int[][] dp = new int[a.length() + 1][b.length() + 1];
        for (int i = 0; i <= a.length(); i++) dp[i][0] = i;
        for (int j = 0; j <= b.length(); j++) dp[0][j] = j;
        for (int i = 1; i <= a.length(); i++) {
            for (int j = 1; j <= b.length(); j++) {
                int cost = (a.charAt(i-1) == a.charAt(j-1))?0:1;
                dp[i][j] = Math.min(
                        Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1),
                        dp[i - 1][j - 1] + cost
                ); //deletion, addition, substituion
            }
        }
        return dp[a.length()][b.length()];
    }

    public void add(String word) {
        if (root == null) {
            root = new Node(word);
            return;
        }
        Node curr = root;
        int distance;
        while (true) {
            distance = levenshtein(curr.word, word);
            Node child = curr.children.get(distance);

            if (child != null) curr = child;
            else {
                curr.children.put(distance, new Node(word));
                break;
            }
        }
    }

    private void searchRecursive(Node node, String target, int maxDist, List<String> result) {
        if (node == null) return;
        int dist = levenshtein(node.word, target);
        if (dist <= maxDist) result.add(node.word);
        for (int i = dist - maxDist; i <= dist + maxDist; i++) {
            Node child = node.children.get(i);
            if (child != null) searchRecursive(child, target, maxDist, result);
        }
    }

    public List<String> search(String target, int maxDist) {
        List<String> result = new ArrayList<>();
        searchRecursive(root, target, maxDist, result);
        return result;
    }
}

