package org.text_analyzer.thread;

import org.text_analyzer.thread.runners.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class Dispatcher {
    private final List<TaskRunner> taskRunners;

    private final Interrupter interrupter;

    public Dispatcher() {
        this.interrupter = new Interrupter();
        this.taskRunners = new ArrayList<>();
        initAnalyzers();
        startAnalyzers();
    }

    private void initAnalyzers() {
        this.taskRunners.add(new ShortestWordsFinderRunner(false, new LinkedBlockingQueue<>(),
                this.interrupter));
        WordCounterRunner wcr = new WordCounterRunner(new LinkedBlockingQueue<>(), this.interrupter);
        this.taskRunners.add(wcr);
        this.taskRunners.add(new AverageWordLengthFinderRunner(wcr, new LinkedBlockingQueue<>(),
                this.interrupter));
        this.taskRunners.add(new MostCommonWordFinderRunner(new LinkedBlockingQueue<>(), this.interrupter));
        this.taskRunners.add(new LongestWordFinderRunner(new LinkedBlockingQueue<>(), this.interrupter));
    }

    private void startAnalyzers() {
        for (TaskRunner analyzer : this.taskRunners) {
            analyzer.start();
        }
    }

    public void dispatch(String[] text) {
        for (TaskRunner analyzer : this.taskRunners) {
            analyzer.addTask(text);
        }
    }

    public void interrupt() {
        this.interrupter.interrupt();
    }

    public List<TaskRunner> getTaskRunners() {
        return this.taskRunners;
    }
}
