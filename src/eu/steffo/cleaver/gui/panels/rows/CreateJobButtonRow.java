package eu.steffo.cleaver.gui.panels.rows;

import javax.swing.*;
import java.awt.event.ActionListener;

/**
 * A {@link Row} with a clickable {@link JButton}.
 *
 * The button click event can be handled with a custom {@link ActionListener}, specified in the constructor.
 *
 * @see eu.steffo.cleaver.gui.panels.CreateJobPanel
 */
public class CreateJobButtonRow extends Row {
    protected final JButton createJobButton;

    /**
     * Construct a CreateJobButtonRow.
     * @param onClick The {@link ActionListener} catching the button click event.
     */
    public CreateJobButtonRow(ActionListener onClick) {
        super();

        this.add(Box.createHorizontalStrut(8));

        createJobButton = new JButton("Create job");
        createJobButton.addActionListener(onClick);
        this.add(createJobButton);

        this.add(Box.createHorizontalStrut(8));
    }
}
