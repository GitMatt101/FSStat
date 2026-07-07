package fsstat;

@FunctionalInterface
public interface Handler {

    void exploreSubdirectory(final String directory);

}
