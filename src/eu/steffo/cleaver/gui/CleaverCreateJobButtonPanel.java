package eu.steffo.cleaver.gui;

import javax.swing.*;

public class CleaverCreateJobButtonPanel extends CleaverRowPanel {
    protected JButton createJobButton;

    public CleaverCreateJobButtonPanel() {
        this.add(Box.createHorizontalStrut(8));

        createJobButton = new JButton("Create job");
        this.add(createJobButton);

        this.add(Box.createHorizontalStrut(8));
    }
}
