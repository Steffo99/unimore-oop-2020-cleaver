package eu.steffo.cleaver.logic.progress;

public class WorkingProgress extends Progress {
    protected float progress;

    public WorkingProgress() {
        this.progress = 0f;
    }

    public WorkingProgress(float progress) {
        this.progress = progress;
    }

    @Override
    public String toString() {
        return String.format("%.0f%%", progress * 100);
    }
}
