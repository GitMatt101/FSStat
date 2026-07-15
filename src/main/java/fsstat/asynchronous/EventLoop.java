package fsstat.asynchronous;

import fsstat.Explorer;
import fsstat.Report;
import fsstat.controller.Controller;

import java.io.IOException;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class EventLoop extends Thread {

    private final BlockingQueue<String> queue = new LinkedBlockingQueue<>();
    private final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);
    private final AtomicInteger activeTasks = new AtomicInteger(0);
    private final Explorer explorer;
    private final long maxSize;
    private final int nBands;
    private boolean running = true;
    private Report report;
    private final CompletableFuture<Report> future = new CompletableFuture<>();
    private boolean paused = true;
    private boolean continuousComputation = false;
    private final Controller controller;

    public EventLoop(final long maxSize, final int nBands) {
        this(maxSize, nBands, null);
    }

    public EventLoop(final long maxSize, final int nBands, final Controller controller) {
        this.explorer = new Explorer(this::addDirectoryToQueue);
        this.maxSize = maxSize;
        this.nBands = nBands;
        this.report = new Report("", 0, new int[nBands + 1]);
        this.controller = controller;
    }

    @Override
    public void run() {
        while (this.running) {
            try {
                final String directory = this.queue.take();
                if (this.controller != null) {
                    while (this.paused) {
                        try {
                            synchronized (this) {
                                this.wait();
                            }
                        } catch (InterruptedException _) {}
                    }
                    if (!this.continuousComputation) {
                        this.paused = true;
                        this.controller.toggleViewPause();
                    }
                }
                this.executor.submit(() -> {
                    try {
                        final Report newReport = this.explorer.explore(directory, this.maxSize, this.nBands);
                        this.addReport(newReport);
                    } catch (IOException e) {
                        this.shutdown(e.getMessage());
                    }
                    if (this.activeTasks.decrementAndGet() == 0 && this.queue.isEmpty()) {
                        this.shutdown();
                    }
                });
            } catch (InterruptedException e) {
                this.report = new Report("", 0, new int[this.nBands + 1]);
                this.report.signalError(e.getMessage());
                this.shutdown();
            }
        }
    }

    public void addDirectoryToQueue(final String directory) {
        this.queue.add(directory);
        this.activeTasks.incrementAndGet();
        if (this.controller != null) {
            this.controller.addDirectory(directory);
        }
    }

    private synchronized void addReport(final Report newReport) {
        this.report = this.report.directory().isEmpty() ? newReport : this.report.addReport(newReport);
        if (this.controller != null) {
            this.controller.addReport(newReport);
        }
    }

    public void shutdown() {
        this.running = false;
        this.executor.shutdown();
        this.future.complete(this.report);
        this.interrupt();
    }

    private void shutdown(final String errorMessage) {
        this.report.signalError(errorMessage);
        this.shutdown();
    }

    public CompletableFuture<Report> getReport() {
        return this.future;
    }

    public synchronized void togglePause() {
        this.paused = !this.paused;
        if (!this.paused) {
            this.notify();
        }
    }

    public void toggleMode() {
        this.continuousComputation = !this.continuousComputation;
    }

}
