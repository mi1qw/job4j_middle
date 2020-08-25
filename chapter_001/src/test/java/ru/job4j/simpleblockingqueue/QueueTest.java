package ru.job4j.simpleblockingqueue;

import org.junit.Test;

import java.util.Arrays;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.IntStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class QueueTest {
    @Test
    public void whenFetchAllThenGetIt() throws InterruptedException {
        final CopyOnWriteArrayList<Integer> buffer = new CopyOnWriteArrayList<>();
        final SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(5);
        Thread producer = new Thread(
                () -> {
                    IntStream.range(0, 5).forEach(queue::offer);
                }, "producer"
        );
        producer.start();
        Thread consumer = new Thread(
                () -> {
                    System.out.println(producer.isAlive() + " producer.isAlive   "
                            + !queue.isEmpty() + " !isEmpty() "
                            + "queue " + queue.size());
                    System.out.println();
                    while (producer.isAlive() || !queue.isEmpty()) {
                        System.out.println(producer.isAlive() + " producer.isAlive   "
                                + !queue.isEmpty() + " !isEmpty() "
                                + "queue " + queue.size());

                        try {
                            buffer.add(queue.poll());
                            Thread.sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            Thread.currentThread().interrupt();
                        }
                    }
                }
        );
        consumer.start();
        producer.join();
        consumer.join();
        assertThat(buffer, is(Arrays.asList(0, 1, 2, 3, 4)));
    }
}
