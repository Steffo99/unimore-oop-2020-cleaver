package eu.steffo.cleaver.logic.job;

import eu.steffo.cleaver.errors.ChpFileError;
import eu.steffo.cleaver.errors.ProgrammingError;
import eu.steffo.cleaver.logic.config.*;
import eu.steffo.cleaver.logic.stream.input.*;
import eu.steffo.cleaver.logic.progress.ErrorProgress;
import eu.steffo.cleaver.logic.progress.FinishedProgress;
import eu.steffo.cleaver.logic.progress.Progress;
import eu.steffo.cleaver.logic.progress.WorkingProgress;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;


/**
 * A {@link Job} that converts <i>chopped</i> (*.chp + *.cXX) files back into regular files.
 */
public class StitchJob extends Job {
    private String cryptKey;
    private File chpFolder;
    private Document chpDocument;

    /**
     * Construct a StitchJob, specifying the *.chp file to import the settings from.
     * @param file The *.chp file.
     * @throws ChpFileError If there's an error while parsing the *.chp file.
     * @throws ProgrammingError It shouldn't be thrown, but it isn't caught to allow its display in the GUI if it actually happens.
     */
    public StitchJob(File file) throws ChpFileError, ProgrammingError {
        this(file, null, null);
    }

    /**
     * Construct a StitchJob, specifying the *.chp file to import the settings from and an encryption key to use while decrypting the files.
     * @param file The *.chp file.
     * @param cryptKey The encryption key to use while decrypting the files.
     * @throws ChpFileError If there's an error while parsing the *.chp file.
     * @throws ProgrammingError It shouldn't be thrown, but it isn't caught to allow its display in the GUI if it actually happens.
     * @see #StitchJob(File)
     */
    public StitchJob(File file, String cryptKey) throws ChpFileError, ProgrammingError {
        this(file, cryptKey, null);
    }

    /**
     * Construct a StitchJob, and additionally specify the {@link Runnable} that should be called on progress updates.
     * @param chpFile The *.chp file.
     * @param cryptKey The encryption key to use while decrypting the files.
     * @param updateTable The {@link Runnable} that should be invoked when {@link #setProgress(Progress)} is called.
     * @throws ChpFileError If there's an error while parsing the *.chp file.
     * @throws ProgrammingError It shouldn't be thrown, but it isn't caught to allow its display in the GUI if it actually happens.
     * @see #StitchJob(File, String)
     * @see Job#Job(Runnable)
     */
    public StitchJob(File chpFile, String cryptKey, Runnable updateTable) throws ChpFileError, ProgrammingError {
        super(updateTable);
        this.cryptKey = cryptKey;
        this.chpDocument = getChpFileDocument(chpFile);
        this.chpFolder = chpFile.getParentFile();
    }

    @Override
    public String getTypeString() {
        return "Stitch";
    }

    public File getResultFile() {
        return new File(chpFolder, chpDocument.getElementsByTagName("OriginalFile").item(0).getTextContent());
    }

    @Override
    public String getFileString() {
        return getResultFile().toString();
    }

    @Override
    public String getProcessString() {
        Element element = (Element)(chpDocument.getDocumentElement().getFirstChild());
        StringBuilder s = new StringBuilder();

        boolean arrow = false;

        while(!element.getTagName().equals("OriginalFile")) {

            if(arrow) {
                s.append(" → ");
            }

            String tagName = element.getTagName();
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
            arrow = true;
        }

        return s.toString();
    }

    /**
     * Instantiate a new {@link Document} based on the contents of the passed file.
     * @param chpFile The file to create a {@link Document} from.
     * @return The created {@link Document}.
     * @throws ChpFileError If the .chp does not exist, or is corrupt.
     * @throws ProgrammingError It should never happen, as the parser should be already configured correctly.
     */
    protected static Document getChpFileDocument(File chpFile) throws ChpFileError, ProgrammingError {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new ProgrammingError("Parser configuration error in the ChopJob.");
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

    private static final int UPDATE_EVERY_BYTES = 16000;

    @Override
    public void run() {
        try {
            Element root = chpDocument.getDocumentElement();
            Element cleaverNode = (Element)root.getFirstChild();
            File resultFile = getResultFile();

            InputStream inputStream = ICleaverInputStream.fromElement(cleaverNode, chpFolder, cryptKey);
            OutputStream outputStream = new FileOutputStream(resultFile);

            //Pipe everything to the output
            int bytesUntilNextUpdate = UPDATE_EVERY_BYTES;
            this.setProgress(new WorkingProgress());

            int i;
            while((i = inputStream.read()) != -1) {
                outputStream.write(i);
                bytesUntilNextUpdate -= 1;
                if(bytesUntilNextUpdate <= 0) {
                    this.setProgress(new WorkingProgress((float)(resultFile.length() - inputStream.available()) / (float)resultFile.length()));
                    bytesUntilNextUpdate = UPDATE_EVERY_BYTES;
                }
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
