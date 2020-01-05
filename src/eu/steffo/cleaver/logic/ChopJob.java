package eu.steffo.cleaver.logic;

import eu.steffo.cleaver.errors.ChpFileError;
import eu.steffo.cleaver.errors.ProgrammingError;
import eu.steffo.cleaver.logic.compress.CompressConfig;
import eu.steffo.cleaver.logic.crypt.CryptConfig;
import eu.steffo.cleaver.logic.crypt.CryptOutputStream;
import eu.steffo.cleaver.logic.progress.ErrorProgress;
import eu.steffo.cleaver.logic.progress.FinishedProgress;
import eu.steffo.cleaver.logic.progress.WorkingProgress;
import eu.steffo.cleaver.logic.split.SplitByPartsConfig;
import eu.steffo.cleaver.logic.split.SplitBySizeConfig;
import eu.steffo.cleaver.logic.split.SplitConfig;
import eu.steffo.cleaver.logic.split.SplitFileOutputStream;
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

    public ChopJob(File file, SplitConfig splitConfig, CryptConfig cryptConfig, CompressConfig compressConfig, Runnable onProgressChange) {
        super(onProgressChange);
        this.file = file;
        this.splitConfig = splitConfig;
        this.cryptConfig = cryptConfig;
        this.compressConfig = compressConfig;
    }

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
            long partSize;

            if(splitConfig instanceof SplitBySizeConfig) {
                partSize = ((SplitBySizeConfig)splitConfig).getSize();
            }
            else if(splitConfig instanceof SplitByPartsConfig) {
                partSize = (long)Math.ceil((double)file.length() / (double)(((SplitByPartsConfig)splitConfig).getParts()));
            }
            else {
                partSize = file.length();
            }
            OutputStream outputStream = new SplitFileOutputStream(file.getAbsolutePath(), partSize);

            if(compressConfig != null) {
                outputStream = new DeflaterOutputStream(outputStream);
            }

            if(cryptConfig != null) {
                outputStream = new CryptOutputStream(outputStream);
            }

            //Create the .chp file
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder;
            try {
                builder = factory.newDocumentBuilder();
            } catch (ParserConfigurationException e) {
                throw new ProgrammingError();
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

            //Actually run the job
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
            this.setProgress(new ErrorProgress(e));
        }
    }
}
