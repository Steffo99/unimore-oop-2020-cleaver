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
        super(file, splitConfig, cryptConfig, compressConfig);
    }

    @Override
    public String getType() {
        return "Chop";
    }

    @Override
    public void run() {
        FileInputStream inputStream;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }

        // TODO: use DeflaterOutputStream to compress
        // TODO: create a CipherOutputStream to encrypt
        // TODO: create a SplitFileOutputStream to output to multiple files, or use a simple FileOutputStream to output to a single file

        // TODO: end with inputStream.transferTo(outputStream);
    }
}
