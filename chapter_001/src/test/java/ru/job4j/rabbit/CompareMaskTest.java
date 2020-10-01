package ru.job4j.rabbit;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CompareMaskTest {
    private CompareMask compare;

    @Test
    public void compare0() {
        String pat = "aa.*.bb";
        String str = "aa.11.22.33.bb";
        assertFalse(new CompareMask(str, pat).compare());
    }

    @Test
    public void compare1() {
        String pat = "aa.#.bb";
        String str = "aa.11.22.33.bb";
        assertTrue(new CompareMask(str, pat).compare());
    }

    @Test
    public void compare3() {
        String pat = "#.bb1";
        String str = "aa.1.2.3.bb";
        assertFalse(new CompareMask(str, pat).compare());
    }

    @Test
    public void compare4() {
        String pat = "a.#";
        String str = "a.1.2.3.bb";
        assertTrue(new CompareMask(str, pat).compare());
    }

    @Test
    public void compare5() {
        String pat = "a.#.b.c";
        String str = "a.1.2.3.b.4";
        assertFalse(new CompareMask(str, pat).compare());
    }

    @Test
    public void compare6() {
        String pat = "a.#.b.*";
        String str = "a.1.2.3.b.4";
        assertTrue(new CompareMask(str, pat).compare());
    }

    @Test
    public void compare7() {
        String pat = "*.a.#.b.*";
        String str = "0.a.1.2.3.b.4";
        assertTrue(new CompareMask(str, pat).compare());
    }

    @Test
    public void compare8() {
        String pat = "*.*.a";
        String str = "0.a.1.2.3.b.4";
        assertFalse(new CompareMask(str, pat).compare());
    }

    @Test
    public void compare9() {
        String pat = "*.*.a";
        String str = "0.a.a.2.3.b.4";
        assertFalse(new CompareMask(str, pat).compare());
    }

    @Test
    public void compare10() {
        String pat = "*.*.a";
        String str = "0.a";
        assertFalse(new CompareMask(str, pat).compare());
    }

    @Test
    public void compare11() {
        String pat = "#.a.a";
        String str = "0";
        assertFalse(new CompareMask(str, pat).compare());
    }
}
