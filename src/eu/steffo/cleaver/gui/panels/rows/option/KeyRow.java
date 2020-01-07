package eu.steffo.cleaver.gui.panels.rows.option;

import eu.steffo.cleaver.logic.config.CryptConfig;

import javax.swing.*;

/**
 * A {@link OptionRow} allowing the {@link CryptConfig configuration of the crypt step} of the file stitch process.
 *
 * This configuration is used only if the selected *.chp file specifies that the *.cXX are encrypted.
 *
 * @see eu.steffo.cleaver.gui.panels.StitchPanel
 */
public class KeyRow extends OptionRow {
    /**
     * A label with "Encryption key" written on it.
     */
    protected final JLabel keyLabel;

    /**
     * The text field where the user can input the encryption key.
     */
    protected final JTextField keyTextField;

    /**
     * A label with "(if required)" written on it.
     */
    protected final JLabel optionalLabel;

    /**
     * Construct a KeyRow.
     */
    public KeyRow() {
        super();

        this.add(Box.createHorizontalStrut(8));

        keyLabel = new JLabel("Encryption key");
        this.add(keyLabel);

        this.add(Box.createHorizontalStrut(8));

        keyTextField = new JTextField();
        this.add(keyTextField);

        this.add(Box.createHorizontalStrut(8));

        optionalLabel = new JLabel("(if required)");
        this.add(optionalLabel);

        this.add(Box.createHorizontalStrut(8));

        this.update();
    }

    /**
     * @return The encryption key inserted in the {@link #keyTextField}.
     */
    public String getKey() {
        return keyTextField.getText();
    }

    @Override
    public void setEditable(boolean value) {
        keyTextField.setEditable(value);
        this.update();
    }

    /**
     * Refresh the enabled/disabled and editable/non-editable status of all components in the KeyRow.
     */
    protected void update() {
        keyTextField.setEnabled(keyTextField.isEditable());
    }
}
