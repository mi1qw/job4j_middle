package ru.job4j.cas;

import net.jcip.annotations.ThreadSafe;

import java.util.concurrent.atomic.AtomicReference;

@ThreadSafe
public class CASCount {
    private final AtomicReference<Integer> count = new AtomicReference<>();

    public CASCount(final int num) {
        count.set(num);
    }

    /**
     * Increment.
     */
    public void increment() {
        int ref;
        int temp;
        do {
            ref = count.get();
            temp = ref + 1;
        } while (!count.compareAndSet(ref, temp));
    }

    /**
     * Get int.
     *
     * @return the int
     */
    public int get() {
        return count.get();
    }
}
