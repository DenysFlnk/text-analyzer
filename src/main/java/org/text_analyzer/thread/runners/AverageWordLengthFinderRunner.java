package org.text_analyzer.thread.runners;

import org.text_analyzer.analyzers.AverageWordLengthFinder;
import org.text_analyzer.thread.Interrupter;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class AverageWordLengthFinderRunner extends TaskRunner{

    private final AverageWordLengthFinder avgWordLengthFinder;

    private final WordCounterRunner wordCounterRunner;

    public AverageWordLengthFinderRunner(WordCounterRunner wordCounterRunner, BlockingQueue<String[]> tasks,
                                         Interrupter interrupter) {
        super("AverageWordLengthFinderRunner", tasks, interrupter);
        this.avgWordLengthFinder = new AverageWordLengthFinder();
        this.wordCounterRunner = wordCounterRunner;
    }

    @Override
    public void run() {
        do {
            try {
                String[] text = this.tasks.poll(100, TimeUnit.MILLISECONDS);
                if (text != null) {
                    this.avgWordLengthFinder.countCharacters(text);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e.getMessage());
            }
        } while (!this.interrupter.isInterrupted() || !this.tasks.isEmpty());

        while (true) {
            if (this.wordCounterRunner.isFinished()) {
                this.avgWordLengthFinder.computeAverageWordLength(this.wordCounterRunner.getWordCount());
                break;
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e.getMessage());
            }
        }

        this.avgWordLengthFinder.printResultToConsole();
    }
}
