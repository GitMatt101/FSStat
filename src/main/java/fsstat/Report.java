package fsstat;

import java.util.Optional;

public record Report(int nFiles, int[] bands) {

    private static boolean hasError = false;
    private static String errorMessage = null;

    public void signalError(final String message) {
        hasError = true;
        errorMessage = message;
    }

    public boolean hasError() {
        return hasError;
    }

    public Optional<String> errorMessage() {
        return Optional.ofNullable(errorMessage);
    }

}
