package fsstat.asynchronous;

import fsstat.Report;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

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

    /**
     * Initializes the event loop.
     *
     * @param maxSize max size (in bytes) of a file
     * @param nBands number of bands to define statistics
     */
    public EventLoop(final long maxSize, final int nBands) {
        this.explorer = new Explorer(this::addDirectoryToQueue);
        this.maxSize = maxSize;
        this.nBands = nBands;
        this.report = new Report(0, new int[nBands + 1]);
    }

    @Override
    public void run() {
        while (this.running) {
            try {
                final String directory = this.queue.take();
                this.executor.submit(() -> {
                    try {
                        final Report stats = this.explorer.explore(directory, this.maxSize, this.nBands);
                        this.addReport(stats);
                    } catch (IOException e) {
                        this.shutdown(e.getMessage());
                    }
                    if (this.activeTasks.decrementAndGet() == 0 && this.queue.isEmpty()) {
                        this.shutdown();
                    }
                });
            } catch (InterruptedException _) {}
        }
    }

    /**
     * Adds a directory to explore to the queue.
     *
     * @param directory name of the directory
     */
    public void addDirectoryToQueue(final String directory) {
        this.queue.add(directory);
        this.activeTasks.incrementAndGet();
    }

    /**
     * Adds a report to the current one.
     * The number of files and the distributions of files are updated.
     *
     * @param newReport new report to include to the current one
     */
    private synchronized void addReport(final Report newReport) {
        final int[] newBands = new int[this.nBands + 1];
        for (int i = 0; i < this.nBands + 1; i++) {
            newBands[i] = this.report.bands()[i] + newReport.bands()[i];
        }
        this.report = new Report(this.report.nFiles() + newReport.nFiles(), newBands);
    }

    /**
     * Shuts down the process.
     */
    private void shutdown() {
        this.running = false;
        this.executor.shutdown();
        this.future.complete(this.report);
    }

    /**
     * Shuts down the process while saving an error message.
     *
     * @param errorMessage the error that will be displayed
     */
    private void shutdown(final String errorMessage) {
        this.report.signalError(errorMessage);
        this.shutdown();
    }

    /**
     * @return a {@link CompletableFuture} containing the final report
     */
    public CompletableFuture<Report> getReport() {
        return this.future;
    }

}
