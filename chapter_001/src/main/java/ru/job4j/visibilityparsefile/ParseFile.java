package ru.job4j.visibilityparsefile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * The type Parse file.
 */
public class ParseFile {
    public static final Logger LOGGER = LoggerFactory.getLogger(ParseFile.class);
    private File file;

    public ParseFile(final File file) {
        this.file = file;
    }

    /**
     * Gets content.
     *
     * @return the content
     */
    public synchronized String getContent(final boolean... unicod) {
        boolean unicode = false;
        int data;
        StringBuilder output = new StringBuilder();
        if (unicod.length != 0 && unicod[0]) {
            unicode = true;
        }
        try (BufferedInputStream i = new BufferedInputStream(new FileInputStream(file))) {
            while ((data = i.read()) > 0) {
                if (unicode || data < 0x80) {
                    output.append((char) data);
                }
            }
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return output.toString();
    }
}
