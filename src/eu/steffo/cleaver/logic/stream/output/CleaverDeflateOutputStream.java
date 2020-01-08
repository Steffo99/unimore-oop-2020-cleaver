package eu.steffo.cleaver.logic.stream.output;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.OutputStream;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

public class CleaverDeflateOutputStream extends DeflaterOutputStream implements ICleaverOutputStream {
    /**
     * Construct a new CleaverCompressOutputStream and ensure the passed {@link OutputStream} implements {@link ICleaverOutputStream}.
     * @see DeflaterOutputStream#DeflaterOutputStream(OutputStream, Deflater, int, boolean)
     */
    public CleaverDeflateOutputStream(OutputStream out, Deflater def, int size, boolean syncFlush) {
        super(out, def, size, syncFlush);
        if(!(out instanceof ICleaverOutputStream)) {
            throw new IllegalArgumentException("The OutputStream passed to the CleaverCompressOutputStream must implement ICleaverOutputStream.");
        }
    }

    /**
     * Construct a new CleaverCompressOutputStream and ensure the passed {@link OutputStream} implements {@link ICleaverOutputStream}.
     * @see DeflaterOutputStream#DeflaterOutputStream(OutputStream, Deflater, int)
     */
    public CleaverDeflateOutputStream(OutputStream out, Deflater def, int size) {
        super(out, def, size);
        if(!(out instanceof ICleaverOutputStream)) {
            throw new IllegalArgumentException("The OutputStream passed to the CleaverCompressOutputStream must implement ICleaverOutputStream.");
        }
    }

    /**
     * Construct a new CleaverCompressOutputStream and ensure the passed {@link OutputStream} implements {@link ICleaverOutputStream}.
     * @see DeflaterOutputStream#DeflaterOutputStream(OutputStream, Deflater, boolean)
     */
    public CleaverDeflateOutputStream(OutputStream out, Deflater def, boolean syncFlush) {
        super(out, def, syncFlush);
        if(!(out instanceof ICleaverOutputStream)) {
            throw new IllegalArgumentException("The OutputStream passed to the CleaverCompressOutputStream must implement ICleaverOutputStream.");
        }
    }

    /**
     * Construct a new CleaverCompressOutputStream and ensure the passed {@link OutputStream} implements {@link ICleaverOutputStream}.
     * @see DeflaterOutputStream#DeflaterOutputStream(OutputStream, Deflater)
     */
    public CleaverDeflateOutputStream(OutputStream out, Deflater def) {
        super(out, def);
        if(!(out instanceof ICleaverOutputStream)) {
            throw new IllegalArgumentException("The OutputStream passed to the CleaverCompressOutputStream must implement ICleaverOutputStream.");
        }
    }

    /**
     * Construct a new CleaverCompressOutputStream and ensure the passed {@link OutputStream} implements {@link ICleaverOutputStream}.
     * @see DeflaterOutputStream#DeflaterOutputStream(OutputStream, boolean)
     */
    public CleaverDeflateOutputStream(OutputStream out, boolean syncFlush) {
        super(out, syncFlush);
        if(!(out instanceof ICleaverOutputStream)) {
            throw new IllegalArgumentException("The OutputStream passed to the CleaverCompressOutputStream must implement ICleaverOutputStream.");
        }
    }

    /**
     * Construct a new CleaverCompressOutputStream and ensure the passed {@link OutputStream} implements {@link ICleaverOutputStream}.
     * @see DeflaterOutputStream#DeflaterOutputStream(OutputStream)
     */
    public CleaverDeflateOutputStream(OutputStream out) {
        super(out);
        if(!(out instanceof ICleaverOutputStream)) {
            throw new IllegalArgumentException("The OutputStream passed to the CleaverCompressOutputStream must implement ICleaverOutputStream.");
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
