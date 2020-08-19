package ru.job4j.threadstate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreadState {
    public static final Logger LOGGER = LoggerFactory.getLogger(ThreadState.class);
    public static final String LN = System.lineSeparator();

    protected ThreadState() {
        throw new IllegalStateException("Utility class");
    }

    public static void main(final String[] args) {
        Thread first = new Thread(() -> LOGGER.info("{}{}",
                Thread.currentThread().getName(), LN));
        first.setName("first");
        Thread second = new Thread(() -> LOGGER.info("{}{}",
                Thread.currentThread().getName(), LN));
        second.setName("second");
        first.start();
        second.start();
        while (first.getState() != Thread.State.TERMINATED
                || second.getState() != Thread.State.TERMINATED) {
            System.out.println(String.format("first- %s   second- %s",
                    first.getState(), second.getState()));
        }
        System.out.println(Thread.currentThread().getName() + " code executed");
    }
}
