package eu.steffo.cleaver.errors;

/**
 * An exception that should never be thrown during the execution of the program.
 *
 * It is thrown when an exception that should never be thrown is caught, such as {@link javax.xml.parsers.ParserConfigurationException}.
 */
public class ProgrammingError extends Exception {
}
