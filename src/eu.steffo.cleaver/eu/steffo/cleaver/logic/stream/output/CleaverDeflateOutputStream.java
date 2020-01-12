package eu.steffo.cleaver.logic.stream.output;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.OutputStream;
import java.util.zip.DeflaterOutputStream;

/**
 * A {@link ICleaverOutputStream} that compresses incoming data with the Deflate algorithm.
 *
 * @see DeflaterOutputStream
 */
public class CleaverDeflateOutputStream extends DeflaterOutputStream implements ICleaverOutputStream {
    /**
     * Construct a new CleaverDeflateOutputStream and ensure the wrapped {@link OutputStream} implements {@link ICleaverOutputStream}.
     * @param out The {@link OutputStream} this stream should wrap.
     * @see DeflaterOutputStream#DeflaterOutputStream(OutputStream)
     */
    public CleaverDeflateOutputStream(OutputStream out) {
        super(out);
        if(!(out instanceof ICleaverOutputStream)) {
            throw new IllegalArgumentException("The OutputStream passed to the CleaverDeflateOutputStream must implement ICleaverOutputStream.");
        }
    }

    @Override
    public Element toElement(Document doc) {
        Element element = doc.createElement("Deflate");

        Element child = ((ICleaverOutputStream)out).toElement(doc);
        element.appendChild(child);

        return element;
    }
}
