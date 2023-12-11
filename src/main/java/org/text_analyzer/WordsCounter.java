package org.text_analyzer;

import java.util.Arrays;

public class WordsCounter {
    private long counter = 0;

    public long getWordsCount() {
        return this.counter;
    }

    public void countWords(String[] text) {
        long sum = Arrays.stream(text).mapToInt(String::length).filter((length) -> length > 0)
                .count();
        this.counter = this.counter + sum;
    }
}
