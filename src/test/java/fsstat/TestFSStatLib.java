package fsstat;

import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TestFSStatLib {

    private static final URL RESOURCE = TestFSStatLib.class.getClassLoader().getResource("test");
    private static final String DIRECTORY;
    private static final long MAX_SIZE = 200;
    private static final int N_BANDS = 2;
    private static final int N_FILES = 4;
    private static final int[] BANDS = new  int[] { 1, 1, 2 };

    static {
        try {
            assert RESOURCE != null;
            DIRECTORY = Paths.get(RESOURCE.toURI()).toAbsolutePath().toString();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testAsynchronous() throws ExecutionException, InterruptedException {
        final Report report = fsstat.asynchronous.FSStatLib.getFSReport(DIRECTORY, MAX_SIZE, N_BANDS).get();
        assertAll(
                () -> assertEquals(N_FILES, report.nFiles()),
                () -> assertEquals(BANDS[0], report.bands()[0]),
                () -> assertEquals(BANDS[1], report.bands()[1]),
                () -> assertEquals(BANDS[2], report.bands()[2])
        );
    }

}
