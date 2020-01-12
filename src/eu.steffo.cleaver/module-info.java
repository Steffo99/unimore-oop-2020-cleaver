import org.w3c.dom.Element;

import java.io.File;

/**
 * <p>
 * A file compression, encryption and splitting utility in Java.
 * </p>
 * <p>
 * It includes a easy to use {@link javax.swing} GUI.
 * </p>
 * <p>
 * <img src="doc-files/main.png" alt="">
 * </p>
 * <p>
 * It can <b>chop</b> regular files into a *.chp metadata file and multiple *.cXX (where XX is a number from 1 to {@link Integer#MAX_VALUE}) data files, which
 * can then be <b>stitch</b>ed together to recreate the original file.
 * </p>
 * <p>
 * Multiple options can be selected while chopping files:
 * </p>
 * <ul>
 *     <li>They can be split into multiple parts having a specific file size ("Split")</li>
 *     <li>They can be split into a specific number of parts having the same file size ("Fork")</li>
 *     <li>They can be encrypted with a password ("Crypt")</li>
 *     <li>They can be compressed to use less hard drive space ("Deflate")</li>
 * </ul>
 * <p>
 * Multiple files can be chopped or stitched at a time: each operation is run in a separate {@link Thread} called {@link eu.steffo.cleaver.logic.job.Job Job}
 * that performs the task independently from the other Jobs.
 * </p>
 * <p>
 * *.chp files are documented {@link eu.steffo.cleaver.logic.job.ChopJob here} and
 * {@link eu.steffo.cleaver.logic.stream.input.ICleaverInputStream#fromElement(Element, File, String) here}.
 * </p>
 * @author Stefano Pigozzi
 */
open module eu.steffo.cleaver {
    requires java.desktop;
    requires java.xml;

    exports eu.steffo.cleaver;
    exports eu.steffo.cleaver.errors;
    exports eu.steffo.cleaver.gui;
    exports eu.steffo.cleaver.gui.panels;
    exports eu.steffo.cleaver.gui.panels.rows;
    exports eu.steffo.cleaver.gui.panels.rows.option;
    exports eu.steffo.cleaver.logic.config;
    exports eu.steffo.cleaver.logic.job;
    exports eu.steffo.cleaver.logic.progress;
    exports eu.steffo.cleaver.logic.stream;
    exports eu.steffo.cleaver.logic.stream.input;
    exports eu.steffo.cleaver.logic.stream.output;
    exports eu.steffo.cleaver.logic.utils;
}
