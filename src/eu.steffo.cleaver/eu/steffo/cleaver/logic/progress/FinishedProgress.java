package eu.steffo.cleaver.logic.progress;

import eu.steffo.cleaver.logic.job.Job;

/**
 * A {@link Progress} that specifies that a {@link Job} has finished.
 */
public class FinishedProgress extends Progress {
    @Override
    public String toString() {
        return "Finished";
    }
}
