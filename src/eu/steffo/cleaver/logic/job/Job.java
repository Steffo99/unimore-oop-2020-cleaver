package eu.steffo.cleaver.logic.job;

import java.io.File;
import javax.swing.SwingUtilities;

import eu.steffo.cleaver.logic.progress.*;

/**
 * A task that can be executed by Cleaver in a separate {@link Thread}, and that keeps track of its progress and can display progress updates on a Swing GUI.
 */
public abstract class Job extends Thread {
    /**
     * The current {@link Progress} of the job.
     * @see NotStartedProgress
     * @see WorkingProgress
     * @see FinishedProgress
     * @see ErrorProgress
     */
    private Progress progress;

    /**
     * A {@link Runnable} that will be invoked on the GUI {@link Thread} (with {@link SwingUtilities#invokeLater(Runnable)}) every time
     * {@link #setProgress(Progress)} is called.
     */
    private Runnable onProgressChange = null;

    /**
     * Construct a Job and set its progress to {@link NotStartedProgress Not started}.
     */
    public Job() {
        this.progress = new NotStartedProgress();
    }

    /**
     * Construct a Job, set its progress to {@link NotStartedProgress Not started} and set the {@link Runnable} that will be called on the GUI {@link Thread}
     * every time the progress changes.
     *
     * @param onProgressChange A {@link Runnable} that will be invoked on the GUI {@link Thread} (with {@link SwingUtilities#invokeLater(Runnable)}) every time
     *                         {@link #setProgress(Progress)} is called.
     */
    public Job(Runnable onProgressChange) {
        this();
        this.onProgressChange = onProgressChange;
        if(onProgressChange != null) {
            SwingUtilities.invokeLater(onProgressChange);
        }
    }

    /**
     * @return The current {@link Progress} of the job.
     * @see NotStartedProgress
     * @see WorkingProgress
     * @see FinishedProgress
     * @see ErrorProgress
     */
    public Progress getProgress() {
        return progress;
    }

    /**
     * Set the progress of the job to a different value.
     *
     * If {@link #onProgressChange} is set, schedule its invocation after the set, allowing for example the updating of a
     * {@link javax.swing.table.TableModel TableModel} after the progress change.
     *
     * @param progress The value {@link #progress} should be set to.
     */
    protected void setProgress(Progress progress) {
        this.progress = progress;
        if(onProgressChange != null) {
            SwingUtilities.invokeLater(onProgressChange);
        }
    }

    /**
     * @return The {@link String} that should be displayed in the <b>Type</b> column of the {@link eu.steffo.cleaver.gui.panels.JobsTablePanel Jobs Table}.
     */
    public abstract String getTypeString();

    /**
     * @return The {@link String} that should be displayed in the <b>File</b> column of the {@link eu.steffo.cleaver.gui.panels.JobsTablePanel Jobs Table}.
     */
    public abstract String getFileString();

    /**
     * @return The {@link String} that should be displayed in the <b>Operations</b> column of the
     *         {@link eu.steffo.cleaver.gui.panels.JobsTablePanel Jobs Table}.
     */
    public abstract String getOperationsString();

    /**
     * @return The {@link String} that should be displayed in the <b>Progress</b> column of the {@link eu.steffo.cleaver.gui.panels.JobsTablePanel Jobs Table}.
     */
    public String getProgressString() {
        return progress.toString();
    }

    @Override
    public abstract void run();
}

