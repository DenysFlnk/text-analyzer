package org.text_analyzer;

import java.text.DecimalFormat;
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

    public String getAverageWordLength(long totalWords) {
        DecimalFormat format = new DecimalFormat("0.00");
        float result = (float) characterCounter / totalWords;
        return format.format(result);
    }
}
