package fsstat.asynchronous;

import fsstat.Report;

import java.util.concurrent.CompletableFuture;

public class FSStatLib {

    /**
     * Computes statistics about files in a directory and all its subdirectories recursively.
     * It counts how many files are in the directory and how they are distributed.
     *
     * @param directory the directory to explore
     * @param maxSize maximum file size for statistics
     * @param nBands number of bands to divide the files sizes distribution
     * @return a {@link Report} containing the number of files and their distribution in the directory
     */
    public static CompletableFuture<Report> getFSReport(final String directory, final long maxSize, final int nBands) {
        final EventLoop loop = new EventLoop(maxSize, nBands);
        loop.addDirectoryToQueue(directory);
        loop.start();
        return loop.getReport();
    }

}
