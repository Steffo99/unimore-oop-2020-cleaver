package eu.steffo.cleaver.logic.config;

/**
 * A {@link ISplitConfig} requesting the split of a file in parts of a specific {@link #size size}.
 */
public class SizeConfig implements ISplitConfig {
    /**
     * The size (in bytes) of a single part.
     */
    private long size;

    /**
     * Construct a new SizeConfig.
     * @param size The size (in bytes) of a single part.
     */
    public SizeConfig(long size) {
        this.size = size;
    }

    /**
     * @return The size (in bytes) of a single part.
     */
    public long getSize() {
        return size;
    }

    @Override
    public String toString() {
        return String.format("Split (%d bytes)", this.size);
    }
}
