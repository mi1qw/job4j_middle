package ru.job4j.rabbit;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static org.junit.Assert.assertTrue;

public class SendInmanyThreadTest {
    @Test
    public void sendInmanyThread() {
        Rabbit rabbit = new Rabbit();
        rabbit.queueDeclare("direct", Rabbit.ExchangeType.DIRECT);
        rabbit.queueBind("direct", "summer");
        rabbit.queueBind("direct", "autumn");
        rabbit.queueBind("direct", "winter");
        rabbit.queueBind("direct", "spring");
        int nThreads = 3999;
        AtomicInteger integ = new AtomicInteger(nThreads);
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
}
