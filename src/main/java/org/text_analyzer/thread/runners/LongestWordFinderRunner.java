package org.text_analyzer.thread.runners;

import org.text_analyzer.analyzers.LongestWordFinder;
import org.text_analyzer.thread.Interrupter;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class LongestWordFinderRunner extends TaskRunner {

    private final LongestWordFinder longestWordFinder;

    public LongestWordFinderRunner(BlockingQueue<String[]> tasks, Interrupter interrupter) {
        super("LongestWordFinderRunner", tasks, interrupter);
        this.longestWordFinder = new LongestWordFinder();
    }

    @Override
    public void run() {
        do {
            try {
                String[] text = this.tasks.poll(100, TimeUnit.MILLISECONDS);
                if (text != null) {
                    this.longestWordFinder.findLongestWord(text);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e.getMessage());
            }
        } while (!this.interrupter.isInterrupted() || !this.tasks.isEmpty());

        this.longestWordFinder.printResultToConsole();
    }
}
