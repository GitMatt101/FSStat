package fsstat;

import java.util.Optional;

public record Report(String directory, int nFiles, int[] bands) {

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

    public Report addReport(final Report newReport) {
        final int[] newBands = new int[this.bands.length];
        for (int i = 0; i < this.bands.length; i++) {
            newBands[i] = this.bands[i] + newReport.bands()[i];
        }
        return new Report(this.directory, this.nFiles + newReport.nFiles(), newBands);
    }

}
