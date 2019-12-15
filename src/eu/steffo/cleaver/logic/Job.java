package eu.steffo.cleaver.logic;

import java.io.File;

import eu.steffo.cleaver.logic.compress.CompressConfig;
import eu.steffo.cleaver.logic.crypt.CryptConfig;
import eu.steffo.cleaver.logic.progress.NotStartedProgress;
import eu.steffo.cleaver.logic.progress.Progress;
import eu.steffo.cleaver.logic.split.SplitConfig;

public abstract class Job extends Thread {
    protected File file;
    protected SplitConfig splitConfig;
    protected CryptConfig cryptConfig;
    protected CompressConfig compressConfig;
    protected Progress progress;

    public Job(File file) {
        this.file = file;
        this.progress = new NotStartedProgress();
        splitConfig = null;
        cryptConfig = null;
        compressConfig = null;
    }

    public abstract String getType();

    public File getFile() {
        return file;
    }

    public Progress getProgress() {
        return progress;
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
