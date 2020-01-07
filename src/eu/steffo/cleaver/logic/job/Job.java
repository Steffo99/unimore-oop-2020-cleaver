package eu.steffo.cleaver.logic.job;

import java.io.File;
import javax.swing.SwingUtilities;

import eu.steffo.cleaver.logic.config.CompressConfig;
import eu.steffo.cleaver.logic.config.CryptConfig;
import eu.steffo.cleaver.logic.progress.NotStartedProgress;
import eu.steffo.cleaver.logic.progress.Progress;
import eu.steffo.cleaver.logic.config.SplitConfig;

/**
 * A {@link Thread} that allows access to the basic .
 */
public abstract class Job extends Thread {

    private Progress progress;
    private Runnable onProgressChange = null;

    /**
     * Construct a Job, setting its progress to {@link NotStartedProgress Not started}.
     */
    public Job() {
        this.progress = new NotStartedProgress();
    }

    /**
     * Construct a Job, then add to it a {@link Runnable} that will be invoked through {@link SwingUtilities#invokeLater(Runnable) invokeLater} on progress
     * changes.
     * @param onProgressChange A {@link Runnable} that should be invoked when {@link #setProgress(Progress)} is called.
     * @see Job()
     */
    public Job(Runnable onProgressChange) {
        this();
        this.onProgressChange = onProgressChange;
        if(onProgressChange != null) {
            SwingUtilities.invokeLater(onProgressChange);
        }
    }

    /**
     * @return The name of the job type, such as "Chop" for a {@link ChopJob}.
     *
     * It will be displayed in the {@link eu.steffo.cleaver.gui.panels.JobsTablePanel}.
     */
    public abstract String getType();

    /**
     * @return The file path the job should act on.
     */
    public abstract File getFile();

    /**
     * @return The current progress of the job.
     * @see eu.steffo.cleaver.logic.progress.NotStartedProgress
     * @see eu.steffo.cleaver.logic.progress.WorkingProgress
     * @see eu.steffo.cleaver.logic.progress.FinishedProgress
     * @see eu.steffo.cleaver.logic.progress.ErrorProgress
     */
    public Progress getProgress() {
        return progress;
    }

    /**
     * @return The {@link SplitConfig} of the job. If {@literal null}, the job shouldn't handle file splitting/merging.
     */
    public abstract SplitConfig getSplitConfig();

    /**
     * @return The {@link CryptConfig} of the job. If {@literal null}, the job shouldn't handle file encryption/decryption.
     */
    public abstract CryptConfig getCryptConfig();

    /**
     * @return The {@link CompressConfig} of the job. If {@literal null}, the job shouldn't handle file compression/decompression.
     */
    public abstract CompressConfig getCompressConfig();

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
     * The function that is run on a different thread when {@link Thread#start()} is called.
     *
     * Child classes should override {@link Thread#run()}.
     */
    @Override
    public abstract void run();
}
