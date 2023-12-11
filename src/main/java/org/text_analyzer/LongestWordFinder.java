package org.text_analyzer;

import java.util.Arrays;
import java.util.Comparator;

public class LongestWordFinder {
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
}
