package eu.steffo.cleaver.logic.config;

public class SplitByPartsConfig extends SplitConfig {
    /**
     * The number of parts the file should be split in.
     */
    private int parts;

    /**
     * The total size of the file to split.
     */
    private long totalSize;

    /**
     * Construct a new SplitByPartsConfig.
     * @param parts The number of parts the file should be split in.
     * @param totalSize The total size of the file to split.
     */
    public SplitByPartsConfig(int parts, long totalSize) {
        this.parts = parts;
        this.totalSize = totalSize;
    }

    @Override
    public String toString() {
        return String.format("%d parts", this.parts);
    }

    @Override
    public long getPartSize() {
        return (int)Math.ceil((double) totalSize / (double)parts);
    }

    @Override
    public int getPartCount() {
        return parts;
    }

    @Override
    public long getTotalSize() {
        return totalSize;
    }
}
