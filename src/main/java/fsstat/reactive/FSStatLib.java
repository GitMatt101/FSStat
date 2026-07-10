package fsstat.reactive;

import fsstat.Explorer;
import fsstat.Report;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.schedulers.Schedulers;

import java.nio.file.Files;
import java.nio.file.Paths;
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
        final Explorer explorer = new Explorer(_ -> {});
        return Observable.create((ObservableEmitter<String> emitter) -> {
                    try {
                        Files.walk(Paths.get(directory))
                                .filter(Files::isDirectory)
                                .forEach(p -> emitter.onNext(p.toAbsolutePath().toString()));
                        emitter.onComplete();
                    } catch (Exception e) {
                        emitter.onError(e);
                    }
                })
                .subscribeOn(Schedulers.io())
                .map(dir -> explorer.explore(dir, maxSize, nBands))
                .reduce(new Report(0, new int[nBands + 1]), Report::addReport)
                .toCompletionStage()
                .toCompletableFuture();
    }

}
