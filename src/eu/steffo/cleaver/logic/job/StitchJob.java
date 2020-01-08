package eu.steffo.cleaver.logic.job;

import eu.steffo.cleaver.errors.ChpFileError;
import eu.steffo.cleaver.errors.ProgrammingError;
import eu.steffo.cleaver.logic.config.*;
import eu.steffo.cleaver.logic.stream.input.*;
import eu.steffo.cleaver.logic.progress.ErrorProgress;
import eu.steffo.cleaver.logic.progress.FinishedProgress;
import eu.steffo.cleaver.logic.progress.Progress;
import eu.steffo.cleaver.logic.progress.WorkingProgress;

import eu.steffo.cleaver.logic.stream.input.CleaverSplitFileInputStream;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.zip.InflaterInputStream;


/**
 * A {@link Job} that converts <i>chopped</i> (*.chp + *.cXX) files back into regular files.
 */
public class StitchJob extends Job {
    private File resultFile;
    private File chpFile;
    private ISplitConfig splitConfig = null;
    private ICryptConfig cryptConfig = null;
    private ICompressConfig compressConfig = null;

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
        this.chpFile = chpFile;
        parseChp(getChpFileDocument(chpFile), cryptKey);
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
    public ISplitConfig getSplitConfig() {
        return splitConfig;
    }

    @Override
    public ICryptConfig getCryptConfig() {
        return cryptConfig;
    }

    @Override
    public ICompressConfig getCompressConfig() {
        return compressConfig;
    }

    /**
     * Instantiate a new {@link Document} based on the contents of a *.chp file.
     * @param file The *.chp {@link File} to create the document from.
     * @return The created {@link Document}.
     * @throws ChpFileError If the .chp does not exist, or is corrupt.
     * @throws ProgrammingError It should never happen, as the parser should be already configured correctly.
     */
    protected static Document getChpFileDocument(File file) throws ChpFileError, ProgrammingError {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new ProgrammingError("Parser configuration error in the ChopJob.");
        }
        Document doc;
        try {
            doc = builder.parse(file);
        } catch (SAXException e) {
            throw new ChpFileError("The .chp is corrupt!");
        } catch (IOException e) {
            throw new ChpFileError("The .chp file does not exist anymore!");
        }
        return doc;
    }

    /**
     * Read a {@link Element} and create a {@link ICleaverInputStream} based on it.
     * @param element The {@link Element} to be read.
     * @param cryptKey The encryption key to use in case a {@link CleaverCryptInputStream} is created.
     * @throws ChpFileError If there's an error while parsing the node.
     */
    protected ICleaverInputStream parseNode(Element element, String cryptKey) throws ChpFileError {
        String name = element.getTagName();

        ICleaverInputStream result;

        if(name.equals("Crypt")) {
            //TODO
        }
        else if(name.equals("Deflate")) {
            //TODO
        }
        else if(name.equals("Fork")) {
            //TODO
        }
        else if(name.equals("Simple")) {
            //TODO
        }
        else if(name.equals("Split")) {
            //TODO
        }
        else {
            throw new ChpFileError("Unknown tag: " + name);
        }

        return result;
    }

    @Override
    public void run() {
        //TODO
    }
}
