package eu.steffo.cleaver.logic.config;

import eu.steffo.cleaver.logic.job.Job;

/**
 * An interface for the configuration of a step of a {@link Job Job}.
 */
public interface IConfig {
    /**
     * @return The string representation of the {@link IConfig}, to be used in the jobs table.
     * @see eu.steffo.cleaver.gui.panels.JobsTablePanel
     */
    String toString();
}
