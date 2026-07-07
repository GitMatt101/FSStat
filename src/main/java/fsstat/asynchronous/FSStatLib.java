package fsstat.asynchronous;

import fsstat.Report;

import java.util.concurrent.CompletableFuture;

public class FSStatLib {

    public static CompletableFuture<Report> getFSReport(final String directory, final long maxSize, final int nBands) {
        final EventLoop loop = new EventLoop(maxSize, nBands);
        loop.addDirectoryToQueue(directory);
        loop.start();
        return loop.getReport();
    }

}
