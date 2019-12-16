package eu.steffo.cleaver.logic;

import eu.steffo.cleaver.errors.ChpFileError;
import eu.steffo.cleaver.errors.ProgrammingError;
import eu.steffo.cleaver.logic.compress.CompressConfig;
import eu.steffo.cleaver.logic.crypt.CryptConfig;
import eu.steffo.cleaver.logic.progress.ErrorProgress;
import eu.steffo.cleaver.logic.progress.FinishedProgress;
import eu.steffo.cleaver.logic.progress.WorkingProgress;
import eu.steffo.cleaver.logic.split.SplitByPartsConfig;
import eu.steffo.cleaver.logic.split.SplitBySizeConfig;
import eu.steffo.cleaver.logic.split.SplitConfig;
import eu.steffo.cleaver.logic.split.SplitFileOutputStream;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.zip.DeflaterOutputStream;

public class ChopJob extends Job {

    public ChopJob(File file, SplitConfig splitConfig, CryptConfig cryptConfig, CompressConfig compressConfig) {
        this(file, null, splitConfig, cryptConfig, compressConfig);
    }

    public ChopJob(File file, Runnable swingCallLaterOnProgressChanges, SplitConfig splitConfig, CryptConfig cryptConfig, CompressConfig compressConfig) {
        super(file, swingCallLaterOnProgressChanges);
        this.splitConfig = splitConfig;
        this.cryptConfig = cryptConfig;
        this.compressConfig = compressConfig;
    }

    @Override
    public String getType() {
        return "Chop";
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
                partSize = ((SplitByPartsConfig)splitConfig).getParts();
            }
            else {
                partSize = file.length();
            }
            OutputStream outputStream = new SplitFileOutputStream(file.getName(), partSize);

            if(compressConfig != null) {
                outputStream = new DeflaterOutputStream(outputStream);
            }

            if(cryptConfig != null) {
                //TODO
            }

            //Create the .chp file
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            try {
                builder = factory.newDocumentBuilder();
            } catch (ParserConfigurationException e) {
                throw new ProgrammingError();
            }
            Document doc = builder.newDocument();
            Element root = doc.createElement("Cleaver");
            doc.appendChild(root);

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
            StreamResult result = new StreamResult(String.format("%s.chp", file.getName()));
            transformer.transform(source, result);

            //Actually run the job
            int bytesUntilNextUpdate = 1024;
            this.setProgress(new WorkingProgress());

            int i;
            while((i = inputStream.read()) != -1) {
                outputStream.write(i);
                bytesUntilNextUpdate -= 1;
                if(bytesUntilNextUpdate <= 0) {
                    this.setProgress(new WorkingProgress((float)file.length() / (float)partSize));
                    bytesUntilNextUpdate = 1024;
                }
            }

            inputStream.close();
            outputStream.close();

            this.setProgress(new FinishedProgress());
        } catch (Throwable e) {
            this.setProgress(new ErrorProgress(e));
        }
    }

    public SplitConfig getSplitConfig() {
        return splitConfig;
    }

    public CryptConfig getCryptConfig() {
        return cryptConfig;
    }

    public CompressConfig getCompressConfig() {
        return compressConfig;
    }
}
