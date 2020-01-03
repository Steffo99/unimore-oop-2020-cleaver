package eu.steffo.cleaver.gui;

import eu.steffo.cleaver.gui.panels.ChopAndStitchPanel;
import eu.steffo.cleaver.gui.panels.JobsButtonsPanel;
import eu.steffo.cleaver.gui.panels.JobsTablePanel;
import eu.steffo.cleaver.logic.Job;
import eu.steffo.cleaver.logic.progress.NotStartedProgress;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import javax.swing.*;

/**
 * A class providing a GUI for Cleaver made with {@link javax.swing}.
 * It uses a <b>vertical</b> {@link BoxLayout} ({@link BoxLayout#PAGE_AXIS}): the panels added to it are stacked vertically, as if it was a list.
 */
public class CleaverFrame extends JFrame {
    /**
     * The panel allowing the creation of new {@link Job Jobs}.
     *
     * @see ChopAndStitchPanel
     */
    protected final ChopAndStitchPanel chopStitchPanel;

    /**
     * The panel containing the {@link Job Jobs} {@link JTable table}.
     *
     * @see JobsTablePanel
     */
    protected final JobsTablePanel jobsTablePanel;

    /**
     * The panel containing jobs-related buttons, such as <i>Delete selected</i> or <i>Run all</i>.
     *
     * @see JobsButtonsPanel
     */
    protected final JobsButtonsPanel jobsButtonRow;

    /**
     * A reference to the {@link ArrayList} where the {@link Job Jobs} should be contained.
     *
     * @see CleaverFrame#CleaverFrame(ArrayList)
     */
    protected final ArrayList<Job> jobs;

    /**
     * Construct the CleaverFrame by setting its fields and adding to it the panels it should contain.
     *
     * The frame is passed a reference to an {@link ArrayList} of {@link Job Jobs} in order to be able to display them in the
     * {@link eu.steffo.cleaver.gui.panels.JobsTablePanel JobsTablePanel} and to add/remove {@link Job Jobs} from it.
     *
     * It also creates {@link ActionListener ActionListeners} for the events that require access to the {@link #jobs jobs list}.
     *
     * @param jobs The reference to the previously mentioned {@link ArrayList}.
     */
    public CleaverFrame(ArrayList<Job> jobs) {
        super();

        this.jobs = jobs;

        this.setTitle("Cleaver File Splitter");

        Container cp = getContentPane();

        cp.setLayout(new BoxLayout(cp, BoxLayout.PAGE_AXIS));

        this.add(Box.createVerticalStrut(4));

        ActionListener chopListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chopStitchPanel.createAndAddChopJobs(jobs, jobsTablePanel::updateTableChanged);
            }
        };

        ActionListener stitchListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chopStitchPanel.createAndAddStitchJobs(jobs, jobsTablePanel::updateTableChanged);
            }
        };

        chopStitchPanel = new ChopAndStitchPanel(chopListener, stitchListener);
        cp.add(chopStitchPanel);

        this.add(Box.createVerticalStrut(4));

        jobsTablePanel = new JobsTablePanel(jobs);
        cp.add(jobsTablePanel);

        this.add(Box.createVerticalStrut(4));

        ActionListener deleteListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] removedIndexes = jobsTablePanel.getSelectedJobsIndexes();
                Arrays.sort(removedIndexes);
                for(int n = removedIndexes.length - 1; n >= 0; n--) {
                    jobs.remove(removedIndexes[n]);
                }
                jobsTablePanel.updateTableChanged();
            }
        };

        ActionListener startListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for(Job job : jobs) {
                    if(job.getProgress() instanceof NotStartedProgress)
                    {
                        job.start();
                    }
                }
            }
        };

        jobsButtonRow = new JobsButtonsPanel(deleteListener, startListener);
        this.add(jobsButtonRow);

        this.add(Box.createVerticalStrut(4));

        this.pack();
    }
}
