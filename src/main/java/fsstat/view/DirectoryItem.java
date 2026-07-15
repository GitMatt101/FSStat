package fsstat.view;

public class DirectoryItem {

    private String path;
    private boolean explored = false;

    public DirectoryItem(final String path) {
        this.path = path;
    }

    public void markAsExplored(final int nFiles) {
        this.explored = true;
        this.path += " - files: " + nFiles;
    }

    public boolean isExplored() {
        return this.explored;
    }

    @Override
    public String toString() {
        return this.path;
    }

}
