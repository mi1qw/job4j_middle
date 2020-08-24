package ru.job4j.simpleblockingqueue;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class SimpleBlockingQueueTest {
    public static final int SIZE = 5;
    public static final String STR = "Great. And all of that implements the sorting algorithm. "
            + "The problem is that SortedSet\n"
            + "also uses the Comparator to decide if the values in the SortedSet are unique. "
            + "And the\n"
            + "implementation above would not let me add any User into the SortedSet where the\n"
            + "username + password length is the same.";
    private final SimpleBlockingQueue<String> queue = new SimpleBlockingQueue<>(SIZE);

    /**
     * Offer.
     */
    @Test
    public void offer() throws InterruptedException {
        StringBuilder sb = new StringBuilder();
        String[] strings = STR.split(" ");
        int length = strings.length;
        Thread producer = new Thread(
                () -> {
                    synchronized (queue) {
                        for (String st : strings) {
                            queue.offer(st);
                        }
                    }
                }, "producer"
        );
        Thread consumer = new Thread(
                () -> {
                    synchronized (queue) {
                        String st;
                        int n = 0;
                        System.out.println(Thread.currentThread().getName() + " started");
                        while (n++ < length) {
                            System.out.println(Thread.currentThread().getName()
                                    + "  " + (st = queue.poll()));
                            sb.append(st).append(n != length ? " " : "");
                        }
                    }
                }, "consumer"
        );
        producer.start();
        consumer.start();
        consumer.join();
        assertThat(sb.toString(), is(STR));
    }
}
