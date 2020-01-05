package eu.steffo.cleaver.gui.panels;

import javax.swing.*;
import java.awt.event.ActionListener;

/**
 * A panel that holds the buttons to perform actions on jobs.
 * Currently, the actions are <b>Delete selected jobs</b> and <b>Start incomplete jobs</b>.
 * The buttons are displayed <i>horizontally</i> side-by-side.
 */
public class JobsButtonsPanel extends JPanel {

    /**
     * The <i>Delete selected jobs</i> button.
     */
    protected final JButton deleteButton;

    /**
     * The <i>Start incomplete jobs</i> button.
     */
    protected final JButton startButton;

    /**
     * Construct a JobsButtonPanel.
     * @param onDeleteJobsButtonClick The {@link ActionListener} that should be added to the <i>Delete</i> {@link JButton button}.
     * @param onStartJobsButtonClick The {@link ActionListener} that should be added to the <i>Start</i> {@link JButton button}.
     */
    public JobsButtonsPanel(ActionListener onDeleteJobsButtonClick, ActionListener onStartJobsButtonClick) {
        super();

        this.add(Box.createHorizontalStrut(8));

        deleteButton = new JButton("Delete selected job");
        deleteButton.addActionListener(onDeleteJobsButtonClick);
        this.add(deleteButton);

        this.add(Box.createHorizontalStrut(8));

        startButton = new JButton("Start incomplete jobs");
        startButton.addActionListener(onStartJobsButtonClick);
        this.add(startButton);

        this.add(Box.createHorizontalStrut(8));
    }
}
