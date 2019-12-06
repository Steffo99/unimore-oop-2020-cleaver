package eu.steffo.cleaver.gui;

import javax.swing.*;

public abstract class CreateJobPanel extends JPanel {
    protected final TitleRow titlePanel;
    protected final FileSelectRow fileSelectPanel;
    protected final SplitRow splitOptionPanel;
    protected final CryptRow cryptOptionPanel;
    protected final CompressRow compressOptionPanel;
    protected final CreateJobButtonRow createJobButtonPanel;

    protected abstract String getPanelText();

    public CreateJobPanel() {
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

        createJobButtonPanel = new CreateJobButtonRow();
        this.add(createJobButtonPanel);

        this.add(Box.createVerticalStrut(8));
    }
}
