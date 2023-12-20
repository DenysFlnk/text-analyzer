package org.text_analyzer.thread.runners;

import org.text_analyzer.analyzers.WordCounter;
import org.text_analyzer.thread.Interrupter;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class WordCounterRunner extends TaskRunner {
    private final WordCounter wordCounter;

    private volatile boolean finished;

    public WordCounterRunner(BlockingQueue<String[]> tasks, Interrupter interrupter) {
        super("WordCounterRunner", tasks, interrupter);
        this.wordCounter = new WordCounter();
        this.finished = false;
    }

    @Override
    public void run() {
        do {
            try {
                String[] text = this.tasks.poll(10, TimeUnit.MILLISECONDS);
                if (text != null) {
                    this.wordCounter.countWords(text);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e.getMessage());
            }
        } while (!this.interrupter.isInterrupted() || !this.tasks.isEmpty());

        this.wordCounter.printResultToConsole();
        this.finished = true;
    }

    public boolean isFinished() {
        return this.finished;
    }

    public long getWordCount() {
        return this.wordCounter.getWordsCount();
    }

    @Override
    public String getTaskResult() {
        return this.wordCounter.getPrintResult();
    }
}
