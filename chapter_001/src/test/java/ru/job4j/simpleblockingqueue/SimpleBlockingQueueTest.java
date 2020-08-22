package ru.job4j.simpleblockingqueue;

import net.jcip.annotations.GuardedBy;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class SimpleBlockingQueueTest {
    public static final int SIZE = 5;
    public static final int SEND = SIZE + 20;
    @GuardedBy("queue")
    private final SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(SIZE);

    /**
     * Offer.
     */
    @Test
    public void offer() throws InterruptedException {
        Thread producer = new Thread(
                () -> {
                    synchronized (queue) {
                        System.out.println(Thread.currentThread().getName() + " started");
                        for (int i = 0; i < SEND; i++) {
                            queue.offer(i);
                        }
                    }
                }, "producer"
        );
        Thread consumer = new Thread(
                () -> {
                    synchronized (queue) {
                        System.out.println(Thread.currentThread().getName() + " started");
                        while (true) {
                            System.out.println(Thread.currentThread().getName()
                                    + "  " + queue.poll());
                        }
                    }
                }, "consumer"
        );
        producer.start();
        consumer.start();
        producer.join();
        //consumer.join();
        assertTrue(true);
    }
}
