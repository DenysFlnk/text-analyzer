package org.text_analyzer.thread;

import org.text_analyzer.thread.runners.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class Dispatcher {
    private final List<TaskRunner> analyzers;

    private final Interrupter interrupter;

    public Dispatcher() {
        this.interrupter = new Interrupter();
        this.analyzers = new ArrayList<>();
        initAnalyzers();
        startAnalyzers();
    }

    private void initAnalyzers() {
        this.analyzers.add(new ShortestWordsFinderRunner(false, new LinkedBlockingQueue<>(),
                this.interrupter));
        WordCounterRunner wcr = new WordCounterRunner(new LinkedBlockingQueue<>(), this.interrupter);
        this.analyzers.add(wcr);
        this.analyzers.add(new AverageWordLengthFinderRunner(wcr, new LinkedBlockingQueue<>(),
                this.interrupter));
        this.analyzers.add(new MostCommonWordFinderRunner(new LinkedBlockingQueue<>(), this.interrupter));
        this.analyzers.add(new LongestWordFinderRunner(new LinkedBlockingQueue<>(), this.interrupter));
    }

    private void startAnalyzers() {
        for (TaskRunner analyzer : this.analyzers) {
            analyzer.start();
        }
    }

    public void dispatch(String[] text) {
        for (TaskRunner analyzer : this.analyzers) {
            analyzer.addTask(text);
        }
    }

    public void interrupt() {
        this.interrupter.interrupt();
    }
}
