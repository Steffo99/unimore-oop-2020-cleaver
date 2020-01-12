package eu.steffo.cleaver.gui.panels.rows.option;

import eu.steffo.cleaver.logic.config.PasswordConfig;

import javax.swing.*;

/**
 * A {@link OptionRow} allowing the {@link PasswordConfig configuration of the crypt step} of the file stitch process.
 *
 * This configuration is used only if the selected *.chp file specifies that the *.cXX are encrypted.
 *
 * <p><img src="doc-files/keyrow.png" alt=""></p>
 *
 * @see eu.steffo.cleaver.gui.panels.StitchPanel
 */
public class KeyRow extends OptionRow {
    /**
     * A label with "Password" written on it.
     */
    protected final JLabel passwordLabel;

    /**
     * The text field where the user can input the encryption key.
     */
    protected final JTextField passwordTextField;

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

        passwordLabel = new JLabel("Password");
        this.add(passwordLabel);

        this.add(Box.createHorizontalStrut(8));

        passwordTextField = new JTextField();
        this.add(passwordTextField);

        this.add(Box.createHorizontalStrut(8));

        optionalLabel = new JLabel("(if required)");
        this.add(optionalLabel);

        this.add(Box.createHorizontalStrut(8));

        this.update();
    }

    /**
     * @return The encryption key inserted in the {@link #passwordTextField}.
     */
    public String getKey() {
        return passwordTextField.getText();
    }

    @Override
    public void setEditable(boolean value) {
        passwordTextField.setEditable(value);
        this.update();
    }

    /**
     * Refresh the enabled/disabled and editable/non-editable status of all components in the KeyRow.
     */
    protected void update() {
        passwordTextField.setEnabled(passwordTextField.isEditable());
    }
}
