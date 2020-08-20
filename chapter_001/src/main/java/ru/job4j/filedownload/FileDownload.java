package ru.job4j.filedownload;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import static java.lang.Math.round;

public class FileDownload {
    public static final Logger LOGGER = LoggerFactory.getLogger(FileDownload.class);

    protected FileDownload() {
        throw new IllegalStateException("Utility class");
    }

    public static void main(final String[] args) throws Exception {
        String file = "https://raw.githubusercontent.com/peterarsentev/course_test/master/pom.xml";
        int kbyteSec = 200;
        FutureTask<LoadItem> task = new FutureTask<>(new LoadItem(kbyteSec, file));
        new Thread(task).start();
        System.out.println(task.get());
    }

    private static class LoadItem implements Callable<LoadItem> {
        private double byteMsec;
        private String file;
        private long time = 0;
        private int totalbyte = 0;
        private String name;

        LoadItem(final int byteSec, final String file) {
            this.byteMsec = byteSec * 1.24;
            this.file = file;
        }

        @Override
        public String toString() {
            return String.format("Loaded %s  size %.2fkb  %.1fkb/sec",
                    name, (double) totalbyte / 1024,
                    ((double) 1000 / 1024) * (double) totalbyte / time);
        }

        /**
         * Computes a result, or throws an exception if unable to do so.
         *
         * @return computed result
         * @throws Exception if unable to compute a result
         */
        @Override
        public LoadItem call() throws Exception {
            File pom = File.createTempFile("temp", ".xml");
            try (BufferedInputStream in = new BufferedInputStream(new URL(file).openStream());
                 FileOutputStream fileOutputStream = new FileOutputStream(pom)) {
                byte[] dataBuffer = new byte[1024];
                int bytesRead;
                time = System.currentTimeMillis();
                while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                    fileOutputStream.write(dataBuffer, 0, bytesRead);
                    Thread.sleep(round((double) bytesRead / byteMsec));
                    totalbyte += bytesRead;
                }
                time = System.currentTimeMillis() - time;
            } catch (IOException e) {
                LOGGER.error(e.getMessage(), e);
            }
            name = file.substring(file.lastIndexOf('/') + 1);
            return this;
        }
    }
}
