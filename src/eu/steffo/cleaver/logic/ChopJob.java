package eu.steffo.cleaver.logic;

import eu.steffo.cleaver.logic.compress.CompressConfig;
import eu.steffo.cleaver.logic.crypt.CryptConfig;
import eu.steffo.cleaver.logic.split.SplitConfig;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.util.zip.DeflaterOutputStream;

public class ChopJob extends Job {

    public ChopJob(File file, SplitConfig splitConfig, CryptConfig cryptConfig, CompressConfig compressConfig) {
        super(file);
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
        System.out.println("CHOP");
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
