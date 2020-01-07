package eu.steffo.cleaver.logic.config;

import eu.steffo.cleaver.logic.job.Job;

/**
 * A class containing the configuration for the split/merge step of a {@link Job Job}.
 */
public abstract class SplitConfig {
    /**
     * @return The size in bytes of a single part of the file.
     */
    public abstract long getPartSize();

    /**
     * @return The number of parts the file should be split in.
     */
    public abstract int getPartCount();

    /**
     * @return The total size of the original file.
     */
    public abstract long getTotalSize();

    /**
     * @return The string representation of the {@link SplitConfig}, to be used in the jobs table.
     * @see eu.steffo.cleaver.gui.panels.JobsTablePanel
     */
    public abstract String toString();
}
