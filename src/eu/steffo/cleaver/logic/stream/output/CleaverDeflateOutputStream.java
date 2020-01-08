package eu.steffo.cleaver.logic.stream.output;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.OutputStream;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

public class CleaverDeflateOutputStream extends DeflaterOutputStream implements ICleaverOutputStream {
    /**
     * Construct a new CleaverDeflateOutputStream and ensure the passed {@link OutputStream} implements {@link ICleaverOutputStream}.
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
