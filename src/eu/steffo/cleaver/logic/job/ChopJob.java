package eu.steffo.cleaver.logic.job;

import eu.steffo.cleaver.errors.ProgrammingError;
import eu.steffo.cleaver.logic.config.*;
import eu.steffo.cleaver.logic.stream.output.*;
import eu.steffo.cleaver.logic.progress.ErrorProgress;
import eu.steffo.cleaver.logic.progress.FinishedProgress;
import eu.steffo.cleaver.logic.progress.Progress;
import eu.steffo.cleaver.logic.progress.WorkingProgress;
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
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * A {@link Job} that converts regular files into <i>chopped</i> (*.chp + *.cXX) files.
 */
public class ChopJob extends Job {

    private final File file;
    private final ISplitConfig splitConfig;
    private final ICryptConfig cryptConfig;
    private final ICompressConfig compressConfig;

    /**
     * Create a new ChopJob (with progress updates support).
     * @param file The file to be chopped.
     * @param splitConfig The configuration for the Split step.
     * @param cryptConfig The configuration for the Crypt step.
     * @param compressConfig The configuration for the Compress step.
     * @param onProgressChange A {@link Runnable} that should be invoked when {@link #setProgress(Progress)} is called.
     * @see Job#Job(Runnable)
     */
    public ChopJob(File file, ISplitConfig splitConfig, ICryptConfig cryptConfig, ICompressConfig compressConfig, Runnable onProgressChange) {
        super(onProgressChange);
        this.file = file.getAbsoluteFile();
        this.splitConfig = splitConfig;
        this.cryptConfig = cryptConfig;
        this.compressConfig = compressConfig;
    }

    /**
     * Create a new ChopJob (without progress updates support).
     * @param file The file to be chopped.
     * @param splitConfig The configuration for the Split step.
     * @param cryptConfig The configuration for the Crypt step.
     * @param compressConfig The configuration for the Compress step.
     * @see ChopJob#ChopJob(File, ISplitConfig, ICryptConfig, ICompressConfig, Runnable)
     * @see Job#Job()
     */
    public ChopJob(File file, ISplitConfig splitConfig, ICryptConfig cryptConfig, ICompressConfig compressConfig) {
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

    @Override
    public void run() {
        try {
            InputStream inputStream = new FileInputStream(file);
            OutputStream outputStream;

            if(splitConfig instanceof SizeConfig) {
                outputStream = new CleaverSplitFileOutputStream(file, ((SizeConfig)splitConfig).getPartSize());
            }
            else if(splitConfig instanceof PartsConfig) {
                outputStream = new CleaverForkFileOutputStream(file, ((PartsConfig)splitConfig).getPartCount());
            }
            else {
                outputStream = new CleaverSimpleFileOutputStream(file);
            }

            if(compressConfig instanceof DeflateConfig) {
                outputStream = new CleaverDeflaterOutputStream(outputStream);
            }

            if(cryptConfig instanceof PasswordConfig) {
                outputStream = new CleaverCryptOutputStream(outputStream, ((PasswordConfig)cryptConfig).getKey());
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

            root.appendChild(((ICleaverOutputStream)outputStream).toElement(doc));

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(String.format("%s.chp", file.getAbsolutePath()));
            transformer.transform(source, result);

            this.setProgress(new FinishedProgress());
        } catch (Throwable e) {
            e.printStackTrace();
            this.setProgress(new ErrorProgress(e));
        }
    }
}
