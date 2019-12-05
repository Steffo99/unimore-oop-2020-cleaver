package eu.steffo.cleaver.gui;

import javax.swing.*;
import java.awt.*;

public class CleaverModeSelectPanel extends JPanel {
    protected JLabel titleLabel;

    public CleaverModeSelectPanel() {
        super();

        this.setBorder(BorderFactory.createLineBorder(Color.red));

        titleLabel = new JLabel("Cleaver Mode Select");
        this.add(titleLabel);
    }
}
