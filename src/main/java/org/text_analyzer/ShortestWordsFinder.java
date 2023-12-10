package org.text_analyzer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class ShortestWordsFinder {

    private final int wordLowerBound;

    private String shortestWord = "SomeLongWordToInitializeClassField";

    private final List<String> shortestWordsList = new ArrayList<>();

    public ShortestWordsFinder(boolean includeOneLetterWords) {
        this.wordLowerBound = includeOneLetterWords ? 0 : 1;
    }

    public List<String> getShortestWords() {
        return shortestWordsList;
    }

    public void findShortestWordsInText(String[] text) {
        String firstSmallestWord;
        firstSmallestWord = Arrays.stream(text)
                .filter((word) -> word.length() > this.wordLowerBound && word.length() <= this.shortestWord.length())
                .min(Comparator.comparingInt(String::length)).orElse(null);

        if (firstSmallestWord != null) {
            Arrays.stream(text).filter((word) -> word.length() == firstSmallestWord.length())
                    .forEach(this::addWordToList);
        }
    }

    private void addWordToList(String newWord) {
        int shortestLength = this.shortestWord.length();
        int newLength = newWord.length();

        if (shortestLength == newLength && !this.shortestWordsList.contains(newWord)) {
            this.shortestWordsList.add(newWord);

        } else if (shortestLength > newLength) {
            this.shortestWord = newWord;
            this.shortestWordsList.clear();
            this.shortestWordsList.add(newWord);
        }
    }
}