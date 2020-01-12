package eu.steffo.cleaver.errors;

/**
 * An exception that is never supposed to happen during the execution of the program.
 *
 * It is thrown when an exception that should never be thrown is caught, such as {@link javax.xml.parsers.ParserConfigurationException}.
 *
 * As they are never supposed to happen, they don't need to be caught, therefore they extend {@link RuntimeException}.
 */
public class ProgrammingError extends RuntimeException {
    public ProgrammingError(String s) {
        super(s);
    }
}
