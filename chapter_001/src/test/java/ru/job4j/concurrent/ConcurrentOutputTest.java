package ru.job4j.concurrent;

import org.junit.Test;

import static org.junit.Assert.*;

public class ConcurrentOutputTest {
    @Test
    public void doMain() {
        ConcurrentOutput.main(new String[]{});
        assertTrue(true);
    }
}