package org.text_analyzer.analyzers;

import java.util.*;

public class ShortestWordsFinder implements ResultPrintable {

    private final int wordLowerBound;

    private String shortestWord;

    private final HashSet<String> shortestWordsSet;

    public ShortestWordsFinder(boolean includeOneLetterWords) {
        this.wordLowerBound = includeOneLetterWords ? 0 : 1;
        this.shortestWord = "SomeLongWordToInitializeClassField";
        this.shortestWordsSet = new HashSet<>();
    }

    public void findShortestWordsInText(String[] text) {
        String firstSmallestWord;
        firstSmallestWord = Arrays.stream(text)
                .filter((word) -> word.length() > this.wordLowerBound && word.length() <= this.shortestWord.length())
                .min(Comparator.comparingInt(String::length)).orElseThrow(() -> new RuntimeException("Empty text"));

        Arrays.stream(text).filter((word) -> word.length() == firstSmallestWord.length())
                .map(String::toLowerCase)
                .forEach(this::addWordToList);

    }

    private void addWordToList(String newWord) {
        int shortestLength = this.shortestWord.length();
        int newLength = newWord.length();

        if (shortestLength == newLength) {
            this.shortestWordsSet.add(newWord);

        } else if (shortestLength > newLength) {
            this.shortestWord = newWord;
            this.shortestWordsSet.clear();
            this.shortestWordsSet.add(newWord);
        }
    }

    @Override
    public void printResultToConsole() {
        System.out.println("Shortest words - " + getShortestWords().toString());
    }

    @Override
    public String getPrintResult() {
        return "Shortest words - " + getShortestWords().toString();
    }

    public Set<String> getShortestWords() {
        return shortestWordsSet;
    }
}
