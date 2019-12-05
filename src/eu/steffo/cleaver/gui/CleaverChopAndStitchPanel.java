package eu.steffo.cleaver.gui;

import java.awt.*;
import javax.swing.*;

public class CleaverChopAndStitchPanel extends JPanel {
    protected CleaverChopPanel chopPanel;
    protected CleaverStitchPanel stitchPanel;

    public CleaverChopAndStitchPanel() {
        super();

        this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

        this.add(Box.createHorizontalStrut(4));

        chopPanel = new CleaverChopPanel();
        this.add(chopPanel);

        this.add(Box.createHorizontalStrut(4));

        stitchPanel = new CleaverStitchPanel();
        this.add(stitchPanel);

        this.add(Box.createHorizontalStrut(4));
    }
}
