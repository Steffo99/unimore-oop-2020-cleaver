package eu.steffo.cleaver.logic.stream.input;

import java.io.*;

public class CleaverForkFileInputStream extends InputStream implements ICleaverInputStream {
    private final File baseFile;
    private FileInputStream[] fileInputStreams;
    private int writeTo;
    private long partSize;

    public CleaverForkFileInputStream(File baseFile, int parts) throws FileNotFoundException {
        this.baseFile = baseFile;
        this.fileInputStreams = new FileInputStream[parts];
        for(int i = 0; i < parts; i++) {
            File file = new File(String.format("%s.c%s", baseFile.getAbsolutePath(), i));
            this.fileInputStreams[i] = new FileInputStream(file);
        }
        this.writeTo = 0;
        this.partSize = 1;
    }

    @Override
    public int read() throws IOException {
        int b = fileInputStreams[writeTo].read();
        writeTo += 1;
        if(writeTo >= fileInputStreams.length) {
            writeTo = 0;
            partSize += 1;
        }
        return b;
    }

    @Override
    public void close() throws IOException {
        super.close();
        for(FileInputStream fileInputStream : fileInputStreams) {
            fileInputStream.close();
        }
    }

    /**
     * Get the base {@link File}.
     *
     * The stream will read from multiple files having a name constituted by the base {@link File} name and a *.cXX extension.
     *
     * For example, if it is {@literal foo.txt}, the stream will read from {@literal foo.txt.c1}, {@literal foo.txt.c2}, and so on.
     *
     * @return The base file.
     */
    public File getBaseFile() {
        return baseFile;
    }

    /**
     * @return The number of bytes read from each part.
     */
    public long getPartSize() {
        return partSize;
    }

    /**
     * @return The number of file parts to read from.
     */
    public int getParts() {
        return fileInputStreams.length;
    }

    /**
     * @return The number of the next file where a byte should be read from, starting from 0 to the number of parts -1.
     */
    public int getWriteTo() {
        return writeTo;
    }
}
