package ru.job4j.threadpool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.simpleblockingqueue.SimpleBlockingQueue;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class ThreadPool {
    private final List<Thread> threads = new LinkedList<>();
    private final SimpleBlockingQueue<Runnable> tasks = new SimpleBlockingQueue<>(5);
    private static final int SIZE = Runtime.getRuntime().availableProcessors();
    private final AtomicBoolean stop = new AtomicBoolean(false);
    public static final Logger LOGGER = LoggerFactory.getLogger(ThreadPool.class);
    public static final String LN = System.lineSeparator();

    public ThreadPool() {
        startPool();
    }

    private void startPool() {
        for (int i = 0; i < SIZE; i++) {
            Thread thred = new Thread(() -> {
                try {
                    while (!stop.get()) {
                        tasks.poll().run();
                    }
                } finally {
                    LOGGER.info("stop Thread{}", LN);
                    threads.remove(Thread.currentThread());
                }
            });
            threads.add(thred);
            thred.start();
        }
    }

    /**
     * Stop pool.
     */
    private void stop() {
        stop.set(true);
        tasks.poll();       // вывести поток из возможной блокировки в SimpleBlockingQueue
    }

    /**
     * Work.
     *
     * @param job the job
     */
    public void work(final Runnable job) {
        if (!stop.get()) {
            tasks.offer(job);
        }
    }

    /**
     * Shutdown.
     * with all tasks.
     */
    public void shutdown() {
        stop();
        for (Thread thread : threads) {
            thread.interrupt();
        }
    }

    /**
     * Instantiates a new Is terminated.
     */
    public boolean isTerminated() {
        return threads.isEmpty();
    }
}
