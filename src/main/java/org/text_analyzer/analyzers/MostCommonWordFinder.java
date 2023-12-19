package org.text_analyzer.analyzers;

import java.util.HashMap;
import java.util.Map;

public class MostCommonWordFinder implements ResultPrintable {
    private String mostCommon;

    private final Map<String, Integer> commonWords;

    public MostCommonWordFinder() {
        this.mostCommon = null;
        this.commonWords = new HashMap<>();
    }

    public void countWordsOccurrence(String[] text) {
        for (String word : text) {
            if (word.length() == 0) {
                continue;
            }
            String lowerCaseWord = word.toLowerCase();
            if (!this.commonWords.containsKey(lowerCaseWord)) {
                this.commonWords.put(lowerCaseWord, 1);
            } else {
                this.commonWords.compute(lowerCaseWord, (k, v) -> v + 1);
            }
        }
    }

    public String getMostCommonWord() {
        this.findMostCommonWord();
        return mostCommon;
    }

    private void findMostCommonWord() {
        int max = 0;
        String word = "";

        for (String key : this.commonWords.keySet()) {
            if (this.commonWords.get(key) > max) {
                word = key;
                max = this.commonWords.get(key);
            }
        }

        this.mostCommon = word;
    }

    @Override
    public void printResultToConsole() {
        System.out.println("Most common word - " + getMostCommonWord());
    }

    @Override
    public String getPrintResult() {
        return "Most common word - " + getMostCommonWord();
    }
}
