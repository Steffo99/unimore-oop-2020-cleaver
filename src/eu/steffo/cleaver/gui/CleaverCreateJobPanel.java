package eu.steffo.cleaver.gui;

import javax.swing.*;

public abstract class CleaverCreateJobPanel extends JPanel {
    protected CleaverTitlePanel titlePanel;
    protected CleaverFileSelectPanel fileSelectPanel;
    protected CleaverSplitOptionPanel splitOptionPanel;
    protected CleaverCryptOptionPanel cryptOptionPanel;
    protected CleaverCompressOptionPanel compressOptionPanel;
    protected CleaverCreateJobButtonPanel createJobButtonPanel;

    protected abstract String getPanelText();

    public CleaverCreateJobPanel() {
        super();

        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.setBorder(BorderFactory.createEtchedBorder());

        this.add(Box.createVerticalStrut(8));

        titlePanel = new CleaverTitlePanel(this.getPanelText());
        this.add(titlePanel);

        this.add(Box.createVerticalStrut(8));

        fileSelectPanel = new CleaverFileSelectPanel();
        this.add(fileSelectPanel);

        this.add(Box.createVerticalStrut(8));

        splitOptionPanel = new CleaverSplitOptionPanel();
        this.add(splitOptionPanel);

        this.add(Box.createVerticalStrut(8));

        cryptOptionPanel = new CleaverCryptOptionPanel();
        this.add(cryptOptionPanel);

        this.add(Box.createVerticalStrut(8));

        compressOptionPanel = new CleaverCompressOptionPanel();
        this.add(compressOptionPanel);

        this.add(Box.createVerticalStrut(8));

        createJobButtonPanel = new CleaverCreateJobButtonPanel();
        this.add(createJobButtonPanel);

        this.add(Box.createVerticalStrut(8));
    }
}
