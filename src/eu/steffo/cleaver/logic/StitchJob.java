package eu.steffo.cleaver.logic;

import eu.steffo.cleaver.logic.compress.CompressConfig;
import eu.steffo.cleaver.logic.crypt.CryptConfig;
import eu.steffo.cleaver.logic.split.SplitConfig;

import java.io.File;

public class StitchJob extends Job {

    public StitchJob(File file, SplitConfig splitConfig, CryptConfig cryptConfig, CompressConfig compressConfig) {
        super(file, splitConfig, cryptConfig, compressConfig);
    }

    @Override
    public String getType() {
        return "Stitch";
    }

    @Override
    public void run() {

    }
}
