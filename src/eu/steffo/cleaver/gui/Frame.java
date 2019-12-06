package eu.steffo.cleaver.gui;

import eu.steffo.cleaver.logic.Job;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

public class Frame extends JFrame {
    protected final ChopAndStitchPanel chopStitchPanel;
    protected final JobsTablePanel jobsTablePanel;
    protected final JobsButtons jobsButtonRow;

    protected final ArrayList<Job> jobs;

    public Frame() {
        super();

        // TODO: change this
        jobs = new ArrayList<Job>(0);
        jobs.add(new Job(null, null, null, null));

        this.setTitle("Cleaver File Splitter");

        Container cp = getContentPane();

        cp.setLayout(new BoxLayout(cp, BoxLayout.PAGE_AXIS));

        this.add(Box.createVerticalStrut(4));

        chopStitchPanel = new ChopAndStitchPanel();
        cp.add(chopStitchPanel);

        this.add(Box.createVerticalStrut(4));

        jobsTablePanel = new JobsTablePanel(jobs);
        cp.add(jobsTablePanel);

        this.add(Box.createVerticalStrut(4));

        jobsButtonRow = new JobsButtons();
        this.add(jobsButtonRow);

        this.add(Box.createVerticalStrut(4));

        this.pack();
    }
}
