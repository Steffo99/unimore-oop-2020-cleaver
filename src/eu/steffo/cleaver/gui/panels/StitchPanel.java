package eu.steffo.cleaver.gui.panels;

import eu.steffo.cleaver.logic.Job;
import eu.steffo.cleaver.logic.StitchJob;

import java.awt.event.ActionListener;

public class StitchPanel extends CreateJobPanel {

    @Override
    protected String getPanelText() {
        return "Stitch";
    }

    @Override
    protected Class<? extends Job> getJobClass() {
        return StitchJob.class;
    }

    public StitchPanel(ActionListener onCreateJobClick) {
        super(onCreateJobClick);
        this.splitOptionPanel.setEditable(false);
        this.cryptOptionPanel.setEditable(false);
        this.compressOptionPanel.setEditable(false);
    }
}
