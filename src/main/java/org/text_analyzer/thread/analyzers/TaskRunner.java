package org.text_analyzer.thread.analyzers;

import org.text_analyzer.thread.Interrupter;

import java.util.concurrent.BlockingQueue;

public abstract class TaskRunner extends Thread{

    protected final String name;

    protected final BlockingQueue<String[]> tasks;

    protected final Interrupter interrupter;

    public TaskRunner(String name, BlockingQueue<String[]> tasks, Interrupter interrupter) {
        super(name);
        this.name = name;
        this.tasks = tasks;
        this.interrupter = interrupter;
    }

    public void addTask(String[] text) {
        this.tasks.add(text);
    }
}
