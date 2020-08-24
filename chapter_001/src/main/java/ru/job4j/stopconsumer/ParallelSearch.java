package ru.job4j.stopconsumer;

import ru.job4j.simpleblockingqueue.SimpleBlockingQueue;

public class ParallelSearch {
    public static void main(final String[] args) throws InterruptedException {
        SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(50);
        final Thread consumer = new Thread(
                () -> {
                    while (true) {
                        try {
                            System.out.println(queue.poll());
                            Thread.sleep(10);
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
        System.out.println(producer.isInterrupted() + "  " + producer.getState());
        queue.offer(0);
        consumer.interrupt();
        System.out.println(consumer.isInterrupted() + "  " + consumer.getState());
        consumer.di
        //consumer.stop();
        System.out.println("End");
        //consumer.join();
    }
}
