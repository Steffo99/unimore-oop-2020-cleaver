package eu.steffo.cleaver.logic;

import eu.steffo.cleaver.logic.compress.CompressConfig;
import eu.steffo.cleaver.logic.crypt.CryptConfig;
import eu.steffo.cleaver.logic.split.SplitConfig;

import java.io.File;

public class ChopJob extends Job {

    public ChopJob(File file, SplitConfig splitConfig, CryptConfig cryptConfig, CompressConfig compressConfig) {
        super(file);
        this.splitConfig = splitConfig;
        this.cryptConfig = cryptConfig;
        this.compressConfig = compressConfig;
    }

    public ChopJob(File file, Runnable swingCallLaterOnProgressChanges, SplitConfig splitConfig, CryptConfig cryptConfig, CompressConfig compressConfig) {
        super(file, swingCallLaterOnProgressChanges);
        this.splitConfig = splitConfig;
        this.cryptConfig = cryptConfig;
        this.compressConfig = compressConfig;
    }

    @Override
    public String getType() {
        return "Chop";
    }

    @Override
    public void run() {
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
