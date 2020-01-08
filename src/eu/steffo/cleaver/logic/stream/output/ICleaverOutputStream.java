package eu.steffo.cleaver.logic.stream.output;

import eu.steffo.cleaver.logic.stream.ICleaverStream;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public interface ICleaverOutputStream extends ICleaverStream {
    /**
     * Create a {@link Element} representing the stream (to be used in *.chp metadata files).
     *
     * @param doc The {@link Document} the {@link Element} should be created in.
     * @return The created {@link Element}.
     */
    Element toElement(Document doc);
}
