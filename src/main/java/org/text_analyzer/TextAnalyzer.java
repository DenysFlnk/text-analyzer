package org.text_analyzer;

import org.text_analyzer.thread.Dispatcher;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class TextAnalyzer {

    private static final Dispatcher dispatcher = new Dispatcher();

    public static void main(String[] args) {
        PerformanceStopWatch stopWatch = new PerformanceStopWatch();
        String input = args[0];
        String output = args[1];

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
                TextAnalyzer.dispatcher.dispatch(text);
                lines.delete(0, lines.length());
            }
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException(String.format("File %s not found", input));
        } catch (IOException e) {
            throw new RuntimeException("I/O exception");
        }
        TextAnalyzer.dispatcher.interrupt();
        stopWatch.printExecutionTime();
    }

    private static String replaceAllNonLetterChar(String str) {
        return str.replaceAll("[^a-zA-Z]", " ");
    }
}