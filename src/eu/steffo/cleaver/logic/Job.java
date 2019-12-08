package eu.steffo.cleaver.logic;

import java.io.File;

import eu.steffo.cleaver.logic.compress.CompressConfig;
import eu.steffo.cleaver.logic.crypt.CryptConfig;
import eu.steffo.cleaver.logic.progress.NotStartedProgress;
import eu.steffo.cleaver.logic.progress.Progress;
import eu.steffo.cleaver.logic.split.SplitConfig;

public abstract class Job extends Thread {
    protected final File file;
    protected final SplitConfig splitConfig;
    protected final CryptConfig cryptConfig;
    protected final CompressConfig compressConfig;
    protected Progress progress;

    public Job(File file, SplitConfig splitConfig, CryptConfig cryptConfig, CompressConfig compressConfig) {
        this.file = file;
        this.splitConfig = splitConfig;
        this.cryptConfig = cryptConfig;
        this.compressConfig = compressConfig;
        this.progress = new NotStartedProgress();
    }

    public abstract String getType();

    public File getFile() {
        return file;
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

    public Progress getProgress() {
        return progress;
    }
}
