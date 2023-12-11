package org.text_analyzer;

import java.util.Arrays;

public class AverageWordLengthFinder {

    private long characterCounter;

    public AverageWordLengthFinder() {
        this.characterCounter = 0;
    }

    public void countCharacters(String[] text) {
        long count = Arrays.stream(text).mapToLong(String::length).sum();
        this.characterCounter += count;
    }

    public float getAverageWordLength(long totalWords) {
        return (float) characterCounter / totalWords;
    }
}
