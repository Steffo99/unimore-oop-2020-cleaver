package eu.steffo.cleaver.errors;

/**
 * An error in the parsing of the {@literal .chp} file occoured.
 */
public class ChpFileError extends Exception {
    public ChpFileError(String s) {
        super(s);
    }
}
