package eu.steffo.cleaver.logic.job;

import eu.steffo.cleaver.errors.ProgrammingError;
import eu.steffo.cleaver.logic.config.CompressConfig;
import eu.steffo.cleaver.logic.config.CryptConfig;
import eu.steffo.cleaver.logic.stream.output.CleaverCryptOutputStream;
import eu.steffo.cleaver.logic.progress.ErrorProgress;
import eu.steffo.cleaver.logic.progress.FinishedProgress;
import eu.steffo.cleaver.logic.progress.Progress;
import eu.steffo.cleaver.logic.progress.WorkingProgress;
import eu.steffo.cleaver.logic.config.SplitConfig;
import eu.steffo.cleaver.logic.stream.output.CleaverSplitFileOutputStream;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.zip.DeflaterOutputStream;

/**
 * A {@link Job} that converts regular files into <i>chopped</i> (*.chp + *.cXX) files.
 */
public class ChopJob extends Job {

    private final File file;
    private final SplitConfig splitConfig;
    private final CryptConfig cryptConfig;
    private final CompressConfig compressConfig;

    /**
     * Create a new ChopJob (with progress updates support).
     * @param file The file to be chopped.
     * @param splitConfig The configuration for the split step.
     * @param cryptConfig The configuration for the crypt step.
     * @param compressConfig The configuration for the compress step.
     * @param onProgressChange A {@link Runnable} that should be invoked when {@link #setProgress(Progress)} is called.
     * @see Job#Job(Runnable)
     */
    public ChopJob(File file, SplitConfig splitConfig, CryptConfig cryptConfig, CompressConfig compressConfig, Runnable onProgressChange) {
        super(onProgressChange);
        this.file = file;
        this.splitConfig = splitConfig;
        this.cryptConfig = cryptConfig;
        this.compressConfig = compressConfig;
    }

    /**
     * Create a new ChopJob (without progress updates support).
     * @param file The file to be chopped.
     * @param splitConfig The configuration for the split step.
     * @param cryptConfig The configuration for the crypt step.
     * @param compressConfig The configuration for the compress step.
     * @see ChopJob#ChopJob(File, SplitConfig, CryptConfig, CompressConfig, Runnable)
     * @see Job#Job()
     */
    public ChopJob(File file, SplitConfig splitConfig, CryptConfig cryptConfig, CompressConfig compressConfig) {
        this(file, splitConfig, cryptConfig, compressConfig, null);
    }

    @Override
    public String getType() {
        return "Chop";
    }

    @Override
    public File getFile() {
        return file;
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

    @Override
    public void run() {
        try {
            InputStream inputStream = new FileInputStream(file);
            OutputStream outputStream;

            if(splitConfig != null) {
                outputStream = new CleaverSplitFileOutputStream(file.getAbsolutePath(), splitConfig.getPartSize());
            }
            else {
                outputStream = new FileOutputStream(String.format("%s.c0", file.getAbsolutePath()));
            }

            if(compressConfig != null) {
                outputStream = new DeflaterOutputStream(outputStream);
            }

            if(cryptConfig != null) {
                outputStream = new CleaverCryptOutputStream(outputStream, cryptConfig.getKey());
            }

            //Create the .chp file
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

            Element original = doc.createElement("Original");
            original.setTextContent(file.getName());
            root.appendChild(original);

            if(splitConfig != null) {
                root.appendChild(splitConfig.toElement(doc));
            }
            if(compressConfig != null) {
                root.appendChild(compressConfig.toElement(doc));
            }
            if(cryptConfig != null) {
                root.appendChild(cryptConfig.toElement(doc));
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(String.format("%s.chp", file.getAbsolutePath()));
            transformer.transform(source, result);

            //Pipe everything to the output
            int bytesUntilNextUpdate = 2048;
            this.setProgress(new WorkingProgress());

            int i;
            while((i = inputStream.read()) != -1) {
                outputStream.write(i);
                bytesUntilNextUpdate -= 1;
                if(bytesUntilNextUpdate <= 0) {
                    this.setProgress(new WorkingProgress((float)(file.length() - inputStream.available()) / (float)file.length()));
                    bytesUntilNextUpdate = 2048;
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
