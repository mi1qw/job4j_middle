package ru.job4j.switcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;

public class Switcher {
    public static final Logger LOGGER = LoggerFactory.getLogger(Switcher.class);
    private static final Object LOCK = new Object();

    public static void main(final String[] args) throws InterruptedException {
        AtomicBoolean start = new AtomicBoolean(true);
        Thread first = new Thread(
                () -> {
                    start.set(false);
                    while (true) {
                        synchronized (LOCK) {
                            try {
                                System.out.println("Thread A");
                                Thread.sleep(200);
                                LOCK.notifyAll();
                                LOCK.wait();
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                                LOGGER.error(e.getMessage(), e);
                            }
                        }
                    }
                }, "A"
        );
        Thread second = new Thread(
                () -> {
                    try {
                        while (start.get()) {
                            synchronized (LOCK) {
                                LOCK.wait();
                            }
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        LOGGER.error(e.getMessage(), e);
                    }
                    while (true) {
                        try {
                            synchronized (LOCK) {
                                System.out.println("Thread B");
                                Thread.sleep(200);
                                LOCK.notifyAll();
                                LOCK.wait();
                            }
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            LOGGER.error(e.getMessage(), e);
                        }
                    }
                }, "B"
        );
        first.start();
        second.start();
        second.join();
        first.join();
    }
}
