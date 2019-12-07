package eu.steffo.cleaver.gui.rows.option;

import eu.steffo.cleaver.gui.rows.option.OptionRow;
import eu.steffo.cleaver.logic.split.SplitByPartsConfig;
import eu.steffo.cleaver.logic.split.SplitBySizeConfig;
import eu.steffo.cleaver.logic.split.SplitConfig;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class SplitRow extends OptionRow {
    protected final JCheckBox splitCheckBox;
    protected final JSeparator firstSeparator;
    protected final JLabel sizeLabel;
    protected final JTextField sizeTextField;
    protected final JLabel sizeUnitLabel;
    protected final JSeparator secondSeparator;
    protected final JLabel partsLabel;
    protected final JTextField partsTextField;

    public SplitRow() {
        super();

        this.add(Box.createHorizontalStrut(8));

        splitCheckBox = new JCheckBox("Split");
        splitCheckBox.addActionListener(e -> updateEnabledState());
        this.add(splitCheckBox);

        this.add(Box.createHorizontalStrut(8));

        firstSeparator = new JSeparator(JSeparator.VERTICAL);
        this.add(firstSeparator);

        this.add(Box.createHorizontalStrut(8));

        sizeLabel = new JLabel("Part size");
        this.add(sizeLabel);

        this.add(Box.createHorizontalStrut(8));

        sizeTextField = new JTextField();
        sizeTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateEnabledState();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateEnabledState();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateEnabledState();
            }
        });
        this.add(sizeTextField);

        this.add(Box.createHorizontalStrut(2));

        sizeUnitLabel = new JLabel("B");
        this.add(sizeUnitLabel);

        this.add(Box.createHorizontalStrut(8));

        secondSeparator = new JSeparator(JSeparator.VERTICAL);
        this.add(secondSeparator);

        this.add(Box.createHorizontalStrut(8));

        partsLabel = new JLabel("Parts");
        this.add(partsLabel);

        this.add(Box.createHorizontalStrut(8));

        partsTextField = new JTextField();
        partsTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateEnabledState();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateEnabledState();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateEnabledState();
            }
        });
        this.add(partsTextField);

        this.add(Box.createHorizontalStrut(8));

        this.updateEnabledState();
    }

    public SplitConfig getSplitConfig() {
        if(!splitCheckBox.isSelected()) {
            return null;
        }
        //TODO: catch exception here and display an error
        else if(!sizeTextField.getText().equals("")) {
            return new SplitBySizeConfig(Integer.parseInt(sizeTextField.getText()));
        }
        else if(!partsTextField.getText().equals("")) {
            return new SplitByPartsConfig(Integer.parseInt(partsTextField.getText()));
        }
        else return null;
    }

    @Override
    public void setEditable(boolean value) {
        splitCheckBox.setEnabled(value);
        this.updateEnabledState();
    }

    @Override
    protected void updateEnabledState() {
        sizeTextField.setEnabled(splitCheckBox.isEnabled());
        partsTextField.setEnabled(splitCheckBox.isEnabled());
        sizeTextField.setEditable(splitCheckBox.isSelected() && partsTextField.getText().equals(""));
        partsTextField.setEditable(splitCheckBox.isSelected() && sizeTextField.getText().equals(""));
    }
}
