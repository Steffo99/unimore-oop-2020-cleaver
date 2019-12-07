package eu.steffo.cleaver.gui.panels;

import javax.swing.*;
import java.awt.event.ActionListener;

public class JobsButtonsPanel extends JPanel {

    protected final JButton deleteButton;
    protected final JButton startButton;

    public JobsButtonsPanel(ActionListener onDeleteJobsButtonClick, ActionListener onStartJobsButtonClick) {
        super();

        this.add(Box.createHorizontalStrut(8));

        deleteButton = new JButton("Delete selected job");
        deleteButton.addActionListener(onDeleteJobsButtonClick);
        this.add(deleteButton);

        this.add(Box.createHorizontalStrut(8));

        startButton = new JButton("Start jobs");
        startButton.addActionListener(onStartJobsButtonClick);
        this.add(startButton);

        this.add(Box.createHorizontalStrut(8));
    }
}
