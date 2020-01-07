package eu.steffo.cleaver.logic.progress;

import eu.steffo.cleaver.logic.job.Job;

/**
 * A {@link Progress} that specifies that a {@link Job} hasn't started yet.
 */
public class NotStartedProgress extends Progress {
    @Override
    public String toString() {
        return "Not started";
    }
}
