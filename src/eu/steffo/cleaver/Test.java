package eu.steffo.cleaver;

import eu.steffo.cleaver.logic.stream.output.CleaverSplitFileOutputStream;

import java.io.IOException;
import java.io.OutputStream;

//TODO: delet this
public class Test {
    public static void main(String[] args) {
        OutputStream stream = new CleaverSplitFileOutputStream("test", 64);
        for(int i = 0; i < 256; i++) {
            try {
                stream.write(i);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
