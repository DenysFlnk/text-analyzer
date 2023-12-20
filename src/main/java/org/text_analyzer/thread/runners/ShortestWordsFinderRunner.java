package org.text_analyzer.thread.runners;

import org.text_analyzer.analyzers.ShortestWordsFinder;
import org.text_analyzer.thread.Interrupter;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class ShortestWordsFinderRunner extends TaskRunner {

    private final ShortestWordsFinder shortestWordsFinder;

    public ShortestWordsFinderRunner(boolean includeOneLetterWords, BlockingQueue<String[]> tasks,
                                     Interrupter interrupter) {
        super("ShortestWordsFinderRunner", tasks, interrupter);
        this.shortestWordsFinder = new ShortestWordsFinder(includeOneLetterWords);
    }

    @Override
    public void run() {
        do {
            try {
                String[] text = this.tasks.poll(10, TimeUnit.MILLISECONDS);
                if (text != null) {
                    this.shortestWordsFinder.findShortestWordsInText(text);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e.getMessage());
            }
        } while (!this.interrupter.isInterrupted() || !this.tasks.isEmpty());

        this.shortestWordsFinder.printResultToConsole();
    }

    @Override
    public String getTaskResult() {
        return this.shortestWordsFinder.getPrintResult();
    }
}
