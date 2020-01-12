package eu.steffo.cleaver.gui.panels.rows.option;

import eu.steffo.cleaver.logic.config.PasswordConfig;
import eu.steffo.cleaver.logic.config.ICryptConfig;

import javax.swing.*;

/**
 * A {@link OptionRow} allowing the {@link PasswordConfig configuration of the crypt step} of the file chop process.
 *
 * <p><img src="doc-files/cryptrow.png" alt=""></p>
 *
 * @see eu.steffo.cleaver.gui.panels.ChopPanel
 */
public class CryptRow extends OptionRow {
    /**
     * The checkbox enabling or disabling the crypt step.
     *
     * If unticked, {@link #getCryptConfig()} will return {@literal null}.
     */
    protected final JCheckBox cryptCheckBox;

    /**
     * A separator between the checkbox and the encryption key field.
     */
    protected final JSeparator separator;

    /**
     * A label with "Password" written on it.
     */
    protected final JLabel passwordLabel;

    /**
     * The {@link JTextField} where the user can input the desired encryption key for the files, in form of a {@link String}.
     */
    protected final JTextField passwordTextField;

    /**
     * Construct a CryptRow.
     */
    public CryptRow() {
        super();

        this.add(Box.createHorizontalStrut(8));

        cryptCheckBox = new JCheckBox("Encrypt");
        cryptCheckBox.addActionListener(e -> update());
        this.add(cryptCheckBox);

        this.add(Box.createHorizontalStrut(8));

        separator = new JSeparator(JSeparator.VERTICAL);
        this.add(separator);

        this.add(Box.createHorizontalStrut(8));

        passwordLabel = new JLabel("Password");
        this.add(passwordLabel);

        this.add(Box.createHorizontalStrut(8));

        passwordTextField = new JTextField();
        this.add(passwordTextField);

        this.add(Box.createHorizontalStrut(8));

        this.update();
    }

    /**
     * Create a {@link PasswordConfig} from the settings in this {@link OptionRow}.
     * @return The resulting {@link PasswordConfig}, or {@literal null} if the {@link #cryptCheckBox} is unticked.
     */
    public ICryptConfig getCryptConfig() {
        if(!cryptCheckBox.isSelected()) {
            return null;
        }
        return new PasswordConfig(passwordTextField.getText());
    }

    @Override
    public void setEditable(boolean value) {
        cryptCheckBox.setEnabled(value);
        this.update();
    }

    /**
     * Refresh the enabled/disabled and editable/non-editable status of all components in the SplitRow.
     */
    protected void update() {
        passwordTextField.setEnabled(cryptCheckBox.isEnabled());
        passwordTextField.setEditable(cryptCheckBox.isSelected());
    }
}
