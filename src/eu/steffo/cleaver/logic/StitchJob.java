package eu.steffo.cleaver.logic;

import eu.steffo.cleaver.errors.ChpFileError;
import eu.steffo.cleaver.errors.ProgrammingError;
import eu.steffo.cleaver.logic.compress.CompressConfig;
import eu.steffo.cleaver.logic.crypt.CryptConfig;
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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class StitchJob extends Job {

    public StitchJob(File file, String cryptKey) throws ChpFileError, ProgrammingError {
        super(file);
        parseChp(openChp(file), cryptKey);
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

    protected final void parseChp(Document doc, String cryptKey) {
        Element root = doc.getDocumentElement();

        NodeList splits = root.getElementsByTagName("Split");
        NodeList crypts = root.getElementsByTagName("Crypt");
        NodeList compresses = root.getElementsByTagName("Compress");

        Node splitNode = splits.item(0);
        Node cryptNode = crypts.item(0);
        Node compressNode = compresses.item(0);

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
    public String getType() {
        return "Stitch";
    }

    @Override
    public void run() {

    }
}
