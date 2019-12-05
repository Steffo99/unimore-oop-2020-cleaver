package eu.steffo.cleaver.gui;

import java.awt.*;
import javax.swing.*;

public abstract class CleaverRowPanel extends JPanel {

    public CleaverRowPanel() {
        super();

        this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        this.setAlignmentX(Component.LEFT_ALIGNMENT);
    }
}
