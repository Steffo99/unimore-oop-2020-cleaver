package eu.steffo.cleaver.logic.config;

/**
 * A {@link IConfig} for splitting a file in a specific number of parts.
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

    @Override
    public String toString() {
        return String.format("%d parts", this.parts);
    }

    public int getPartCount() {
        return parts;
    }
}
