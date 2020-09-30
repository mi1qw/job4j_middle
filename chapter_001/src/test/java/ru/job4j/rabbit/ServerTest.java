package ru.job4j.rabbit;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static org.junit.Assert.assertTrue;

public class ServerTest {
    private static final String LN = System.lineSeparator();
    //private Server server = new Server();

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

        //создание объекта для сериализации в JSON
        //Cat cat = new Cat();
        Cat cat = new Cat("Murka", 5, 4);
        //cat.name = "Murka";
        //cat.age = 5;
        //cat.weight = 4;

        //писать результат сериализации будем во Writer(StringWriter)
        StringWriter writer = new StringWriter();
        //это объект Jackson, который выполняет сериализацию
        ObjectMapper mapper = new ObjectMapper();
        // сама сериализация: 1-куда, 2-что
        mapper.writeValue(writer, cat);
        //преобразовываем все записанное во StringWriter в строку
        String result = writer.toString();
        System.out.println(result);
        String jsonString = "{ \"name\":\"Murka\", \"age\":5, \"weight\":4}";
        StringReader reader = new StringReader(jsonString);

        Cat cat1 = mapper.readValue(reader, Cat.class);
        System.out.println(cat1);
    }

    @Test
    public void stop() throws InterruptedException {
        Send send = new Send();
        ExecutorService service = send.getSendService();
        BlockingQueue<String> blockingQueue = send.getBlockingQueue();

        Rabbit rabbit = new Rabbit();

        rabbit.queueDeclare("Queues", Rabbit.ExchangeType.QUEUES);
        rabbit.basicPublish("Queues", null, "Queues 1");
        rabbit.basicPublish("Queues", "1", "Queues 1");
        rabbit.printQueue("Queues");

        rabbit.queueDeclare("direct", Rabbit.ExchangeType.DIRECT);
        rabbit.queueBind("direct", "summer");
        rabbit.queueBind("direct", "autumn");
        rabbit.queueBind("direct", "winter");
        rabbit.queueBind("direct", "spring");
        //int nThreads = 399;
        //int nThreads = 3999;
        int nThreads = 9999;
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
        //rabbit.printQueue("direct");
        //System.out.println(num + "  for (int i = nThreads; i > -1; i--)");
        //rabbit.getThreadServer().join();
        //Thread.sleep(5000);
        //rabbit.getServer().stop();

        System.out.println(threads.size());
        while (threads.size() != 0) {
            Thread th = threads.get(0);
            if (!th.isAlive()) {
                threads.remove(th);
            }
        }

        //Thread.sleep(0);
        //String[] queue = rabbit.printQueue("direct");
        //System.out.println(rabbit.getBasicPublish().get() + "   getBasicPublish");
        //System.out.println(sprin.get() + " spin");
        System.out.println();
        String[] queue;
        List<Integer> queueItems = new ArrayList<>();
        Map<String, Exchange.InnerQueue> map = rabbit.getQueues("direct");
        for (Map.Entry<String, Exchange.InnerQueue> innerQueue : map.entrySet()) {
            queue = innerQueue.getValue().peekAll();
            int items = (int) Stream.of(queue).distinct().count();
            queueItems.add(items);
            System.out.println(items + " "
                    //+ Stream.of(queue).count() + " "
                    + innerQueue.getKey());
        }
        rabbit.getServer().stop();
        assertTrue(queueItems.get(0).equals(queueItems.get(1))
                && queueItems.get(1).equals(queueItems.get(2))
                && queueItems.get(2).equals(queueItems.get(3))
        );
        //for (Map.Entry<String, Exchange.InnerQueue> innerQueue : map.entrySet()) {
        //    queue = innerQueue.getValue().peekAll();
        //    System.out.println(Stream.of(queue).distinct().count() + " "
        //            //+ Stream.of(queue).count() + " "
        //            + innerQueue.getKey());
        //}

    }

    @Test
    public void send() throws InterruptedException, IOException {
        Send send = new Send();
        ExecutorService service = send.getSendService();
        BlockingQueue<String> blockingQueue = send.getBlockingQueue();

        Rabbit rabbit = new Rabbit();
        rabbit.queueDeclare("weather", Rabbit.ExchangeType.DIRECT);
        rabbit.queueBind("weather", "weather.town");
        //rabbit.printQueue("weather");
        String header = "POST / HTTP/1.1\n"
                + "Host: localhost:9000\n"
                + "Connection: keep-alive\n"
                + "Content-Type: text/plain;charset=UTF-8" + LN + LN;
        List<String> list = List.of("{\n"
                        + " \"postGet\":\"POST\",\n"
                        + " \"queue\":\"weather\",\n"
                        + " \"routingKey\":\"weather.town\",\n"
                        + " \"text\":\"temperature +18 C\"\n"
                        + "}",
                "{\n"
                        + " \"postGet\":\"POST\",\n"
                        + " \"queue\":\"weather\",\n"
                        + " \"routingKey\":\"weather.town\",\n"
                        + " \"text\":\"temperature +19 C\"\n"
                        + "}",
                "{\n"
                        + " \"postGet\":\"POST\",\n"
                        + " \"queue\":\"weather\",\n"
                        + " \"routingKey\":\"weather.town\",\n"
                        + " \"text\":\"temperature +20 C\"\n"
                        + "}"
        );
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

        //Thread.sleep(1000);
        Map<String, Exchange.InnerQueue> map = rabbit.getQueues("weather");
        Exchange.InnerQueue exchange = map.get("weather.town");

        send.send();
        Iterator<String> it = list.iterator();
        while (exchange.peekAll().length < 1) {
            //System.out.println(header.concat(it.next()));
            //System.out.println(sb.toString());
            if (it.hasNext()) {
                //System.out.println(header.concat(it.next()));
                blockingQueue.put(header.concat(it.next()));
            } else {
                it = list.iterator();
            }
            //blockingQueue.put(sb.toString());

            //blockingQueue.offer(sb.toString());
            System.out.println(blockingQueue.size() + "  size()");
            //System.out.println(rabbit.getThreadServer().isAlive() + " server");
            //send.send();
            //send(sb.toString());
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

        //send = new Send();
        //service = send.getSendService();
        //blockingQueue = send.getBlockingQueue();
        send.send();

        while (exchange.peekAll().length > 0) {
            //send(sb.toString());
            System.out.println(blockingQueue.size() + "  size()");
            blockingQueue.put(sb.toString());
            Thread.sleep(200);
        }
        System.out.println("End");

        //System.out.println(blockingQueue.size());
        //blockingQueue.forEach(System.out::println);
        send.getResult().forEach(System.out::println);
        System.out.println();
        Deque<String> result = send.getResult();
        System.out.println(result.getLast());

        //Thread.sleep(2000);
    }

    /**
     * Send string.
     *
     * @param message the message
     * @return the string
     * @throws IOException the io exception
     */
    String send(final String message) throws IOException {
        String mesgsend;

        Socket socket = new Socket(InetAddress.getLocalHost(), 9000);
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
             PrintWriter writer = new PrintWriter(socket.getOutputStream())
        ) {
            writer.println(message);
            writer.flush();
            Thread.sleep(10);
        } catch (
                InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}


class Send {
    public static final Logger LOGGER = LoggerFactory.getLogger(Send.class);
    private static final String LN = System.lineSeparator();
    private BlockingQueue<String> blockingQueue = new LinkedBlockingQueue<>();
    private Deque<String> result = new ConcurrentLinkedDeque<>();
    //private BlockingQueue<String> blockingQueue = new SynchronousQueue<>();
    private ExecutorService service = Executors.newCachedThreadPool();

    public BlockingQueue<String> getBlockingQueue() {
        return blockingQueue;
    }

    public Deque<String> getResult() {
        return result;
    }

    public ExecutorService getSendService() {
        return service;
    }

    public void send() throws IOException {
        String mesgsend;

        Socket socket = new Socket(InetAddress.getLocalHost(), 9000);

        //try (BufferedReader in = new BufferedReader(
        //        new InputStreamReader(socket.getInputStream()));
        //     PrintWriter writer = new PrintWriter(socket.getOutputStream())
        //) {
        String str;
        //str = blockingQueue.poll();
        str = "a";
        service.execute(() -> sendSocket(str, socket));

        //{
        //    String str1;
        //    while (!service.isTerminated()) {
        //        //System.out.println(socket.isConnected());
        //
        //        //writer.println(str);
        //        //writer.flush();
        //        try {
        //            while ((str1 = in.readLine()) != null) {
        //                System.out.println(str + "  in.readLine()");
        //            }
        //            //if (in.ready()) {
        //            //    System.out.println(in.readLine() + "  in.readLine()");
        //            //}
        //        } catch (IOException e) {
        //            e.printStackTrace();
        //        }
        //        //try {
        //        //    Thread.sleep(10);
        //        //} catch (InterruptedException e) {
        //        //    e.printStackTrace();
        //        //}
        //
        //        //break;
        //    }
        //});
        //}
    }

    void sendSocket(final String str, final Socket socket) {
        //System.out.println(str);
        String str1;
        try (
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                //PrintWriter writer = new PrintWriter(socket.getOutputStream())
                PrintWriter writer = new PrintWriter(new BufferedWriter(
                        new OutputStreamWriter(socket.getOutputStream()), 1000), true)
        ) {
            while (!service.isTerminated()) {

                //str1 = in.readLine();
                //System.out.println(str1);

                //System.out.println("sssss");
                //writer.println(str);
                //String aaa = "aaa";
                //String aaa = blockingQueue.poll();

                String aaa = blockingQueue.take();
                //System.out.println(aaa);
                writer.println(aaa);
                //writer.flush();
                while ((str1 = in.readLine()) != null) {
                    //blockingQueue.put(str1);
                    result.add(str1);
                    //System.out.println(str1 + " !!!!!!!!!!!!!!!!!!!!!!!! in.readLine()");
                }
                //System.out.println(Thread.currentThread().getName());
                //System.out.println(
                //        socket.isConnected() + "  isConnected" + LN
                //                + socket.isInputShutdown() + "  isInputShutdown()" + LN
                //                + socket.isClosed() + "  isClosed()" + LN
                //);
            }
        } catch (IOException | InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}


@JsonAutoDetect
class Cat {
    private String name;
    private int age;
    private int weight;

    Cat(final String name, final int age, final int weight) {
        this.name = name;
        this.age = age;
        this.weight = weight;
    }

    Cat() {
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public int getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return "Cat{"
                + "name='" + name + '\''
                + ", age=" + age
                + ", weight=" + weight + '}';
    }
}
