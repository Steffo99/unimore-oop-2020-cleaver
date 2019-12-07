package eu.steffo.cleaver.logic;

import java.io.File;

import eu.steffo.cleaver.logic.compress.CompressConfig;
import eu.steffo.cleaver.logic.crypt.CryptConfig;
import eu.steffo.cleaver.logic.split.SplitConfig;

public abstract class Job {
    protected File file;
    protected SplitConfig splitConfig;
    protected CryptConfig cryptConfig;
    protected CompressConfig compressConfig;

    public Job(File file, SplitConfig splitConfig, CryptConfig cryptConfig, CompressConfig compressConfig) {
        this.file = file;
        this.splitConfig = splitConfig;
        this.cryptConfig = cryptConfig;
        this.compressConfig = compressConfig;
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
}
