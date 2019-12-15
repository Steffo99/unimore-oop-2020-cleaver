package eu.steffo.cleaver.logic;

import java.io.File;

import eu.steffo.cleaver.logic.compress.CompressConfig;
import eu.steffo.cleaver.logic.crypt.CryptConfig;
import eu.steffo.cleaver.logic.progress.NotStartedProgress;
import eu.steffo.cleaver.logic.progress.Progress;
import eu.steffo.cleaver.logic.split.SplitConfig;

public abstract class Job extends Thread {
    protected final File file;
    protected Progress progress;

    public Job(File file) {
        this.file = file;
        this.progress = new NotStartedProgress();
    }

    public abstract String getType();

    public File getFile() {
        return file;
    }

    public Progress getProgress() {
        return progress;
    }
}
