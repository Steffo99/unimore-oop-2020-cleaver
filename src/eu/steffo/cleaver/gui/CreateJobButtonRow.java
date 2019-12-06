package eu.steffo.cleaver.gui;

import javax.swing.*;

public class CreateJobButtonRow extends Row {
    protected final JButton createJobButton;

    public CreateJobButtonRow() {
        super();

        this.add(Box.createHorizontalStrut(8));

        createJobButton = new JButton("Create job");
        this.add(createJobButton);

        this.add(Box.createHorizontalStrut(8));
    }
}
