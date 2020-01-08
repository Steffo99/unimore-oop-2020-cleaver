package eu.steffo.cleaver.logic.config;

/**
 * A {@link IConfig} for splitting a file in parts of a specific part size.
 */
public class SizeConfig implements ISplitConfig {
    /**
     * The size of the parts to split the file in.
     */
    private long partSize;

    /**
     * Construct a new SplitBySizeConfig.
     * @param partSize The size of the parts to split the file in.
     */
    public SizeConfig(long partSize) {
        this.partSize = partSize;
    }

    @Override
    public String toString() {
        return String.format("Split (%d bytes)", this.partSize);
    }

    public long getPartSize() {
        return partSize;
    }
}
