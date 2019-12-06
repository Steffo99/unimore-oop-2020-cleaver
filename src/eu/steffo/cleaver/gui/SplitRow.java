package eu.steffo.cleaver.gui;

import javax.swing.*;

public class SplitRow extends Row {
    protected final JCheckBox compressionCheckBox;
    protected final JSeparator firstSeparator;
    protected final JLabel sizeLabel;
    protected final JTextField sizeTextField;
    protected final JLabel sizeUnitLabel;
    protected final JSeparator secondoSeparator;
    protected final JLabel partsLabel;
    protected final JTextField partsTextField;

    public SplitRow() {
        super();

        this.add(Box.createHorizontalStrut(8));

        compressionCheckBox = new JCheckBox("Split");
        this.add(compressionCheckBox);

        this.add(Box.createHorizontalStrut(8));

        firstSeparator = new JSeparator(JSeparator.VERTICAL);
        this.add(firstSeparator);

        this.add(Box.createHorizontalStrut(8));

        sizeLabel = new JLabel("Part size");
        this.add(sizeLabel);

        this.add(Box.createHorizontalStrut(8));

        sizeTextField = new JTextField();
        this.add(sizeTextField);

        this.add(Box.createHorizontalStrut(2));

        sizeUnitLabel = new JLabel("B");
        this.add(sizeUnitLabel);

        this.add(Box.createHorizontalStrut(8));

        secondoSeparator = new JSeparator(JSeparator.VERTICAL);
        this.add(secondoSeparator);

        this.add(Box.createHorizontalStrut(8));

        partsLabel = new JLabel("Parts");
        this.add(partsLabel);

        this.add(Box.createHorizontalStrut(8));

        partsTextField = new JTextField();
        this.add(partsTextField);

        this.add(Box.createHorizontalStrut(8));
    }
}
