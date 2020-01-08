package eu.steffo.cleaver.logic.stream.input;

import eu.steffo.cleaver.errors.ChpFileError;
import eu.steffo.cleaver.logic.stream.ICleaverStream;
import eu.steffo.cleaver.logic.utils.SaltSerializer;
import org.w3c.dom.Element;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public interface ICleaverInputStream extends ICleaverStream {
    /**
     * Construct a ICleaverInputStream from a XML tag.
     * @param element The XML tag.
     * @param chpFileDirectory The directory in which the *.chp file is located.
     * @param key The key to use in case &lt;Crypt&gt; tags are present.
     */
    static InputStream fromElement(Element element, File chpFileDirectory, String key) throws ChpFileError, IOException {
        String tagName = element.getTagName();
        switch (tagName) {
            case "Crypt":
                InputStream cryptChild = fromElement((Element)element.getFirstChild(), chpFileDirectory, key);
                byte[] salt = SaltSerializer.deserialize(element.getAttribute("salt"));
                byte[] iv = SaltSerializer.deserialize(element.getAttribute("iv"));
                return new CleaverCryptInputStream(cryptChild, key.toCharArray(), salt, iv);
            case "Deflate":
                InputStream deflateChild = fromElement((Element)element.getFirstChild(), chpFileDirectory, key);
                return new CleaverDeflateInputStream(deflateChild);
            case "Fork":
                int parts = Integer.parseInt(element.getAttribute("parts"));
                return new CleaverForkFileInputStream(new File(chpFileDirectory, element.getFirstChild().getTextContent()), parts);
            case "Simple":
                return new CleaverSimpleFileInputStream(new File(chpFileDirectory, element.getFirstChild().getTextContent()));
            case "Split":
                long partSize = Long.parseLong(element.getAttribute("part-size"));
                return new CleaverSplitFileInputStream(new File(chpFileDirectory, element.getFirstChild().getTextContent()), partSize);
            default:
                throw new ChpFileError("Unknown tag: " + tagName);
        }
    }
}
