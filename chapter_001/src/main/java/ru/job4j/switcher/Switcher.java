package ru.job4j.switcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Switcher {
    public static final Logger LOGGER = LoggerFactory.getLogger(Switcher.class);
    private static final Object LOCK = new Object();

    public static void main(final String[] args) throws InterruptedException {

        Thread first = new Thread(
                () -> {
                    while (true) {
                        synchronized (LOCK) {
                            System.out.println("Thread A");
                            try {
                                Thread.sleep(1000);
                                LOCK.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, "A"
        );
        Thread second = new Thread(
                () -> {
                    while (true) {
                        try {
                            synchronized (LOCK) {
                                LOCK.wait(100);
                                System.out.println("Thread B");
                                Thread.sleep(1000);
                                LOCK.notifyAll();
                            }
                        } catch (InterruptedException e) {
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
