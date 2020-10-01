package ru.job4j.rabbit;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ServerTest {
    private static final String LN = System.lineSeparator();
    private Deque<String> result = new LinkedList<>();
    public static final Logger LOGGER = LoggerFactory.getLogger(ServerTest.class);
    private Rabbit rabbit;
    private List<String> list = List.of("element 1", "element 2");

    @Before
    public void setUp() {
        rabbit = new Rabbit();
    }

    @Test
    public void queue() {
        rabbit.queueDeclare("Queues", Rabbit.ExchangeType.QUEUES);
        rabbit.basicPublish("Queues", null, "element 1");
        rabbit.basicPublish("Queues", "1", "element 2");
        rabbit.printQueue("Queues");
        String el1 = rabbit.basicConsume("Queues", null);
        String el2 = rabbit.basicConsume("Queues", null);
        String error = rabbit.basicConsume("Queues", null);
        assertEquals("The message wasn't added", error);
        assertThat(list, containsInAnyOrder(el1, el2));
    }

    @Test
    public void direct() {
        rabbit.queueDeclare("direct", Rabbit.ExchangeType.DIRECT);
        rabbit.queueBind("direct", "winter");
        rabbit.queueBind("direct", "summer");
        rabbit.queueBind("direct1", "?");
        rabbit.queueBind("direct", "summer.*");

        rabbit.basicPublish("direct", "winter", "element 1");
        rabbit.basicPublish("direct", "winter", "element 2");
        rabbit.basicPublish("direct", "summer", "summer 1");
        String error = rabbit.basicPublish("direct", "summer1", "?");
        assertEquals("The message wasn't added", error);
        rabbit.printQueue("direct");

        String el1 = rabbit.basicConsume("direct", "winter");
        String el2 = rabbit.basicConsume("direct", "winter");
        String el3 = rabbit.basicConsume("direct", "summer");
        String el4 = rabbit.basicConsume("direct", "summer.*");
        String el5 = rabbit.basicConsume("direct", "summer1");
        String el6 = rabbit.basicConsume("direct", "summer");
        assertThat(list, containsInAnyOrder(el1, el2));
        assertEquals("summer 1", el3);
        assertEquals("Wrong routingKey, must be without '*' or '#'", el4);
        assertEquals("The message wasn't added", el5);
        assertEquals("The message wasn't added", el6);
    }

    @Test
    public void topic() {
        rabbit.queueDeclare("weather", Rabbit.ExchangeType.TOPIC);
        rabbit.queueBind("weather", "weather.*");
        rabbit.queueBind("weather", "weather.city.*");
        rabbit.queueBind("weather", "population.city.*");
        rabbit.queueBind("weather", "#.Mytown");

        //rabbit.basicPublish("weather", "weather.vilage", "10 C");
        //rabbit.basicPublish("weather", "weather.city", "15 C");

        rabbit.basicPublish("weather", "weather.city.Mytown", "15 C");
        rabbit.basicPublish("weather", "population.city.Mytown",
                "population 20 00 people");

        rabbit.printQueue("weather");
    }

    @Test
    public void start() throws IOException, InterruptedException {
        //System.out.println(Arrays.toString(Rabbit.ExchangeType.values()));
        //System.out.println(ExchangeType.valueOf("DIRECT"));
        //System.out.println(ExchangeType.valueOf("TOPIC").ordinal());
        //System.out.println(ExchangeType.DIRECT.getExchangeName());
        Rabbit rabbit = new Rabbit();
        rabbit.queueDeclare("weather", Rabbit.ExchangeType.TOPIC);
        rabbit.queueBind("weather", "weather.*");

        rabbit.queueDeclare("1", Rabbit.ExchangeType.TOPIC);
        rabbit.queueDeclare("2", Rabbit.ExchangeType.TOPIC);
        rabbit.queueBind("1", "weather.*");
        rabbit.queueBind("2", "rain.*");

        rabbit.queueDeclare("direct", Rabbit.ExchangeType.DIRECT);
        rabbit.queueBind("direct", "winter");
        rabbit.queueBind("direct", "summer");
        rabbit.basicPublish("direct", "winter", "winter 100");
        rabbit.basicPublish("direct", "winter", "winter 1");
        rabbit.basicPublish("direct", "winter", "winter 2");
        rabbit.basicPublish("direct", "summer", "summer 1");
        rabbit.printQueue("direct");

        //rabbit.queueBind("1", "weather.village");
        rabbit.queueBind("1", "weather.village,weather.city.*");
        rabbit.basicPublish("1", "weather.city", "a1a1a1");
        rabbit.basicPublish("1", "weather.city", "b1b1b1");
        rabbit.basicPublish("1", "weather.village", "c1c1c1");
        rabbit.basicPublish("1", "weather.city.1", "d1d1d1");
        //System.out.println(rabbit.basicConsume("1", "weather.city") + "  weather.city");
        //System.out.println(rabbit.basicConsume("1", "weather.city") + "  weather.city");
        //System.out.println(rabbit.basicConsume("1", "weather.village") + "  weather.village");
        rabbit.printQueue("1");
        //rabbit.printQueue("2");
        System.out.println();

        System.out.println(rabbit.basicConsume("1", "weather.city.1") + "  weather.city.1");
        System.out.println();
        rabbit.printQueue("1");

        System.out.println();
        System.out.println(rabbit.basicConsume("1", "weather.village") + "  weather.village");
        System.out.println(rabbit.basicConsume("1", "weather.village") + "  weather.village");
        rabbit.printQueue("1");

        //Thread.sleep(5000);
        //rabbit.getServer().stop();
        rabbit.getThreadServer().join();
    }

    @Test
    public void stop() throws InterruptedException {

        Rabbit rabbit = new Rabbit();

        rabbit.queueDeclare("direct", Rabbit.ExchangeType.DIRECT);
        rabbit.queueBind("direct", "summer");
        rabbit.queueBind("direct", "autumn");
        rabbit.queueBind("direct", "winter");
        rabbit.queueBind("direct", "spring");

        int nThreads = 3999;

        AtomicInteger integ = new AtomicInteger(nThreads);
        //AtomicInteger sprin = new AtomicInteger(0);
        //int num = 0;
        CountDownLatch cnt = new CountDownLatch(nThreads);
        List<Thread> threads = new ArrayList<>();
        for (int i = nThreads; i > -1; i--) {
            Thread thread = new Thread(() -> {
                try {
                    cnt.await();
                    String name = Thread.currentThread().getName();
                    int nameInt = Integer.parseInt(name);
                    //System.out.println(nameInt + "  nameInt");
                    if ((nameInt = nameInt % 4) == 0) {
                        rabbit.basicPublish("direct",
                                "summer", "summer " + name);
                    } else if (nameInt == 1) {
                        rabbit.basicPublish("direct",
                                "autumn", "autumn " + name);
                    } else if (nameInt == 2) {
                        rabbit.basicPublish("direct",
                                "winter", "winter " + name);
                    } else if (nameInt == 3) {
                        //sprin.getAndIncrement();
                        rabbit.basicPublish("direct",
                                "spring", "spring " + name);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }, String.valueOf(integ.getAndDecrement()));
            thread.start();
            threads.add(thread);
            cnt.countDown();
            //num++;
        }

        System.out.println(threads.size());
        while (threads.size() != 0) {
            Thread th = threads.get(0);
            if (!th.isAlive()) {
                threads.remove(th);
            }
        }

        String[] queue;
        List<Integer> queueItems = new ArrayList<>();
        Map<String, Exchange.InnerQueue> map = rabbit.getQueues("direct");
        for (Map.Entry<String, Exchange.InnerQueue> innerQueue : map.entrySet()) {
            queue = innerQueue.getValue().peekAll();
            int items = (int) Stream.of(queue).distinct().count();
            queueItems.add(items);
            System.out.println(items + " " + innerQueue.getKey());
        }
        rabbit.getServer().stop();
        assertTrue(queueItems.get(0).equals(queueItems.get(1))
                && queueItems.get(1).equals(queueItems.get(2))
                && queueItems.get(2).equals(queueItems.get(3))
        );
    }

    @Test
    public void send() throws InterruptedException, IOException {
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
