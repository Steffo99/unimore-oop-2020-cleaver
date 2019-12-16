package eu.steffo.cleaver.gui.panels;

import eu.steffo.cleaver.gui.rows.CreateJobButtonRow;
import eu.steffo.cleaver.gui.rows.option.CompressRow;
import eu.steffo.cleaver.gui.rows.option.CryptRow;
import eu.steffo.cleaver.gui.rows.option.SplitRow;
import eu.steffo.cleaver.logic.ChopJob;
import eu.steffo.cleaver.logic.Job;
import eu.steffo.cleaver.logic.compress.CompressConfig;
import eu.steffo.cleaver.logic.crypt.CryptConfig;
import eu.steffo.cleaver.logic.split.SplitConfig;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class ChopPanel extends CreateJobPanel {
    protected final SplitRow splitOptionPanel;
    protected final CryptRow cryptOptionPanel;
    protected final CompressRow compressOptionPanel;
    protected final CreateJobButtonRow createJobButtonPanel;

    @Override
    protected String getPanelText() {
        return "Chop";
    }

    public ChopPanel(ActionListener onCreateJobClick) {
        super(onCreateJobClick);

        this.add(Box.createVerticalStrut(8));

        splitOptionPanel = new SplitRow();
        this.add(splitOptionPanel);

        this.add(Box.createVerticalStrut(8));

        cryptOptionPanel = new CryptRow();
        this.add(cryptOptionPanel);

        this.add(Box.createVerticalStrut(8));

        compressOptionPanel = new CompressRow();
        this.add(compressOptionPanel);

        this.add(Box.createVerticalStrut(8));

        createJobButtonPanel = new CreateJobButtonRow(onCreateJobClick);
        this.add(createJobButtonPanel);

        this.add(Box.createVerticalStrut(8));
    }

    public void createAndAddJobs(ArrayList<Job> jobs, Runnable updateTable) {
        if(fileSelectPanel.getSelectedFiles().length == 0) {
            JOptionPane.showMessageDialog(null, "No files selected.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        for(File file : fileSelectPanel.getSelectedFiles()) {

            SplitConfig sc;
            try {
                sc = splitOptionPanel.getSplitConfig();
            } catch (NumberFormatException exc) {
                JOptionPane.showMessageDialog(null, "Invalid value in the Split fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            CryptConfig cc = cryptOptionPanel.getCryptConfig();

            CompressConfig zc = compressOptionPanel.getCompressConfig();

            Job job = new ChopJob(file, updateTable, sc, cc, zc);
            jobs.add(job);

        }
        fileSelectPanel.clearSelectedFiles();
    }
}
