package ru.job4j.rabbit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    public static final Logger LOGGER = LoggerFactory.getLogger(Server.class);
    private static final ExecutorService EXEC = Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors());
    private Thread thread;

    /**
     * Start.
     */
    public void start() {
        thread = new Thread(() -> {
            try (ServerSocket server = new ServerSocket(9000)) {
                while (!EXEC.isShutdown()) {
                    final Socket connection = server.accept();
                    EXEC.execute(() -> handleRequest(connection));
                }
            } catch (IOException e) {
                LOGGER.error(e.getMessage(), e);
            }
        });
        thread.start();
    }

    /**
     * Gets server.
     *
     * @return the server
     */
    public Thread getThread() {
        return thread;
    }

    /**
     * Stop.
     */
    public void stop() {
        EXEC.shutdown();
    }

    private void handleRequest(final Socket conn) {
        String str;
        String answer = "HTTP/1.1 200 OK";
        StringBuilder sb = new StringBuilder();
        boolean begin = false;
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
             PrintWriter writer = new PrintWriter(conn.getOutputStream())
        ) {
            while ((str = in.readLine()) != null) {
                if (begin) {
                    sb.append(str);
                } else if (str.isEmpty()) {
                    begin = true;
                }
                if (!in.ready()) { // для выхода изцикла при запросе из браузера
                    break;
                }
            }
            if (sb.length() != 0) {
                answer = new ProcessMesage(sb.toString()).process();
            }
            writer.println(answer);
            writer.flush();
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}
