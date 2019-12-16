package eu.steffo.cleaver.gui.panels;

import eu.steffo.cleaver.logic.Job;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ChopAndStitchPanel extends JPanel {
    protected final ChopPanel chopPanel;
    protected final StitchPanel stitchPanel;

    public ChopAndStitchPanel(ActionListener onCreateChopJobClick, ActionListener onCreateStitchJobClick) {
        super();

        this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

        this.add(Box.createHorizontalStrut(4));

        chopPanel = new ChopPanel(onCreateChopJobClick);
        this.add(chopPanel);

        this.add(Box.createHorizontalStrut(4));

        stitchPanel = new StitchPanel(onCreateStitchJobClick);
        this.add(stitchPanel);

        this.add(Box.createHorizontalStrut(4));
    }

    public void createAndAddChopJobs(ArrayList<Job> jobs, Runnable updateTable) {
        chopPanel.createAndAddJobs(jobs, updateTable);
    }

    public void createAndAddStitchJobs(ArrayList<Job> jobs, Runnable updateTable) {
        stitchPanel.createAndAddJobs(jobs, updateTable);
    }
}
