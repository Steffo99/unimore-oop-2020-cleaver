package eu.steffo.cleaver.gui.rows;

import eu.steffo.cleaver.gui.rows.Row;

import javax.swing.*;
import java.awt.event.ActionListener;

public class CreateJobButtonRow extends Row {
    protected final JButton createJobButton;

    public CreateJobButtonRow(ActionListener onClick) {
        super();

        this.add(Box.createHorizontalStrut(8));

        createJobButton = new JButton("Create job");
        createJobButton.addActionListener(onClick);
        this.add(createJobButton);

        this.add(Box.createHorizontalStrut(8));
    }
}
