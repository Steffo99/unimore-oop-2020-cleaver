package eu.steffo.cleaver.logic.split;

/**
 * A {@link SplitConfig} created by reading a *.chp file, containing the number of parts and their size, but not the resulting file size.
 */
public class MergeConfig extends SplitConfig {
    /**
     * The size of the parts the file was split in.
     */
    private long partSize;

    /**
     * The number of parts the file was split in.
     */
    private int parts;

    /**
     * The total size of the original file.
     */
    private long totalSize;

    /**
     * Construct a new MergeConfig.
     * @param partSize The size of the parts the file was split in.
     * @param parts The number of parts the file was split in.
     * @param totalSize The total size of the original file.
     */
    public MergeConfig(long partSize, int parts, long totalSize) {
        this.partSize = partSize;
        this.parts = parts;
        this.totalSize = totalSize;
    }

    @Override
    public long getPartSize() {
        return partSize;
    }

    @Override
    public int getPartCount() {
        return parts;
    }

    @Override
    public long getTotalSize() {
        return totalSize;
    }

    @Override
    public String toString() {
        return String.format("%d parts (%d bytes)", parts, partSize);
    }
}
