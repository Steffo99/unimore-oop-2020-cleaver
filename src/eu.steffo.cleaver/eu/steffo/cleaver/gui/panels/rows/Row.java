package eu.steffo.cleaver.gui.panels.rows;

import java.awt.*;
import javax.swing.*;

/**
 * A left-aligned {@link JPanel} with a <i>horizontal</i> {@link BoxLayout}.
 */
public abstract class Row extends JPanel {

    /**
     * Construct a Row, setting the layout and the X axis alignment.
     */
    public Row() {
        super();

        this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        this.setAlignmentX(Component.LEFT_ALIGNMENT);
    }
}
