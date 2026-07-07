package fsstat.asynchronous;

import fsstat.Handler;
import fsstat.Report;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicInteger;

public class Explorer {

    private final Handler handler;

    public Explorer(final Handler handler) {
        this.handler = handler;
    }

    /**
     * Explores a directory, calculating stats for each file inside it.
     * If another directory is found, signal the event loop to explore that one too.
     *
     * @param directory name of the directory
     * @param maxSize maximum file size for statistics
     * @param bands number of bands to generate statistics about files sizes
     * @return the statistics regarding all the files in the directory
     */
    public Report explore(final String directory, final long maxSize, final int bands) throws IOException {
        final Path path = Paths.get(directory);
        if (!Files.exists(path) || !Files.isDirectory(path))
            return null;

        AtomicInteger files = new AtomicInteger();
        int[] sizes = new int[bands + 1];
        Files.list(path).forEach(p -> {
            if (Files.isDirectory(p)) {
                this.handler.exploreSubdirectory(p.toAbsolutePath().toString());
            } else if (Files.isRegularFile(p)) {
                files.getAndIncrement();
                try {
                    long bytes = Files.size(p);
                    final int index = (int) (bytes * (bands + 1) / maxSize);
                    sizes[Math.min(index, bands)]++;
                } catch (IOException _) {}
            }
        });
        return new Report(files.get(), sizes);
    }

}
