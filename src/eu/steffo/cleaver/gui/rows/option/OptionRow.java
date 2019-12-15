package eu.steffo.cleaver.gui.rows.option;

import eu.steffo.cleaver.gui.rows.Row;

import java.awt.*;

public abstract class OptionRow extends Row {
    public abstract void setEditable(boolean value);
    protected abstract void updateEnabledState();
}
