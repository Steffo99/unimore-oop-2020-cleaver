package eu.steffo.cleaver.logic.progress;

/**
 * A {@link Progress} that specifies that a {@link eu.steffo.cleaver.logic.Job} hasn't started yet.
 */
public class NotStartedProgress extends Progress {
    @Override
    public String toString() {
        return "Not started";
    }
}
