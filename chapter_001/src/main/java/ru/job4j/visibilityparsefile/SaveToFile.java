package ru.job4j.visibilityparsefile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SaveToFile {
    public static final Logger LOGGER = LoggerFactory.getLogger(SaveToFile.class);
    private File file;

    public SaveToFile(final File file) {
        this.file = file;
    }

    /**
     * Save content.
     *
     * @param content the content
     */
    public synchronized void saveContent(final String content) {
        try (BufferedWriter o = new BufferedWriter(new FileWriter(file))) {
            o.write(content);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}
