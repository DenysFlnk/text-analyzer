package org.text_analyzer.analyzers;

import java.text.DecimalFormat;
import java.util.Arrays;

public class AverageWordLengthFinder implements Analyzer{

    private long characterCounter;

    private String avgWordLength;

    public AverageWordLengthFinder() {
        this.characterCounter = 0;
        this.avgWordLength = "0";
    }

    public void countCharacters(String[] text) {
        long count = Arrays.stream(text).mapToLong(String::length).sum();
        this.characterCounter += count;
    }

    public void computeAverageWordLength(long totalWords) {
        DecimalFormat format = new DecimalFormat("0.00");
        float result = (float) this.characterCounter / totalWords;
        this.avgWordLength = format.format(result);
    }

    public String getAverageWordLength() {
        return avgWordLength;
    }

    @Override
    public void printResultToConsole() {
        System.out.println("Average word length - " + getAverageWordLength());
    }

    @Override
    public String getPrintResult() {
        return "Average word length - " + getAverageWordLength();
    }
}
