package ru.job4j.rabbit;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CompareMaskTest {
    @Test
    //@Ignore
    public void compare0() {
        String patern = "aa.*.bb";
        String str = "aa.11.22.33.bb";
        assertFalse(CompareMask.compare(str.split("\\."), patern.split("\\.")));
    }

    @Test
    @Ignore
    public void compare1() {
        String patern = "aa.#.bb";
        String str = "aa.11.22.33.bb";
        assertTrue(CompareMask.compare(str.split("\\."), patern.split("\\.")));
    }

    @Test
    @Ignore
    public void compare2() {
        String patern = "aa.#";
        String str = "aa.11.22.33.bb";
        assertTrue(CompareMask.compare(str.split("\\."), patern.split("\\.")));
    }
}
