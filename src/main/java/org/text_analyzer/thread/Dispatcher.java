package org.text_analyzer.thread;

import org.text_analyzer.thread.analyzers.ShortestWordsFinderRunner;
import org.text_analyzer.thread.analyzers.TaskRunner;

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
        this.analyzers.add(new ShortestWordsFinderRunner(false, this.interrupter,
                new LinkedBlockingQueue<>()));
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
