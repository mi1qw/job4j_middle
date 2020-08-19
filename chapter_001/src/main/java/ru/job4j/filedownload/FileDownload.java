package ru.job4j.filedownload;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

import static java.lang.Math.round;

public class FileDownload {
    public static final Logger LOGGER = LoggerFactory.getLogger(FileDownload.class);

    protected FileDownload() {
        throw new IllegalStateException("Utility class");
    }

    public static void main(final String[] args) throws Exception {
        String file = "https://raw.githubusercontent.com/peterarsentev/course_test/master/pom.xml";
        int byteSec = 200;
        File pom = File.createTempFile("temp", ".xml");
        try (BufferedInputStream in = new BufferedInputStream(new URL(file).openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(pom)) {
            byte[] dataBuffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
                Thread.sleep(kBsPause(byteSec, bytesRead));
            }
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public static long kBsPause(final double byteMsec, final int bytes) {
        return round((double) bytes / byteMsec);
    }
}
