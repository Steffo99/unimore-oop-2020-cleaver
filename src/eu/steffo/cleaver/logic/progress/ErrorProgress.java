package eu.steffo.cleaver.logic.progress;

import eu.steffo.cleaver.logic.job.Job;

/**
 * A {@link Progress} that specifies that a {@link Job} stopped because an exception occoured.
 */
public class ErrorProgress extends Progress {
    /**
     * The error encountered by the {@link Job}.
     */
    private final Throwable error;

    /**
     * Create a new ErrorProgress for a specific error.
     * @param error The error encountered by the {@link Job}.
     */
    public ErrorProgress(Throwable error) {
        this.error = error;
    }

    /**
     * @return The error encountered by the {@link Job}.
     */
    public Throwable getError() {
        return error;
    }

    @Override
    public String toString() {
        return "Error: " + error.getMessage();
    }
}
