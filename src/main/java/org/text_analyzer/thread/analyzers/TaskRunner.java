package org.text_analyzer.thread.analyzers;

public abstract class TaskRunner extends Thread{

    protected String name;

    public TaskRunner(String name) {
        super(name);
        this.name = name;
    }

    public abstract void addTask(String[] text);
}
