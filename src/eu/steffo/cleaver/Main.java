package eu.steffo.cleaver;

import javax.swing.*;
import eu.steffo.cleaver.gui.CleaverFrame;
import eu.steffo.cleaver.logic.Job;

import java.util.ArrayList;

public class Main {
    protected static ArrayList<Job> jobs;

    public static void main(String[] args) {
        jobs = new ArrayList<>();

        CleaverFrame cf = new CleaverFrame(jobs);
        cf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        cf.setVisible(true);
    }
}
