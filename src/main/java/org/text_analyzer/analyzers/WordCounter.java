package org.text_analyzer.analyzers;

import java.util.Arrays;

public class WordCounter implements Analyzer{
    private long counter = 0;

    public long getWordsCount() {
        return this.counter;
    }

    public void countWords(String[] text) {
        long sum = Arrays.stream(text).mapToInt(String::length).filter((length) -> length > 0)
                .count();
        this.counter = this.counter + sum;
    }

    @Override
    public void printResultToConsole() {
        System.out.println("Total words count - " + getWordsCount());
    }

    @Override
    public String getPrintResult() {
        return "Total words count - " + getWordsCount();
    }
}
