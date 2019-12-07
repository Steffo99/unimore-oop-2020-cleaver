package eu.steffo.cleaver.gui.panels;

import eu.steffo.cleaver.gui.rows.CreateJobButtonRow;
import eu.steffo.cleaver.gui.rows.FileSelectRow;
import eu.steffo.cleaver.gui.rows.TitleRow;
import eu.steffo.cleaver.gui.rows.option.*;
import eu.steffo.cleaver.logic.Job;
import eu.steffo.cleaver.logic.compress.CompressConfig;
import eu.steffo.cleaver.logic.crypt.CryptConfig;
import eu.steffo.cleaver.logic.split.SplitConfig;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public abstract class CreateJobPanel extends JPanel {
    protected final TitleRow titlePanel;
    protected final FileSelectRow fileSelectPanel;
    protected final SplitRow splitOptionPanel;
    protected final CryptRow cryptOptionPanel;
    protected final CompressRow compressOptionPanel;
    protected final CreateJobButtonRow createJobButtonPanel;

    protected abstract String getPanelText();

    protected abstract Class<? extends Job> getJobClass();

    public CreateJobPanel(ActionListener onCreateJobClick) {
        super();

        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.setBorder(BorderFactory.createEtchedBorder());

        this.add(Box.createVerticalStrut(8));

        titlePanel = new TitleRow(this.getPanelText());
        this.add(titlePanel);

        this.add(Box.createVerticalStrut(8));

        fileSelectPanel = new FileSelectRow();
        this.add(fileSelectPanel);

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

    public void createAndAddJobs(ArrayList<Job> jobs) {
        for(File file : fileSelectPanel.getSelectedFiles()) {
            try {
                Job job = getJobClass().getConstructor(File.class, SplitConfig.class, CryptConfig.class, CompressConfig.class).newInstance(file, splitOptionPanel.getSplitConfig(), cryptOptionPanel.getCryptConfig(), compressOptionPanel.getCompressConfig());
                jobs.add(job);
            } catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException ex) {
                // TODO: open an error jframe instead
                ex.printStackTrace();
            }
        }
        fileSelectPanel.clearSelectedFiles();
    }
}
