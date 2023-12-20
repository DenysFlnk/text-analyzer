package org.text_analyzer;

import org.text_analyzer.thread.Dispatcher;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.text_analyzer.analyzers.*;
import org.text_analyzer.thread.runners.TaskRunner;

public class TextAnalyzer {

    private static Dispatcher dispatcher;

    private static WordCounter wordsCounter;

    private static MostCommonWordFinder commonWordFinder;

    private static AverageWordLengthFinder avgWordLengthFinder;

    private static LongestWordFinder longestWordFinder;

    private static ShortestWordsFinder shortestWordsFinder;

    private static List<ResultPrintable> results;

    public static void main(String[] args) {
        PerformanceStopWatch stopWatch = new PerformanceStopWatch();
        String input = args[0];
        String output = args[1];
        boolean multiTread = Boolean.parseBoolean(args[2]);

        TextAnalyzer.init(multiTread);

        try (BufferedReader reader = new BufferedReader(new FileReader(input))) {
            StringBuilder lines = new StringBuilder();
            while (reader.ready()) {
                for (int i = 0; i < 100 && reader.ready(); i++) {
                    String line = replaceAllNonLetterChar(reader.readLine());
                    if (lines.isEmpty()) {
                        lines.append(line);
                    } else {
                        lines.append(" ").append(line);
                    }
                }

                String[] text = lines.toString().split(" ");
                TextAnalyzer.analyze(text, multiTread);
                lines.delete(0, lines.length());
            }
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException(String.format("File %s not found", input));
        } catch (IOException e) {
            throw new RuntimeException("I/O exception");
        }
        printResults(output, multiTread);
        stopWatch.printExecutionTime();
    }

    private static void init(boolean multiTread) {
        if (multiTread) {
            TextAnalyzer.dispatcher = new Dispatcher();
        } else {
            TextAnalyzer.wordsCounter = new WordCounter();
            TextAnalyzer.commonWordFinder = new MostCommonWordFinder();
            TextAnalyzer.avgWordLengthFinder = new AverageWordLengthFinder();
            TextAnalyzer.longestWordFinder = new LongestWordFinder();
            TextAnalyzer.shortestWordsFinder = new ShortestWordsFinder(false);
            TextAnalyzer.results = new ArrayList<>();
            TextAnalyzer.results.add(TextAnalyzer.wordsCounter);
            TextAnalyzer.results.add(TextAnalyzer.commonWordFinder);
            TextAnalyzer.results.add(TextAnalyzer.avgWordLengthFinder);
            TextAnalyzer.results.add(TextAnalyzer.longestWordFinder);
            TextAnalyzer.results.add(TextAnalyzer.shortestWordsFinder);
        }
    }

    private static String replaceAllNonLetterChar(String str) {
        return str.replaceAll("[^a-zA-Z]", " ");
    }

    private static void analyze(String[] text, boolean multiTread) {
        if (multiTread) {
            TextAnalyzer.dispatcher.dispatch(text);
        } else {
            TextAnalyzer.shortestWordsFinder.findShortestWordsInText(text);
            TextAnalyzer.wordsCounter.countWords(text);
            TextAnalyzer.commonWordFinder.countWordsOccurrence(text);
            TextAnalyzer.avgWordLengthFinder.countCharacters(text);
            TextAnalyzer.longestWordFinder.findLongestWord(text);
        }
    }

    private static void printResults(String outputFile, boolean multiTread) {
        if (multiTread) {
            TextAnalyzer.dispatcher.interrupt();
            for (Thread thread : TextAnalyzer.dispatcher.getTaskRunners()) {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        } else {
            TextAnalyzer.avgWordLengthFinder.computeAverageWordLength(TextAnalyzer.wordsCounter.getWordsCount());

            TextAnalyzer.results.forEach(ResultPrintable::printResultToConsole);
        }
        writeResultsToFile(outputFile, multiTread);
    }

    private static void writeResultsToFile(String outputFile, boolean multiTread) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            if (multiTread) {
                writeResultsFromTaskRunners(writer);
            } else {
                writeResultsFromResultPrintable(writer);
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private static void writeResultsFromResultPrintable(BufferedWriter writer) throws IOException {
        for (int i = 0; i < TextAnalyzer.results.size(); i++) {
            writer.write(TextAnalyzer.results.get(i).getPrintResult());
            if (i != TextAnalyzer.results.size() - 1) {
                writer.newLine();
            }
        }
    }

    private static void writeResultsFromTaskRunners(BufferedWriter writer) throws IOException {
        List<TaskRunner> taskRunners = TextAnalyzer.dispatcher.getTaskRunners();
        for (int i = 0; i < taskRunners.size(); i++) {
            writer.write(taskRunners.get(i).getTaskResult());
            if (i != taskRunners.size() - 1) {
                writer.newLine();
            }
        }
    }
}