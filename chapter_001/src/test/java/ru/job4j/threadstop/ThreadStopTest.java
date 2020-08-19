package ru.job4j.threadstop;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class ThreadStopTest {
    @Test
    public void doMain() throws InterruptedException {
        ThreadStop.main(new String[]{});
        assertTrue(true);
    }
}
