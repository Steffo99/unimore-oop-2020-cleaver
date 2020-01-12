package eu.steffo.cleaver.logic.stream.input;

import eu.steffo.cleaver.errors.ChpFileError;
import eu.steffo.cleaver.logic.stream.ICleaverStream;
import eu.steffo.cleaver.logic.utils.SaltSerializer;
import org.w3c.dom.Element;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * A {@link ICleaverStream} that is also a {@link InputStream} (a stream you can read bytes from).
 * @see eu.steffo.cleaver.logic.job.StitchJob
 */
public interface ICleaverInputStream extends ICleaverStream {
    /**
     * <p>
     * Construct a ICleaverInputStream from a XML tag.
     * </p>
     * <p>
     * Based on the input {@link Element#getTagName() tagName}, a different ICleaverInputStream is created:
     * </p>
     * <ul>
     *     <li>{@literal <Crypt>} tags create a {@link CleaverCryptInputStream} with the <i>salt</i> and <i>iv</i> respectively
     *     {@link SaltSerializer#deserialize(String) deserialized} from the <code>salt</code> and <code>iv</code> attributes, wrapping the ICleaverInputStream
     *     recursively created from its child;</li>
     *     <li>{@literal <Deflate>} tags create a {@link CleaverDeflateInputStream} wrapping the ICleaverInputStream recursively created from its child;</li>
     *     <li>{@literal <Simple>} tags create a {@link CleaverSimpleFileInputStream} with the {@link File} specified in the child {@literal <OriginalFile>}
     *     tag;</li>
     *     <li>{@literal <Split>} tags create a {@link CleaverSplitFileInputStream} with the {@link File} specified in the child {@literal <OriginalFile>}
     *     tag and the part size specified in the <code>part-size</code> attribute;</li>
     *     <li>{@literal <Fork>}, representing a {@link CleaverForkFileInputStream} with the {@link File} specified in the child {@literal <OriginalFile>}
     *     tag and the number of parts specified in the <code>parts</code> attribute;</li>
     * </ul>
     *
     * @param element The XML {@link Element} to construct the ICleaverInputStream from.
     * @param chpFileDirectory The directory in which the *.chp file is located.
     * @param password The password to use in case &lt;Crypt&gt; tags are present.
     * @throws ChpFileError If an unknown tag is encountered.
     * @throws IOException If something goes wrong while opening the streams ({@link java.io.FileNotFoundException missing files}, insufficient permissions,
     *                     ...)
     * @return The created ICleaverInputStream.
     * @see eu.steffo.cleaver.logic.job.ChopJob
     * @see SaltSerializer
     */
    static InputStream fromElement(Element element, File chpFileDirectory, String password) throws ChpFileError, IOException {
        String tagName = element.getTagName();
        switch (tagName) {
            case "Crypt":
                InputStream cryptChild = fromElement((Element)element.getFirstChild(), chpFileDirectory, password);
                byte[] salt = SaltSerializer.deserialize(element.getAttribute("salt"));
                byte[] iv = SaltSerializer.deserialize(element.getAttribute("iv"));
                return new CleaverCryptInputStream(cryptChild, password.toCharArray(), salt, iv);
            case "Deflate":
                InputStream deflateChild = fromElement((Element)element.getFirstChild(), chpFileDirectory, password);
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
