package eu.steffo.cleaver.gui;

import java.awt.*;
import javax.swing.*;

public class CleaverFrame extends JFrame {
    protected CleaverChopAndStitchPanel chopStitchPanel;

    public CleaverFrame() {
        super();
        this.setTitle("Cleaver File Splitter");

        Container cp = getContentPane();

        cp.setLayout(new BoxLayout(cp, BoxLayout.PAGE_AXIS));

        this.add(Box.createVerticalStrut(4));

        chopStitchPanel = new CleaverChopAndStitchPanel();
        cp.add(chopStitchPanel);

        this.add(Box.createVerticalStrut(4));

        this.pack();
    }
}
