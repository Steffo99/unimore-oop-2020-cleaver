package eu.steffo.cleaver.logic.split;

/**
 * A {@link SplitConfig} for splitting a file in parts of a specific part size.
 */
public class SplitBySizeConfig extends SplitConfig {
    /**
     * The size of the parts to split the file in.
     */
    private long partSize;

    /**
     * The total size of the file to split.
     */
    private long totalSize;

    /**
     * Construct a new SplitBySizeConfig.
     * @param partSize The size of the parts to split the file in.
     * @param totalSize The total size of the file to split.
     */
    public SplitBySizeConfig(long partSize, long totalSize) {
        this.partSize = partSize;
        this.totalSize = totalSize;
    }

    @Override
    public String toString() {
        return String.format("%d bytes", this.partSize);
    }

    @Override
    public long getPartSize() {
        return partSize;
    }

    @Override
    public int getPartCount() {
        return (int)Math.ceil((double) totalSize / (double)partSize);
    }

    @Override
    public long getTotalSize() {
        return totalSize;
    }
}
