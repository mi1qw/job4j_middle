package ru.job4j.visibilityparsefile;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * The type Parse file.
 */
public class ParseFile {
    public static final Logger LOGGER = LoggerFactory.getLogger(ParseFile.class);
    private File file;

    /**
     * Sets file.
     *
     * @param f the f
     */
    public synchronized void setFile(final File f) {
        file = f;
    }

    /**
     * Gets file.
     *
     * @return the file
     */
    public synchronized File getFile() {
        return file;
    }

    /**
     * Gets content.
     *
     * @return the content
     * @throws IOException the io exception
     */
    public synchronized String getContent() {
        return getContent(true);
    }

    /**
     * Gets content without unicode.
     *
     * @return the content without unicode
     * @throws IOException the io exception
     */
    public synchronized String getContentWithoutUnicode() {
        return getContent(false);
    }

    @NotNull
    private String getContent(final boolean unicode) {
        StringBuilder output = new StringBuilder();
        int data;
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

    /**
     * Save content.
     *
     * @param content the content
     * @throws IOException the io exception
     */
    public synchronized void saveContent(@NotNull final String content) {
        try (BufferedOutputStream o = new BufferedOutputStream(new FileOutputStream(file))) {
            o.write(content.getBytes());
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}
