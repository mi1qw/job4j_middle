package ru.job4j.jcipannotations;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

@ThreadSafe
public class Count {
    @GuardedBy("this")
    private int value;

    /**
     * Increment.
     */
    public synchronized void increment() {
        this.value++;
    }

    /**
     * Get int.
     *
     * @return the int
     */
    public synchronized int get() {
        return this.value;
    }
}
