package eu.steffo.cleaver;

import javax.swing.*;
import eu.steffo.cleaver.gui.CleaverFrame;

public class Main {

    public static void main(String[] args) {
        System.out.println("Chop chop!");

        CleaverFrame cf = new CleaverFrame();
        cf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        cf.setVisible(true);
    }
}
