package ru.job4j.threadpool;

import org.awaitility.core.ConditionTimeoutException;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.awaitility.Awaitility.await;
import static org.junit.Assert.assertTrue;

public class ThreadPoolTest {
    public static final Logger LOGGER = LoggerFactory.getLogger(ThreadPoolTest.class);

    @Test
    public void work() {
        AtomicInteger task = new AtomicInteger();
        ThreadPool pool = new ThreadPool();
        new Thread(() -> {
            await().pollDelay(250, MILLISECONDS).until(() -> true);
            pool.shutdown();
            LOGGER.warn("pool.shutdown{}", System.lineSeparator());
        }, "shutdown").start();
        await().pollDelay(30, MILLISECONDS).until(() -> true);
        while (!pool.isTerminated()) {
            pool.work(() -> {
                System.out.println("task-" + task.incrementAndGet()
                        + " " + Thread.currentThread().getName());
                try {
                    await().atMost(Duration.ofMillis(110)).until(() -> true);
                } catch (ConditionTimeoutException e) {
                    LOGGER.info(e.getMessage(), e);
                }
            });
        }
        assertTrue(pool.isTerminated());
    }
}
