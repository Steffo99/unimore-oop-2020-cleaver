package eu.steffo.cleaver.logic.stream.input;

import java.io.InputStream;
import java.util.zip.InflaterInputStream;

/**
 * A {@link ICleaverInputStream} that decompresses incoming data with the Deflate algorithm.
 *
 * @see InflaterInputStream
 */
public class CleaverDeflateInputStream extends InflaterInputStream implements ICleaverInputStream {

    /**
     * Construct a new CleaverDeflateInputStream wrapping the passed {@link InputStream} and ensuring it {@link InputStream} implements
     * {@link ICleaverInputStream}.
     * @see InflaterInputStream#InflaterInputStream(InputStream)
     */
    public CleaverDeflateInputStream(InputStream in) {
        super(in);

        if(!(in instanceof ICleaverInputStream)) {
            throw new IllegalArgumentException("The InputStream passed to the CleaverDeflateInputStream must implement ICleaverInputStream.");
        }
    }
}
