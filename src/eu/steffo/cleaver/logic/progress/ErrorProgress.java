package eu.steffo.cleaver.logic.progress;

public class ErrorProgress extends Progress {
    public final Throwable error;

    public ErrorProgress(Throwable error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "Error: " + error.getMessage();
    }
}
