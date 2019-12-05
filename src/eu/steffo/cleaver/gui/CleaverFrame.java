package eu.steffo.cleaver.gui;

import java.awt.*;
import javax.swing.*;

public class CleaverFrame extends JFrame {
    protected CleaverFileSelectPanel fileSelectPanel;
    protected CleaverModeSelectPanel modeSelectPanel;

    public CleaverFrame() {
        super();
        this.setTitle("Cleaver File Splitter");

        Container cp = getContentPane();

        cp.setLayout(new BoxLayout(cp, BoxLayout.PAGE_AXIS));

        fileSelectPanel = new CleaverFileSelectPanel();
        cp.add(fileSelectPanel);

        modeSelectPanel = new CleaverModeSelectPanel();
        cp.add(modeSelectPanel);

        this.pack();
    }
}
