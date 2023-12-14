package org.text_analyzer.thread.analyzers;

import org.text_analyzer.analyzers.ShortestWordsFinder;
import org.text_analyzer.thread.Interrupter;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class ShortestWordsFinderRunner extends TaskRunner{

    private final ShortestWordsFinder shortestWordsFinder;

    private final Interrupter interrupter;

    private final BlockingQueue<String[]> blockingQueue;

    public ShortestWordsFinderRunner(boolean includeOneLetterWords, Interrupter interrupter,
                                     BlockingQueue<String[]> blockingQueue) {
        super("ShortestWordsFinderRunner");
        this.shortestWordsFinder = new ShortestWordsFinder(includeOneLetterWords);
        this.interrupter = interrupter;
        this.blockingQueue = blockingQueue;
    }

    @Override
    public void run() {
        do {
            try {
                String[] text = blockingQueue.poll(100, TimeUnit.MILLISECONDS);
                if (text != null) {
                    this.shortestWordsFinder.findShortestWordsInText(text);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e.getMessage());
            }
        } while (!interrupter.isInterrupted());

        this.shortestWordsFinder.printResultToConsole();
    }

    @Override
    public void addTask(String[] text) {
        this.blockingQueue.add(text);
    }
}
