package ru.job4j.threadsleep;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class ThreadSleepTest {
    @Test
    public void doMain() {
        ThreadSleep.main(new String[]{});
        assertTrue(true);
    }
}
