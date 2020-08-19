package ru.job4j.threadsleep;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreadSleep {
    public static final Logger LOGGER = LoggerFactory.getLogger(ThreadSleep.class);

    protected ThreadSleep() {
        throw new IllegalStateException("Utility class");
    }

    public static void main(final String[] args) {

        Thread thread = new Thread(
                () -> {
                    try {
                        for (int index = 0; index < 101; index++) {
                            System.out.print("\rLoading : " + index + "%");
                            Thread.sleep(10);
                        }
                        System.out.println("\nLoaded.");
                    } catch (InterruptedException e) {
                        LOGGER.error(e.getMessage(), e);
                        Thread.currentThread().interrupt();
                    }
                }
        );
        thread.start();
        System.out.println("Main");
    }
}
