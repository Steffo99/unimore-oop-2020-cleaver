package eu.steffo.cleaver.logic.config;

/**
 * A {@link ICompressConfig} requesting a compression with the <a href="https://en.wikipedia.org/wiki/DEFLATE">Deflate</a> algorithm.
 */
public class DeflateConfig implements ICompressConfig {
    @Override
    public String toString() {
        return "Compress (Deflate)";
    }
}
