package eu.steffo.cleaver.logic.config;

/**
 * A {@link ISplitConfig} requesting the split of a file in a specific number of {@link #parts}.
 */
public class PartsConfig implements ISplitConfig {
    /**
     * The number of parts the file should be split in.
     */
    private int parts;

    /**
     * Construct a new SplitByPartsConfig.
     * @param parts The number of parts the file should be split in.
     */
    public PartsConfig(int parts) {
        this.parts = parts;
    }
    /**
     * @return The number of parts the file should be split in.
     */
    public int getPartCount() {
        return parts;
    }

    @Override
    public String toString() {
        return String.format("Split (%d parts)", this.parts);
    }

}
