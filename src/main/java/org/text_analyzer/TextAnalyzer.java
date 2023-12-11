package org.text_analyzer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class TextAnalyzer {
    public static void main(String[] args) {
        String input = args[0];

        ShortestWordsFinder shortestWordsFinder = new ShortestWordsFinder(false);
        WordsCounter wordsCounter = new WordsCounter();
        MostCommonWordFinder commonWordFinder = new MostCommonWordFinder();
        AverageWordLengthFinder averageWordLengthFinder = new AverageWordLengthFinder();
        LongestWordFinder longestWordFinder = new LongestWordFinder();

        try (BufferedReader reader = new BufferedReader(new FileReader(input))) {
            StringBuilder lines = new StringBuilder();
            while (reader.ready()) {
                String line = reader.readLine().replaceAll("[^\\w\\s_-]", " ");
                if (lines.isEmpty()) {
                    lines.append(line);
                } else {
                    lines.append(" ").append(line);
                }
            }
            String[] text = lines.toString().split(" ");

            shortestWordsFinder.findShortestWordsInText(text);
            wordsCounter.countWords(text);
            commonWordFinder.countWordsOccurrence(text);
            averageWordLengthFinder.countCharacters(text);
            longestWordFinder.findLongestWord(text);
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException(String.format("File %s not found", input));
        } catch (IOException e) {
            throw new RuntimeException("I/O exception");
        }

        System.out.println(shortestWordsFinder.getShortestWords());
        System.out.println(commonWordFinder.getMostCommonWord());
        System.out.println(wordsCounter.getWordsCount());
        System.out.println(averageWordLengthFinder.getAverageWordLength(wordsCounter.getWordsCount()));
        System.out.println(longestWordFinder.getLongestWord());
    }
}