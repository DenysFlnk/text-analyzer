package org.text_analyzer;

import org.text_analyzer.analyzers.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TextAnalyzer {
    private final WordsCounter wordsCounter;

    private final MostCommonWordFinder commonWordFinder;

    private final AverageWordLengthFinder avgWordLengthFinder;

    private final LongestWordFinder longestWordFinder;

    private final ShortestWordsFinder shortestWordsFinder;

    private final List<Analyzer> analyzers;

    public TextAnalyzer() {
        this.wordsCounter = new WordsCounter();
        this.commonWordFinder = new MostCommonWordFinder();
        this.avgWordLengthFinder = new AverageWordLengthFinder();
        this.longestWordFinder = new LongestWordFinder();
        this.shortestWordsFinder = new ShortestWordsFinder(false);
        this.analyzers = new ArrayList<>();
        initAnalyzersList();

    }

    private void initAnalyzersList() {
        analyzers.add(this.wordsCounter);
        analyzers.add(this.avgWordLengthFinder);
        analyzers.add(this.commonWordFinder);
        analyzers.add(this.shortestWordsFinder);
        analyzers.add(this.longestWordFinder);
    }

    public static void main(String[] args) {
        TextAnalyzer textAnalyzer = new TextAnalyzer();
        PerformanceStopWatch stopWatch = new PerformanceStopWatch();
        String input = args[0];
        String output = args[1];

        try (BufferedReader reader = new BufferedReader(new FileReader(input))) {
            StringBuilder lines = new StringBuilder();
            while (reader.ready()) {
                String line = reader.readLine().replaceAll("[^a-zA-Z]", " ");
                if (lines.isEmpty()) {
                    lines.append(line);
                } else {
                    lines.append(" ").append(line);
                }
            }
            String[] text = lines.toString().split(" ");

            textAnalyzer.shortestWordsFinder.findShortestWordsInText(text);
            textAnalyzer.wordsCounter.countWords(text);
            textAnalyzer.commonWordFinder.countWordsOccurrence(text);
            textAnalyzer.avgWordLengthFinder.countCharacters(text);
            textAnalyzer.avgWordLengthFinder.computeAverageWordLength(textAnalyzer.wordsCounter.getWordsCount());
            textAnalyzer.longestWordFinder.findLongestWord(text);
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException(String.format("File %s not found", input));
        } catch (IOException e) {
            throw new RuntimeException("I/O exception");
        }

        textAnalyzer.printResultsToConsole();
        textAnalyzer.writeResultsToFile(output);
        stopWatch.printExecutionTime();
    }

    private void printResultsToConsole() {
        for (Analyzer analyzer : this.analyzers) {
            analyzer.printResultToConsole();
        }
    }

    private void writeResultsToFile(String outputFile) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            for (int i = 0; i < this.analyzers.size(); i++) {
                writer.write(this.analyzers.get(i).getPrintResult());
                if (i != this.analyzers.size() - 1) {
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("I/O exception");
        }
    }
}