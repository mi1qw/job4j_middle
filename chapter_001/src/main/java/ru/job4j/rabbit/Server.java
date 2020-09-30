package ru.job4j.rabbit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class Server {
    public static final Logger LOGGER = LoggerFactory.getLogger(Server.class);
    private static final String LN = System.lineSeparator();
    private static final String BEGINREG = "^GET /\\?msg=";
    private static final String ENDNREG = " HTTP.*\\z";
    //private static final ExecutorService EXEC = Executors.newCachedThreadPool();
    private static final ExecutorService EXEC = Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors());
    private Thread thread;
    private Consumer postGet = null;

    /**
     * Start.
     */
    public void start() {
        thread = new Thread(() -> {
            try (ServerSocket server = new ServerSocket(9000)) {
                while (!EXEC.isShutdown()) {
                    //System.out.println(EXEC.isShutdown() + " EXEC.isShutdown()");
                    final Socket connection = server.accept();
                    //System.out.println("server.accept()");
                    EXEC.execute(() -> handleRequest(connection));
                    //EXEC.execute(new HandleRequest(connection));
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
                new InputStreamReader(conn.getInputStream(), "UTF-8"), 1000);
             PrintWriter writer = new PrintWriter(conn.getOutputStream())
        ) {
            //System.out.println(Thread.currentThread().getName() + " server name");
            while ((str = in.readLine()) != null) {
                //System.out.println(str);
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
                System.out.println(answer + "     new ProcessMesage(sb.toString()).process();");
            }
            //getMesage(sb.toString());
            //writer.println("HTTP/1.1 200 OK");
            writer.println(answer);
            //writer.println("<p>Получил</p>");
            //writer.println(answer);
            writer.flush();
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    private void setUpMessage(final String str) {
        if (str.contains("POST")) {
            //System.out.println("POSTPOSTPOSTPOSTPOSTPOSTPOSTPOSTPOSTPOSTPOSTPOSTPOSTPOST");
            //postGet = this::post;
        } else if (str.contains("GET")) {
            //System.out.println("GETGETGETGETGETGETGETGETGETGETGETGETGETGETGETGETGETGETGET");
        }
    }

    private String getMesage(final String mesage) {
        //String str = mesage.replaceAll(BEGINREG, "");
        //return str.replaceAll(ENDNREG, "");
        int bodyJSON = mesage.indexOf("{");
        if (bodyJSON != -1) {
            String key = mesage.substring(0, bodyJSON);
            String body = mesage.substring(bodyJSON);
            System.out.println(key);
            System.out.println(body);
        }
        return null;
    }

    /**
     * Post.
     */
    void post() {
        System.out.println("post();");
    }

    /**
     * Get.
     */
    void get() {
        System.out.println("get");
    }
}
