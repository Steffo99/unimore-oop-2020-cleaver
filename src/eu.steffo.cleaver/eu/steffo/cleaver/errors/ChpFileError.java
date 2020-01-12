package eu.steffo.cleaver.errors;

/**
 * An error occoured during the parsing of a {@literal .chp} file.
 */
public class ChpFileError extends Exception {
    public ChpFileError(String s) {
        super(s);
    }
}
