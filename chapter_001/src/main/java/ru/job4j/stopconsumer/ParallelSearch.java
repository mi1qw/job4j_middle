package ru.job4j.stopconsumer;

import ru.job4j.simpleblockingqueue.SimpleBlockingQueue;

public class ParallelSearch {
    public static void main(final String[] args) throws InterruptedException {
        SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(50);
        final Thread consumer = new Thread(
                () -> {
                    while (!Thread.currentThread().isInterrupted()) {
                        try {
                            System.out.println(queue.poll());
                            Thread.sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            Thread.currentThread().interrupt();
                        }
                    }
                }, "Consumer"
        );
        consumer.start();
        final Thread producer = new Thread(
                () -> {
                    for (int index = 0; index != 3; index++) {
                        queue.offer(index);
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            Thread.currentThread().interrupt();
                        }
                    }
                }, "Producer"
        );
        producer.start();
        producer.join();
        queue.offer(0);
        consumer.interrupt();
        System.out.println(consumer.isInterrupted() + "  " + consumer.getState());
    }
}
