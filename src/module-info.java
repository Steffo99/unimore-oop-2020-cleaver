/**
 * A file compression, encryption and splitting utility in Java.
 *
 * It includes a easy to use {@link javax.swing} GUI.
 *
 * <p><img src="doc-files/main.png" alt=""></p>
 *
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
