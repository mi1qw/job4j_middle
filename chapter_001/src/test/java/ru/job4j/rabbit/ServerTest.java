package ru.job4j.rabbit;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class ServerTest {
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
        Rabbit rabbit = new Rabbit();
        rabbit.queueDeclare("direct", Rabbit.ExchangeType.DIRECT);
        rabbit.queueBind("direct", "summer");
        rabbit.queueBind("direct", "autumn");
        rabbit.queueBind("direct", "winter");
        rabbit.queueBind("direct", "spring");
        int nThreads = 399;
        //int nThreads = 3999;
        //int nThreads = 9999;
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
        //System.out.println(threads.size());
        rabbit.getServer().stop();

        //Thread.sleep(0);
        //String[] queue = rabbit.printQueue("direct");
        //System.out.println(rabbit.getBasicPublish().get() + "   getBasicPublish");
        //System.out.println(sprin.get() + " spin");
        System.out.println();
        String[] queue;
        Map<String, Exchange.InnerQueue> map = rabbit.getQueues("direct");
        for (Map.Entry<String, Exchange.InnerQueue> innerQueue : map.entrySet()) {
            queue = innerQueue.getValue().peekAll();
            System.out.println(Stream.of(queue).distinct().count() + " "
                    //+ Stream.of(queue).count() + " "
                    + innerQueue.getKey());
        }
        //for (Map.Entry<String, Exchange.InnerQueue> innerQueue : map.entrySet()) {
        //    queue = innerQueue.getValue().peekAll();
        //    System.out.println(Stream.of(queue).distinct().count() + " "
        //            //+ Stream.of(queue).count() + " "
        //            + innerQueue.getKey());
        //}
        //rabbit.getServer().stop();
    }
}


class TestHarness {
    public long timeTasks(final int nThreads, final Runnable task) throws InterruptedException {
        final CountDownLatch startGate = new CountDownLatch(1);
        final CountDownLatch endGate = new CountDownLatch(nThreads);
        for (int i = 0; i < nThreads; i++) {
            Thread t = new Thread() {
                public void run() {
                    try {
                        startGate.await();
                        try {
                            task.run();
                        } finally {
                            endGate.countDown();
                        }
                    } catch (InterruptedException ignored) {
                    }
                }
            };
            t.start();
        }
        long start = System.nanoTime();
        startGate.countDown();
        endGate.await();
        long end = System.nanoTime();
        return end - start;
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
