package org.text_analyzer.analyzers;

import java.util.Arrays;
import java.util.Comparator;

public class LongestWordFinder implements ResultPrintable {
    String longest;

    public LongestWordFinder() {
        this.longest = "";
    }

    public void findLongestWord(String[] text) {
        String l = Arrays.stream(text).sorted().max(Comparator.comparingInt(String::length))
                .orElseThrow(() -> new RuntimeException("Empty text"));
        if (this.longest.length() < l.length()) {
            this.longest = l.toLowerCase();
        }
    }

    public String getLongestWord() {
        return this.longest;
    }

    @Override
    public void printResultToConsole() {
        System.out.println("Longest word - " + getLongestWord());
    }

    @Override
    public String getPrintResult() {
        return "Longest word - " + getLongestWord();
    }
}
