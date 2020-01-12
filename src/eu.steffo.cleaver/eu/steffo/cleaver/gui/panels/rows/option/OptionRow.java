package eu.steffo.cleaver.gui.panels.rows.option;

import eu.steffo.cleaver.gui.panels.rows.Row;

/**
 * A {@link Row} where options can be set.
 *
 * It can be enabled and disabled.
 */
public abstract class OptionRow extends Row {
    /**
     * Allow edits in the fields of the OptionRow.
     * @param value {@literal true} if the OptionRow should be editable, {@literal false} otherwise.
     */
    public abstract void setEditable(boolean value);
}
