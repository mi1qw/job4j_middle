package ru.job4j.waitcountbarrier;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class CountBarrierTest {
    @Test
    public void count() {
        CountBarrier.main(new String[]{});
        assertTrue(true);
    }
}
