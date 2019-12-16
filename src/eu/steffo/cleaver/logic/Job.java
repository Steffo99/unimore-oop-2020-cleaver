package eu.steffo.cleaver.logic;

import java.io.File;
import javax.swing.SwingUtilities;

import eu.steffo.cleaver.logic.compress.CompressConfig;
import eu.steffo.cleaver.logic.crypt.CryptConfig;
import eu.steffo.cleaver.logic.progress.NotStartedProgress;
import eu.steffo.cleaver.logic.progress.Progress;
import eu.steffo.cleaver.logic.split.SplitConfig;

public abstract class Job extends Thread {
    protected File file;

    private Progress progress;
    protected Runnable swingCallLaterOnProgressChanges = null;

    protected SplitConfig splitConfig = null;
    protected CryptConfig cryptConfig = null;
    protected CompressConfig compressConfig = null;

    public Job(File file) {
        this.file = file;
        this.progress = new NotStartedProgress();
    }

    public Job(File file, Runnable swingCallLaterOnProgressChanges) {
        this(file);
        this.swingCallLaterOnProgressChanges = swingCallLaterOnProgressChanges;
        if(swingCallLaterOnProgressChanges != null) {
            SwingUtilities.invokeLater(swingCallLaterOnProgressChanges);
        }
    }

    public abstract String getType();

    public File getFile() {
        return file;
    }

    public Progress getProgress() {
        return progress;
    }

    protected void setProgress(Progress progress) {
        this.progress = progress;
        if(swingCallLaterOnProgressChanges != null) {
            SwingUtilities.invokeLater(swingCallLaterOnProgressChanges);
        }
    }

    public SplitConfig getSplitConfig() {
        return splitConfig;
    }

    public CryptConfig getCryptConfig() {
        return cryptConfig;
    }

    public CompressConfig getCompressConfig() {
        return compressConfig;
    }
}
