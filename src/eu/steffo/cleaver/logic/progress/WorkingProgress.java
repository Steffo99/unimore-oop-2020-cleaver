package eu.steffo.cleaver.logic.progress;

/**
 * A {@link Progress} that specifies that a {@link eu.steffo.cleaver.logic.Job} is currently running and has progressed to {@link #progress} %.
 */
public class WorkingProgress extends Progress {
    public final float progress;

    /**
     * Create a new WorkingProgress at 0%.
     */
    public WorkingProgress() {
        this.progress = 0f;
    }

    /**
     * Create a new WorkingProgress at a specific percentage.
     * @param progress The job progress from 0.0f to 1.0f.
     */
    public WorkingProgress(float progress) {
        this.progress = progress;
    }

    @Override
    public String toString() {
        return String.format("%.0f%%", progress * 100);
    }
}
