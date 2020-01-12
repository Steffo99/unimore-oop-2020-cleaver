package eu.steffo.cleaver.logic.stream.output;

import eu.steffo.cleaver.logic.stream.ICleaverStream;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * A {@link ICleaverStream} that is also a {@link java.io.OutputStream} (a stream you can write bytes to).
 */
public interface ICleaverOutputStream extends ICleaverStream {
    /**
     * Create a {@link Element} representing the stream (to be used in *.chp metadata files).
     *
     * @param doc The {@link Document} the {@link Element} should be created in.
     * @return The created {@link Element}.
     * @see eu.steffo.cleaver.logic.job.ChopJob
     */
    Element toElement(Document doc);
}
