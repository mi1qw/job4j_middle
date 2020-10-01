package ru.job4j.rabbit;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class SendWithSocket {
    private static final String LN = System.lineSeparator();
    public static final Logger LOGGER = LoggerFactory.getLogger(SendWithSocket.class);
    private Deque<String> result = new LinkedList<>();

    @Test
    public void sendWithSocket() throws InterruptedException, IOException {
        Rabbit rabbit = new Rabbit();
        rabbit.queueDeclare("weather", Rabbit.ExchangeType.DIRECT);
        rabbit.queueBind("weather", "weather.town");
        Map<String, Exchange.InnerQueue> map = rabbit.getQueues("weather");
        Exchange.InnerQueue exchange = map.get("weather.town");
        StringBuilder sb = new StringBuilder("POST / HTTP/1.1");
        sb.append("Host: localhost:9000\n"
                + "Connection: keep-alive\n"
                + "Content-Type: text/plain;charset=UTF-8");
        sb.append(LN).append(LN).append("{\n"
                + " \"postGet\":\"POST\",\n"
                + " \"queue\":\"weather\",\n"
                + " \"routingKey\":\"weather.town\",\n"
                + " \"text\":\"temperature +18 C\"\n"
                + "}");
        while (exchange.peekAll().length == 0) {
            send(sb.toString());
            Thread.sleep(1000);
        }
        rabbit.printQueue("weather");
        sb = new StringBuilder("POST / HTTP/1.1");
        sb.append("Host: localhost:9000\n"
                + "Connection: keep-alive\n"
                + "Content-Type: text/plain;charset=UTF-8");
        sb.append(LN).append(LN).append("{\n"
                + " \"postGet\":\"GET\",\n"
                + " \"queue\":\"weather\",\n"
                + " \"routingKey\":\"weather.town\",\n"
                + " \"text\":\"temperature +18 C\"\n"
                + "}");

        while (exchange.peekAll().length > 0) {
            send(sb.toString());
            Thread.sleep(200);
        }
        System.out.println(result.getLast());
        assertEquals("temperature +18 C", result.getLast());
    }

    /**
     * Send string.
     *
     * @param message the message
     * @return the string
     * @throws IOException the io exception
     */
    void send(final String message) throws IOException {
        result.clear();
        String str = null;
        Socket socket = new Socket(InetAddress.getLocalHost(), 9000);
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
             PrintWriter writer = new PrintWriter(socket.getOutputStream())
        ) {
            writer.println(message);
            writer.flush();
            while ((str = in.readLine()) != null) {
                result.push(str);
            }
            Thread.sleep(10);
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}
