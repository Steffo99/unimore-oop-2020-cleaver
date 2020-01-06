package eu.steffo.cleaver.logic.progress;

/**
 * A {@link Progress} that specifies that a {@link eu.steffo.cleaver.logic.Job} has finished.
 */
public class FinishedProgress extends Progress {
    @Override
    public String toString() {
        return "Finished";
    }
}
