package eu.steffo.cleaver.logic.stream.input;

import java.io.InputStream;
import java.util.zip.InflaterInputStream;

public class CleaverDeflateInputStream extends InflaterInputStream implements ICleaverInputStream {

    /**
     * Construct a new CleaverDeflateInputStream and ensure the passed {@link InputStream} implements {@link ICleaverInputStream}.
     * @see InflaterInputStream#InflaterInputStream(InputStream)
     */
    public CleaverDeflateInputStream(InputStream in) {
        super(in);
        if(!(in instanceof ICleaverInputStream)) {
            throw new IllegalArgumentException("The InputStream passed to the CleaverDeflateInputStream must implement ICleaverInputStream.");
        }
    }
}
