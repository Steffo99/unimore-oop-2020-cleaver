package eu.steffo.cleaver.gui.panels.rows.option;

import eu.steffo.cleaver.logic.split.SplitByPartsConfig;
import eu.steffo.cleaver.logic.split.SplitBySizeConfig;
import eu.steffo.cleaver.logic.split.SplitConfig;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * A {@link OptionRow} allowing the {@link SplitConfig configuration of the split step} of the file chop process.
 *
 * @see eu.steffo.cleaver.gui.panels.ChopPanel
 */
public class SplitRow extends OptionRow {
    /**
     * The checkbox enabling or disabling the split step.
     *
     * If unticked, {@link #getSplitConfig(long)} will return {@literal null}.
     */
    protected final JCheckBox splitCheckBox;

    /**
     * A separator between the checkbox and the part size selector.
     */
    protected final JSeparator firstSeparator;

    /**
     * A label with "Part size" written on it.
     */
    protected final JLabel sizeLabel;

    /**
     * The text field where the user can select the size of the parts to split the file in.
     *
     * If something is written on it, it disables the {@link #partsTextField}.
     *
     * It is disabled if something is written on the {@link #partsTextField}.
     */
    protected final JTextField sizeTextField;

    /**
     * A label with "B" (bytes) written on it.
     */
    protected final JLabel sizeUnitLabel;

    /**
     * A separator between the file part size selector and the parts number selector.
     */
    protected final JSeparator secondSeparator;

    /**
     * A label with "Parts" written on it.
     */
    protected final JLabel partsLabel;

    /**
     * The text field where the user can select the number of parts to split the file in.
     *
     * If something is written on it, it disables the {@link #sizeTextField}.
     *
     * It is disabled if something is written on the {@link #sizeTextField}.
     */
    protected final JTextField partsTextField;

    /**
     * Construct a SplitRow.
     */
    public SplitRow() {
        super();

        this.add(Box.createHorizontalStrut(8));

        splitCheckBox = new JCheckBox("Split");
        splitCheckBox.addActionListener(e -> update());
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
                update();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                update();
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
                update();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                update();
            }
        });
        this.add(partsTextField);

        this.add(Box.createHorizontalStrut(8));

        this.update();
    }

    /**
     * Create a {@link SplitConfig} from the settings in this {@link OptionRow}.
     * @param fileSize The size of the file that the {@link SplitConfig} is being generated for.
     * @return The resulting {@link SplitConfig}, or {@literal null} if the {@link #splitCheckBox} is unticked.
     */
    public SplitConfig getSplitConfig(long fileSize) throws NumberFormatException {
        if(!splitCheckBox.isSelected()) {
            return null;
        }
        //TODO: catch exception here and display an error
        else if(!sizeTextField.getText().equals("")) {
            return new SplitBySizeConfig(Integer.parseInt(sizeTextField.getText()), fileSize);
        }
        else if(!partsTextField.getText().equals("")) {
            return new SplitByPartsConfig(Integer.parseInt(partsTextField.getText()), fileSize);
        }
        else return null;
    }

    @Override
    public void setEditable(boolean value) {
        splitCheckBox.setEnabled(value);
        this.update();
    }

    /**
     * Refresh the enabled/disabled and editable/non-editable status of all components in the SplitRow.
     */
    protected void update() {
        sizeTextField.setEnabled(splitCheckBox.isEnabled());
        partsTextField.setEnabled(splitCheckBox.isEnabled());
        sizeTextField.setEditable(splitCheckBox.isSelected() && partsTextField.getText().equals(""));
        partsTextField.setEditable(splitCheckBox.isSelected() && sizeTextField.getText().equals(""));
    }
}
