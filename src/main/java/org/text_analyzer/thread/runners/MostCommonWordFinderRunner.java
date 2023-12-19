package org.text_analyzer.thread.runners;

import org.text_analyzer.analyzers.MostCommonWordFinder;
import org.text_analyzer.thread.Interrupter;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class MostCommonWordFinderRunner extends TaskRunner {

    private final MostCommonWordFinder mostCommonWordFinder;

    public MostCommonWordFinderRunner(BlockingQueue<String[]> tasks, Interrupter interrupter) {
        super("MostCommonWordFinderRunner", tasks, interrupter);
        this.mostCommonWordFinder = new MostCommonWordFinder();
    }

    @Override
    public void run() {
        do {
            try {
                String[] text = this.tasks.poll(100, TimeUnit.MILLISECONDS);
                if (text != null) {
                    this.mostCommonWordFinder.countWordsOccurrence(text);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e.getMessage());
            }
        } while (!this.interrupter.isInterrupted() || !this.tasks.isEmpty());

        this.mostCommonWordFinder.printResultToConsole();
    }
}
