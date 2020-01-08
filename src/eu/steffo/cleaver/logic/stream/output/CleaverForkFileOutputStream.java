package eu.steffo.cleaver.logic.stream.output;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.*;

/**
 * A custom {@link OutputStream} that writes the bytes received in input in multiple files with a progressively increasing number (.c1, .c2, .c3, and so on).
 *
 * Bytes are written one at a time to the files in a round-robin format until the stream is exausted.
 */
public class CleaverForkFileOutputStream extends OutputStream implements ICleaverOutputStream {
    private final String fileBaseName;
    private FileOutputStream[] fileOutputStreams;
    private int writeTo;
    private long partSize;

    /**
     * Construct a CleaverForkFileOutputStream.
     * @param fileBaseName The name of the files without the extension. If it is {@literal example}, the created files will be {@literal example.c1}, {@literal example.c2}, and so on.
     * @param parts The number of parts to be created.
     */
    public CleaverForkFileOutputStream(String fileBaseName, int parts) throws FileNotFoundException {
        this.fileBaseName = fileBaseName;
        this.fileOutputStreams = new FileOutputStream[parts];
        for(int i = 0; i < parts; i++) {
            File file = new File(String.format("%s.c%s", fileBaseName, i));
            this.fileOutputStreams[i] = new FileOutputStream(file);
        }
        this.writeTo = 0;
        this.partSize = 1;
    }

    @Override
    public Element toElement(Document doc) {
        Element element = doc.createElement("Fork");
        element.setTextContent(fileBaseName);

        Attr partSizeAttr = doc.createAttribute("part-size");
        partSizeAttr.setValue(Long.toString(partSize));
        element.setAttributeNode(partSizeAttr);

        Attr partCountAttr = doc.createAttribute("parts");
        partCountAttr.setValue(Long.toString(fileOutputStreams.length));
        element.setAttributeNode(partCountAttr);

        return element;
    }

    @Override
    public void write(int b) throws IOException {
        fileOutputStreams[writeTo].write(b);
        writeTo += 1;
        if(writeTo >= fileOutputStreams.length) {
            writeTo = 0;
            partSize += 1;
        }
    }

    /**
     * @return The name of the files without the extension. If it is {@literal example}, the created files will be {@literal example.c1}, {@literal example.c2}, and so on.
     */
    public String getFileBaseName() {
        return fileBaseName;
    }

    /**
     * @return The current maximum size of a part, in bytes.
     */
    public long getPartSize() {
        return partSize;
    }

    /**
     * @return The number of file parts to create.
     */
    public int getParts() {
        return fileOutputStreams.length;
    }

    /**
     * @return The number of the next file where a byte should be written, from 0 to the number of parts -1.
     */
    public int getWriteTo() {
        return writeTo;
    }
}
