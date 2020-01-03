package eu.steffo.cleaver;

import eu.steffo.cleaver.logic.ChopJob;
import eu.steffo.cleaver.logic.Job;
import eu.steffo.cleaver.logic.split.SplitFileOutputStream;

import java.io.IOException;
import java.io.OutputStream;

//TODO: delet this
public class Test {
    public static void main(String[] args) {
        OutputStream stream = new SplitFileOutputStream("test", 64);
        for(int i = 0; i < 256; i++) {
            try {
                stream.write(i);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
