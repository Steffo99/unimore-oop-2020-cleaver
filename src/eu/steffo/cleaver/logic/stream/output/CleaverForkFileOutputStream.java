package eu.steffo.cleaver.logic.stream.output;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.*;

/**
 * A {@link ICleaverOutputStream} that reads split data from a specific number of files having the same size.
 *
 * Bytes are written one at a time to the files in a round-robin sequence until the stream is exausted.
 */
public class CleaverForkFileOutputStream extends OutputStream implements ICleaverOutputStream {
    /**
     * @see #getBaseFile()
     */
    private final File baseFile;

    /**
     * The {@link OutputStream OutputStreams} this object can write to.
     *
     * Each one wraps a different {@link FileOutputStream} writing to a *.cXX file.
     */
    private BufferedOutputStream[] outputStreams;

    /**
     * The index of the next {@link #outputStreams FileInputStream} to write a byte to.
     */
    private int writeTo;

    /**
     * The number of bytes that have been written to a single part.
     */
    private long partSize;

    /**
     * The maaximum buffer size in bytes of the {@link BufferedOutputStream BufferedOutputStreams} created by this object (currently {@value}).
     */
    private static final int BUFFER_SIZE = 8192;

    /**
     * Construct a CleaverForkFileOutputStream.
     * @param baseFile {@link #getBaseFile() Please see getBaseFile().}
     * @param parts The number of parts to be created.
     * @throws FileNotFoundException If a file can't be created.
     */
    public CleaverForkFileOutputStream(File baseFile, int parts) throws FileNotFoundException {
        this.baseFile = baseFile;
        this.outputStreams = new BufferedOutputStream[parts];
        for(int i = 0; i < parts; i++) {
            File file = new File(String.format("%s.c%s", baseFile.getAbsolutePath(), i));
            this.outputStreams[i] = new BufferedOutputStream(new FileOutputStream(file), BUFFER_SIZE);
        }
        this.writeTo = 0;
        this.partSize = 1;
    }

    /**
     * Get the base {@link File}.
     *
     * The base file is the {@link File} will be reconstructed after reversing the Fork operation with a
     * {@link eu.steffo.cleaver.logic.stream.input.CleaverForkFileInputStream}.
     *
     * The files created by this stream will have the same name of the base file, with the addition of a .cXX extension.
     *
     * @return The base file.
     */
    public File getBaseFile() {
        return baseFile;
    }

    /**
     * @return The number of bytes written to each part.
     */
    public long getPartSize() {
        return partSize;
    }

    /**
     * @return The number of file parts to create.
     */
    public int getParts() {
        return outputStreams.length;
    }

    /**
     * @return The number of the next file where a byte should be written, from 0 to the number of parts -1.
     */
    public int getWriteTo() {
        return writeTo;
    }

    /**
     * @return The number of bytes that have been written, in total.
     */
    public long getTotalReadBytes() {
        return getParts() * getPartSize() + getWriteTo();
    }

    @Override
    public void write(int b) throws IOException {
        outputStreams[writeTo].write(b);
        writeTo += 1;
        if(writeTo >= outputStreams.length) {
            writeTo = 0;
            partSize += 1;
        }
    }

    @Override
    public void close() throws IOException {
        super.close();
        for(BufferedOutputStream outputStream : outputStreams) {
            outputStream.close();
        }
    }

    @Override
    public Element toElement(Document doc) {
        Element element = doc.createElement("Fork");

        Element fileElement = doc.createElement("OriginalFile");
        fileElement.setTextContent(baseFile.getName());
        element.appendChild(fileElement);

        Attr partSizeAttr = doc.createAttribute("part-size");
        partSizeAttr.setValue(Long.toString(partSize));
        element.setAttributeNode(partSizeAttr);

        Attr partCountAttr = doc.createAttribute("parts");
        partCountAttr.setValue(Long.toString(outputStreams.length));
        element.setAttributeNode(partCountAttr);

        return element;
    }
}
