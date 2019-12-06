package eu.steffo.cleaver.gui;

import javax.swing.*;

public class ChopAndStitchPanel extends JPanel {
    protected final ChopPanel chopPanel;
    protected final CleaverStitchPanel stitchPanel;

    public ChopAndStitchPanel() {
        super();

        this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

        this.add(Box.createHorizontalStrut(4));

        chopPanel = new ChopPanel();
        this.add(chopPanel);

        this.add(Box.createHorizontalStrut(4));

        stitchPanel = new CleaverStitchPanel();
        this.add(stitchPanel);

        this.add(Box.createHorizontalStrut(4));
    }
}
