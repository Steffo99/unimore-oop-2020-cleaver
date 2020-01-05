package eu.steffo.cleaver.gui.panels;

import eu.steffo.cleaver.logic.Job;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * This panel displays a {@link ChopPanel} and a {@link StitchPanel} <i>horizontally</i> side-by-side.
 */
public class ChopAndStitchPanel extends JPanel {
    /**
     * The {@link ChopPanel}, displayed on the left.
     */
    protected final ChopPanel chopPanel;

    /**
     * The {@link StitchPanel}, displayed on the right.
     */
    protected final StitchPanel stitchPanel;

    /**
     * Construct a ChopAndStitchPanel by instantiating and adding a {@link ChopPanel} and a {@link StitchPanel} to it.
     * @param onCreateChopJobClick The {@link ActionListener} that will be bound to the {@link eu.steffo.cleaver.gui.rows.CreateJobButtonRow CreateJobButton} of the {@link ChopPanel}.
     * @param onCreateStitchJobClick The {@link ActionListener} that will be bound to the {@link eu.steffo.cleaver.gui.rows.CreateJobButtonRow CreateJobButton} of the {@link StitchPanel}.
     */
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

    /**
     * Propagate downwards the click of the <i>Create Jobs</i> button on the {@link #chopPanel}.
     * @param jobs The {@link ArrayList} of jobs that should be manipulated.
     * @param onProgressChange The function that should be invoked when the {@link Job} {@link eu.steffo.cleaver.logic.progress.Progress Progress} changes.
     * @see ChopPanel#createAndAddJobs(ArrayList, Runnable)
     * @see eu.steffo.cleaver.logic.ChopJob
     * @see eu.steffo.cleaver.gui.CleaverFrame
     */
    public void createAndAddChopJobs(ArrayList<Job> jobs, Runnable onProgressChange) {
        chopPanel.createAndAddJobs(jobs, onProgressChange);
    }

    /**
     * Propagate downwards the click of the <i>Create Jobs</i> button on the {@link #stitchPanel}.
     * @param jobs The {@link ArrayList} of jobs that should be manipulated.
     * @param onProgressChange The function that should be invoked when the {@link Job} {@link eu.steffo.cleaver.logic.progress.Progress Progress} changes.
     * @see StitchPanel#createAndAddJobs(ArrayList, Runnable)
     * @see eu.steffo.cleaver.logic.ChopJob
     * @see eu.steffo.cleaver.gui.CleaverFrame
     */
    public void createAndAddStitchJobs(ArrayList<Job> jobs, Runnable onProgressChange) {
        stitchPanel.createAndAddJobs(jobs, onProgressChange);
    }
}
