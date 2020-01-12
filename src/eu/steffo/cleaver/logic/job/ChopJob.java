package eu.steffo.cleaver.logic.job;

import eu.steffo.cleaver.errors.*;
import eu.steffo.cleaver.logic.config.*;
import eu.steffo.cleaver.logic.progress.*;
import eu.steffo.cleaver.logic.stream.output.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * A {@link Job} to convert a file into one or more <i>chopped</i> (*.chp + *.cXX) files.
 *
 * The conversion is done in steps, which may be skipped if the required configuration is {@code null}:
 * <ol>
 *     <li><b>Compress</b> (if {@link #compressConfig} is not {@code null})</li>
 *     <li><b>Crypt</b> (if {@link #cryptConfig} is not {@code null})</li>
 *     <li><b>Split</b> (if {@link #splitConfig} is not {@code null})</li>
 *     <li>*.chp file creation</li>
 * </ol>
 *
 * @see StitchJob
 */
public class ChopJob extends Job {

    /**
     * The file to <i>chop</i>.
     */
    private final File fileToChop;

    /**
     * The {@link IConfig} for the <b>Crypt</b> step.
     */
    private final ICryptConfig cryptConfig;

    /**
     * The {@link IConfig} for the <b>Compress</b> step.
     */
    private final ICompressConfig compressConfig;


    /**
     * The {@link IConfig} for the <b>Split</b> step.
     */
    private final ISplitConfig splitConfig;

    /**
     * The size in bytes of the {@link BufferedInputStream} wrapping the {@link FileInputStream} created when {@link #run()} is called to read the data from the
     * file to be chopped.
     *
     * The same value is used for a temporary array in the {@link #run()} method where bytes are stored between being read from the input file and being written
     * through a {@link ICleaverOutputStream}; after that amount of bytes are written, {@link #getProgress()} is called, updating the
     * {@link Progress} of this Job.
     */
    private static final int BUFFER_SIZE = 8192;

    /**
     * Create a new ChopJob (with progress updates support).
     * @param fileToChop The file to <i>chop</i>.
     * @param compressConfig The {@link IConfig} for the <b>Compress</b> step.
     * @param cryptConfig The {@link IConfig} for the <b>Crypt</b> step.
     * @param splitConfig The {@link IConfig} for the <b>Split</b> step.
     * @param onProgressChange  A {@link Runnable} that will be invoked on the GUI {@link Thread}
     *                          (with {@link javax.swing.SwingUtilities#invokeLater(Runnable)}) every time {@link #setProgress(Progress)} is called.
     * @see Job#Job(Runnable)
     */
    public ChopJob(File fileToChop, ICompressConfig compressConfig, ICryptConfig cryptConfig, ISplitConfig splitConfig, Runnable onProgressChange) {
        super(onProgressChange);
        this.fileToChop = fileToChop.getAbsoluteFile();
        this.compressConfig = compressConfig;
        this.cryptConfig = cryptConfig;
        this.splitConfig = splitConfig;
    }

    /**
     * Create a new ChopJob (without progress updates support).
     * @param fileToChop The file to <i>chop</i>.
     * @param compressConfig The {@link IConfig} for the <b>Compress</b> step.
     * @param cryptConfig The {@link IConfig} for the <b>Crypt</b> step.
     * @param splitConfig The {@link IConfig} for the <b>Split</b> step.
     * @see ChopJob#ChopJob(File, ICompressConfig, ICryptConfig, ISplitConfig, Runnable)
     * @see Job#Job()
     */
    public ChopJob(File fileToChop, ICompressConfig compressConfig, ICryptConfig cryptConfig, ISplitConfig splitConfig) {
        this(fileToChop, compressConfig, cryptConfig, splitConfig, null);
    }

    @Override
    public String getTypeString() {
        return "Chop";
    }

    @Override
    public String getFileString() {
        return fileToChop.toString();
    }

    @Override
    public String getOperationsString() {
        StringBuilder s = new StringBuilder();

        if(compressConfig != null) {
            s.append(compressConfig.toString());
            if(cryptConfig != null || splitConfig != null) {
                s.append(" → ");
            }
        }

        if(cryptConfig != null) {
            s.append(cryptConfig.toString());
            if(splitConfig != null) {
                s.append(" → ");
            }
        }

        if(splitConfig != null) {
            s.append(splitConfig.toString());
        }

        return s.toString();
    }

    /**
     * Create a {@link OutputStream} based on the {@link #splitConfig} of this ChopJob.
     *
     * The {@link OutputStream} will be the <i>sink</i> of the stream chain created in {@link #run()}.
     *
     * @return The created {@link OutputStream}.
     * @throws FileNotFoundException If one or more files cannot be created (for example, a directory with the same name is present).
     * @see CleaverSplitFileOutputStream
     * @see CleaverForkFileOutputStream
     * @see CleaverSimpleFileOutputStream
     */
    private OutputStream createSplitOutputStream() throws FileNotFoundException {
        if(splitConfig instanceof SizeConfig) {
            return new CleaverSplitFileOutputStream(fileToChop, ((SizeConfig)splitConfig).getSize());
        }
        else if(splitConfig instanceof PartsConfig) {
            return new CleaverForkFileOutputStream(fileToChop, ((PartsConfig)splitConfig).getPartCount());
        }
        return new CleaverSimpleFileOutputStream(fileToChop);
    }

    /**
     * Create a {@link OutputStream} based on the {@link #compressConfig} of this ChopJob.
     *
     * The created {@link OutputStream} will <i>wrap</i> the {@link OutputStream} passed as parameter, creating a chain of streams.
     *
     * @param sourceOutputStream The {@link OutputStream} that should be wrapped.
     * @return The created {@link OutputStream}, wrapping the one that was passed as parameter.
     * @see CleaverDeflateOutputStream
     */
    private OutputStream createCompressOutputStream(OutputStream sourceOutputStream) {
        if(compressConfig instanceof DeflateConfig) {
            return new CleaverDeflateOutputStream(sourceOutputStream);
        }
        return sourceOutputStream;
    }

    /**
     * Create a {@link OutputStream} based on the {@link #cryptConfig} of this ChopJob.
     *
     * The created {@link OutputStream} will <i>wrap</i> the {@link OutputStream} passed as parameter, creating a chain of streams.
     *
     * @param sourceOutputStream The {@link OutputStream} that should be wrapped.
     * @return The created {@link OutputStream}, wrapping the one that was passed as parameter.
     * @see CleaverCryptOutputStream
     */
    private OutputStream createCryptOutputStream(OutputStream sourceOutputStream) {
        if(cryptConfig instanceof PasswordConfig) {
            return new CleaverCryptOutputStream(sourceOutputStream, ((PasswordConfig)cryptConfig).getPassword().toCharArray());
        }
        return sourceOutputStream;
    }

    /**
     * <p>
     * Generate the *.chp file for this job by calling the {@link ICleaverOutputStream#toElement(Document)} method of the passed {@link OutputStream} and by
     * writing the results on a file with a {@link Transformer}.
     * </p>
     * <p>
     * *.chp files are special XML files that contain information about the stream chain used to produce the *.cXX files.
     * </p>
     * <p>
     * Every *.chp file has a {@literal <Cleaver>} element as root containing all the information required to reconstruct the original file.
     * </p>
     * <p>
     * Every element below the root represents either a {@link FilterOutputStream} that wraps the stream represented by its child, or, if the child node is
     * {@literal <OriginalFile>}, a {@link OutputStream} reading from the {@link File} having the {@link Element#getTextContent() text content}
     * of the {@literal <OriginalFile>} element as name.
     * </p>
     * <p>
     * Possible tags representing {@link FilterOutputStream FilterOutputStreams} are:
     * </p>
     * <ul>
     *     <li>{@literal <Crypt>}, representing a {@link CleaverCryptOutputStream};</li>
     *     <li>{@literal <Deflate>}, representing a {@link CleaverDeflateOutputStream}.</li>
     * </ul>
     * <p>
     * Possible tags representing {@link OutputStream simple OutputStreams} are:
     * </p>
     * <ul>
     *     <li>{@literal <Simple>}, representing a {@link CleaverSimpleFileOutputStream};</li>
     *     <li>{@literal <Split>}, representing a {@link CleaverSplitFileOutputStream};</li>
     *     <li>{@literal <Fork>}, representing a {@link CleaverForkFileOutputStream}.</li>
     * </ul>
     * <p>
     * Elements may include attributes that useful or required to reconstruct the original file: for example, the {@literal <Crypt>} tag includes the value of
     * the salt used to generate the AES key and the value of the initialization vector used to encrypt the stream.
     * </p>
     * <h4>Example</h4>
     * <blockquote>
     *     {@literal <?xml version="1.0" encoding="UTF-8" standalone="no"?><Cleaver><Crypt algorithm="AES" iteration-count="65535" iv="-10,6,-6,63,-68,-106,-102,-12,59,-58,-123,-76,-16,-29,-75,-67," key-algorithm="PBKDF2WithHmacSHA512" key-length="256" mode-of-operation="CFB8" padding="NoPadding" salt="-60,-84,108,93,100,5,63,44,"><Deflate><Split part-size="16384" parts="4"><OriginalFile>AllThreeExample.png</OriginalFile></Split></Deflate></Crypt></Cleaver>}
     * </blockquote>
     *
     * @param outputStream The {@link OutputStream} to create the *.chp file for.
     * @see eu.steffo.cleaver.logic.stream.input.ICleaverInputStream#fromElement(Element, File, String)
     * */
    protected void createChpFile(OutputStream outputStream) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new ProgrammingError("Parser configuration error in the ChopJob.");
        }
        Document doc = builder.newDocument();
        Element root = doc.createElement("Cleaver");
        doc.appendChild(root);

        root.appendChild(((ICleaverOutputStream)outputStream).toElement(doc));

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer;
        try {
            transformer = transformerFactory.newTransformer();
        } catch (TransformerConfigurationException e) {
            throw new ProgrammingError(e.toString());
        }
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(String.format("%s.chp", fileToChop.getAbsolutePath()));
        try {
            transformer.transform(source, result);
        } catch (TransformerException e) {
            throw new ProgrammingError(e.toString());
        }
    }

    @Override
    public void run() {
        try {
            InputStream inputStream = new BufferedInputStream(new FileInputStream(fileToChop));
            OutputStream outputStream = createCryptOutputStream(createCompressOutputStream(createSplitOutputStream()));

            this.setProgress(new WorkingProgress());

            byte[] buffer = new byte[BUFFER_SIZE];
            int readBytes = inputStream.read(buffer);
            while(readBytes != -1) {
                outputStream.write(buffer, 0, readBytes);
                this.setProgress(new WorkingProgress((float)(fileToChop.length() - inputStream.available()) / (float)fileToChop.length()));
                readBytes = inputStream.read(buffer);
            }

            inputStream.close();
            outputStream.close();

            createChpFile(outputStream);

            this.setProgress(new FinishedProgress());
        } catch (Throwable e) {
            e.printStackTrace();
            this.setProgress(new ErrorProgress(e));
        }
    }
}
