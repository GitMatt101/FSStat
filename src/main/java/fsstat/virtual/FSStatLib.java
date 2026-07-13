package fsstat.virtual;

import fsstat.Explorer;
import fsstat.Report;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
        final Explorer explorer = new Explorer(_ -> {});
        return CompletableFuture.supplyAsync(() -> {
            Report report = new Report(0, new int[nBands + 1]);

            try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
                List<String> directories = Files.walk(Paths.get(directory))
                        .filter(Files::isDirectory)
                        .map(p -> p.toAbsolutePath().toString())
                        .toList();

                List<Future<Report>> futures = directories.stream()
                        .map(dir -> executor.submit(() -> explorer.explore(dir, maxSize, nBands)))
                        .toList();

                for (Future<Report> future : futures) {
                    report = report.addReport(future.get());
                }
            } catch (Exception e) {
                report.signalError(e.getMessage());
            }
            return report;
        }, Executors.newVirtualThreadPerTaskExecutor());
    }

}
