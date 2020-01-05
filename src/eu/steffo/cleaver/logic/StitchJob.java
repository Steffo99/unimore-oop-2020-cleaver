package eu.steffo.cleaver.logic;

import eu.steffo.cleaver.errors.ChpFileError;
import eu.steffo.cleaver.errors.ProgrammingError;
import eu.steffo.cleaver.logic.compress.CompressConfig;
import eu.steffo.cleaver.logic.crypt.CryptConfig;
import eu.steffo.cleaver.logic.progress.ErrorProgress;
import eu.steffo.cleaver.logic.progress.Progress;
import eu.steffo.cleaver.logic.split.SplitByPartsConfig;
import eu.steffo.cleaver.logic.split.SplitBySizeConfig;
import eu.steffo.cleaver.logic.split.SplitConfig;

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
    private File resultFile;
    private SplitConfig splitConfig = null;
    private CryptConfig cryptConfig = null;
    private CompressConfig compressConfig = null;

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
     * Construct a StitchJob, and additionally specify the {@link Runnable} that should be called on progress updates..
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
        parseChp(openChp(chpFile), cryptKey);
    }

    @Override
    public String getType() {
        return "Stitch";
    }

    @Override
    public File getFile() {
        return resultFile;
    }

    @Override
    public SplitConfig getSplitConfig() {
        return splitConfig;
    }

    @Override
    public CryptConfig getCryptConfig() {
        return cryptConfig;
    }

    @Override
    public CompressConfig getCompressConfig() {
        return compressConfig;
    }

    protected static Document openChp(File file) throws ChpFileError, ProgrammingError {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new ProgrammingError();
        }
        Document doc;
        try {
            doc = builder.parse(file);
        } catch (SAXException e) {
            throw new ProgrammingError();
        } catch (IOException e) {
            throw new ChpFileError("The .chp file does not exist anymore!");
        }
        return doc;
    }

    /**
     * Read a {@link Document} and set {@link #splitConfig}, {@link #cryptConfig} and {@link #compressConfig} accordingly.
     * @param doc The {@link Document} to be read.
     * @param cryptKey The encryption key to use in the {@link CryptConfig}.
     * @throws ChpFileError If there's an error while parsing the *.chp file.
     */
    protected final void parseChp(Document doc, String cryptKey) throws ChpFileError {
        Element root = doc.getDocumentElement();

        NodeList originals = root.getElementsByTagName("Original");
        NodeList splits = root.getElementsByTagName("Split");
        NodeList crypts = root.getElementsByTagName("Crypt");
        NodeList compresses = root.getElementsByTagName("Compress");

        Node originalNode = originals.item(0);
        Node splitNode = splits.item(0);
        Node cryptNode = crypts.item(0);
        Node compressNode = compresses.item(0);

        if(originalNode == null) {
            throw new ChpFileError("No original filename found (<Original> tag)");
        }
        Element original = (Element)originalNode;
        resultFile = new File(original.getTextContent());

        if(splitNode != null) {
            Element split = (Element)splitNode;
            String splitMode = split.getAttribute("mode");
            if(splitMode.equals("by-parts")) {
                splitConfig = new SplitByPartsConfig(Integer.parseInt(split.getTextContent()));
            }
            else {
                splitConfig = new SplitBySizeConfig(Integer.parseInt(split.getTextContent()));
            }
        }
        if(cryptNode != null) {
            cryptConfig = new CryptConfig(cryptKey);
        }
        if(compressNode != null) {
            compressConfig = new CompressConfig();
        }
    }

    @Override
    public void run() {
        this.setProgress(new ErrorProgress(null));
    }
}
