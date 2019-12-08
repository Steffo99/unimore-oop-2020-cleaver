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

public class CleaverFrame extends JFrame {
    protected final ChopAndStitchPanel chopStitchPanel;
    protected final JobsTablePanel jobsTablePanel;
    protected final JobsButtonsPanel jobsButtonRow;

    protected final ArrayList<Job> jobs;

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
                chopStitchPanel.createAndAddChopJobs(jobs);
                jobsTablePanel.updateTableChanged();
            }
        };

        ActionListener stitchListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chopStitchPanel.createAndAddStitchJobs(jobs);
                jobsTablePanel.updateTableChanged();
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
                    if(job.getProgress().getClass() == NotStartedProgress.class)
                    {
                        job.start();
                    }
                    // TODO: refresh the jobs table every once in a while
                    // TODO: catch exceptions from the jobs
                }
            }
        };

        jobsButtonRow = new JobsButtonsPanel(deleteListener, startListener);
        this.add(jobsButtonRow);

        this.add(Box.createVerticalStrut(4));

        this.pack();
    }
}
