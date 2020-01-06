package eu.steffo.cleaver.logic.progress;

/**
 * A {@link Progress} that specifies that a {@link eu.steffo.cleaver.logic.Job} stopped because an exception occoured.
 */
public class ErrorProgress extends Progress {
    private final Throwable error;

    /**
     * Create a new ErrorProgress for a specific error.
     * @param error The encountered error.
     */
    public ErrorProgress(Throwable error) {
        this.error = error;
    }

    /**
     * @return The error encountered by the {@link eu.steffo.cleaver.logic.Job}.
     */
    public Throwable getError() {
        return error;
    }

    @Override
    public String toString() {
        return "Error: " + error.getMessage();
    }
}
