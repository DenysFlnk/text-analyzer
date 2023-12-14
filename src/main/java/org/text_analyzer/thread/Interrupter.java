package org.text_analyzer.thread;

public class Interrupter {
    private volatile boolean interrupted;

    public Interrupter() {
        this.interrupted = false;
    }

    public boolean isInterrupted() {
        return this.interrupted;
    }

    public void interrupt() {
        this.interrupted = true;
    }
}
