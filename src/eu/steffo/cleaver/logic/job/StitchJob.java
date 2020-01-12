package eu.steffo.cleaver.logic.job;

import eu.steffo.cleaver.errors.*;
import eu.steffo.cleaver.logic.progress.*;
import eu.steffo.cleaver.logic.stream.input.*;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;


/**
 * A {@link Job} to one or more <i>chopped</i> (*.chp + *.cXX) files back into the original file.
 *
 * The *.chp file is parsed to discover how to revert the chop process, then multiple {@link InputStream InputStreams} are created and the original file is
 * recreated through a {@link FileOutputStream}.
 */
public class StitchJob extends Job {
    /**
     * The password to be used in the decryption step(s), if there are any.
     */
    private String cryptKey;

    /**
     * The {@link File folder} where the *.chp file is located.
     *
     * The original file will be placed in that folder when it is re-created.
     */
    private File chpFolder;

    /**
     * The {@link Document} created by parsing the *.chp file as XML.
     */
    private Document chpDocument;

    /**
     * The size in bytes of the {@link BufferedOutputStream} wrapping the {@link FileOutputStream} created by {@link #run()} to reconstruct the original file.
     *
     * The same value is used for a temporary array in the {@link #run()} method where bytes are stored between being read through a {@link ICleaverInputStream}
     * and being written to the reconstructed file; after that amount of bytes are written, {@link #getProgress()} is called, updating the
     * {@link Progress} of this Job.
     */
    private static final int BUFFER_SIZE = 8192;

    /**
     * Construct a StitchJob, specifying the *.chp file to import the settings from.
     * @param file The *.chp file.
     * @throws ChpFileError If an error is encountered while parsing the *.chp file.
     */
    public StitchJob(File file) throws ChpFileError {
        this(file, null, null);
    }

    /**
     * Construct a StitchJob, specifying the *.chp file to import the settings from and an encryption key to use while decrypting the files.
     * @param file The *.chp file.
     * @param cryptKey The encryption key to use while decrypting the files.
     * @throws ChpFileError If an error is encountered while parsing the *.chp file.
     */
    public StitchJob(File file, String cryptKey) throws ChpFileError {
        this(file, cryptKey, null);
    }

    /**
     * Construct a StitchJob, and additionally specify the {@link Runnable} that should be called on progress updates.
     * @param chpFile The *.chp file.
     * @param cryptKey The encryption key to use while decrypting the files.
     * @param updateTable The {@link Runnable} that should be invoked when {@link #setProgress(Progress)} is called.
     * @throws ChpFileError If an error is encountered while parsing the *.chp file.
     * @see #StitchJob(File, String)
     */
    public StitchJob(File chpFile, String cryptKey, Runnable updateTable) throws ChpFileError {
        super(updateTable);
        this.cryptKey = cryptKey;
        this.chpDocument = getChpFileDocument(chpFile);
        this.chpFolder = chpFile.getParentFile();
    }

    //TODO: decide on a protection
    private File getResultFile() {
        return new File(chpFolder, chpDocument.getElementsByTagName("OriginalFile").item(0).getTextContent());
    }

    @Override
    public String getTypeString() {
        return "Stitch";
    }

    @Override
    public String getFileString() {
        return getResultFile().toString();
    }

    @Override
    public String getOperationsString() {
        Element element = (Element)(chpDocument.getDocumentElement().getFirstChild());
        String tagName = element.getTagName();

        StringBuilder s = new StringBuilder();
        boolean arrow = false;

        //Iterate over the elements of the document tree until the <OriginalFile> node is reached
        while(!tagName.equals("OriginalFile")) {

            if(arrow && !tagName.equals("Simple")) {
                s.append(" → ");
            }

            switch (tagName) {
                case "Crypt":
                    s.append("Decrypt");
                    break;
                case "Deflate":
                    s.append("Decompress (Deflate)");
                    break;
                case "Fork":
                    s.append(String.format("Merge (%s parts)", element.getAttribute("parts")));
                    break;
                case "Simple":
                    break;
                case "Split":
                    s.append(String.format("Merge (%s bytes)", element.getAttribute("part-size")));
                    break;
                default:
                    s.append("Unknown");
                    break;
            }

            element = (Element)element.getFirstChild();
            tagName = element.getTagName();
            arrow = true;
        }

        return s.toString();
    }

    /**
     * Create a new {@link Document} based on the contents of the passed {@link File}.
     * @param chpFile The file to create a {@link Document} from.
     * @return The created {@link Document}.
     * @throws ChpFileError If the .chp does not exist, or is corrupt.
     */
    protected static Document getChpFileDocument(File chpFile) throws ChpFileError {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new ProgrammingError(e.toString());
        }
        Document doc;
        try {
            doc = builder.parse(chpFile);
        } catch (SAXException e) {
            throw new ChpFileError("The .chp is corrupt!");
        } catch (IOException e) {
            throw new ChpFileError("The .chp file does not exist anymore!");
        }
        return doc;
    }

    @Override
    public void run() {
        try {
            Element root = chpDocument.getDocumentElement();
            Element cleaverNode = (Element)root.getFirstChild();
            File resultFile = getResultFile();

            InputStream inputStream = ICleaverInputStream.fromElement(cleaverNode, chpFolder, cryptKey);
            OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(resultFile), BUFFER_SIZE);

            //Pipe everything to the output, in chunks
            this.setProgress(new WorkingProgress());

            byte[] buffer = new byte[BUFFER_SIZE];
            int readBytes = inputStream.read(buffer);
            while(readBytes != -1) {
                outputStream.write(buffer, 0, readBytes);
                long targetSize = resultFile.length() + inputStream.available();
                this.setProgress(new WorkingProgress((float)(resultFile.length()) / (float)targetSize));
                readBytes = inputStream.read(buffer);
            }

            inputStream.close();
            outputStream.close();

            this.setProgress(new FinishedProgress());

        } catch (Throwable e) {
            e.printStackTrace();
            this.setProgress(new ErrorProgress(e));
        }

    }
}
