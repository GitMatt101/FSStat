package fsstat.view;

public class DirectoryItem {

    private final String path;
    private boolean explored = false;

    public DirectoryItem(final String path) {
        this.path = path;
    }

    public void markAsExplored() {
        this.explored = true;
    }

    public boolean isExplored() {
        return this.explored;
    }

    @Override
    public String toString() {
        return this.path;
    }

}
