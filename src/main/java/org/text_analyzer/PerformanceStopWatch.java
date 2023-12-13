package org.text_analyzer;

public class PerformanceStopWatch {
    private final long startMillis;

    public PerformanceStopWatch() {
        this.startMillis = System.currentTimeMillis();
    }

    public void printExecutionTime() {
        long execTime = System.currentTimeMillis() - startMillis;
        System.out.println("Execution time - " + execTime + " ms");
    }
}
