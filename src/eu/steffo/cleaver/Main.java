package eu.steffo.cleaver;

import javax.swing.*;
import eu.steffo.cleaver.gui.CleaverFrame;
import eu.steffo.cleaver.logic.job.Job;

import java.util.ArrayList;

/**
 * The class containing the main function.
 */
public class Main {
    /**
     * The {@link ArrayList} of {@link Job Jobs} that the {@link CleaverFrame} will manipulate.
     *
     * @see #main(String[])
     */
    protected static ArrayList<Job> jobs;

    /**
     * The main function of the program.
     * It opens a new {@link CleaverFrame} that the user will be able to interact with, and will exit the program when the frame is closed.
     *
     * @param args The arguments passed to the program from the command line.
     */
    public static void main(String[] args) {
        jobs = new ArrayList<>();

        CleaverFrame cf = new CleaverFrame(jobs);
        cf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        cf.setVisible(true);
    }
}
