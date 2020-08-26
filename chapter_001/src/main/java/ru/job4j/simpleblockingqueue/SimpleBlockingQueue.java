package ru.job4j.simpleblockingqueue;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.LinkedList;
import java.util.Queue;

@ThreadSafe
public class SimpleBlockingQueue<T> {
    private final int bufMaxSize;
    @GuardedBy("this")
    private final Queue<T> queue = new LinkedList<>();

    public SimpleBlockingQueue(final int bufmaxsize) {
        this.bufMaxSize = bufmaxsize;
    }

    /**
     * Offer.
     *
     * @param value the value
     * @return boolean
     */
    public synchronized boolean offer(final T value) {
        boolean res;
        try {
            while (queue.size() == bufMaxSize) {
                wait();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println(Thread.currentThread().getName() + "  "
                + queue.size() + " offer(value)");
        res = queue.offer(value);
        notifyAll();
        return res;
    }

    /**
     * Poll t.
     *
     * @return the t
     */
    public synchronized T poll() {
        while (queue.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        T out = queue.poll();
        notifyAll();
        return out;
    }

    /**
     * Is empty boolean.
     *
     * @return the boolean
     */
    public synchronized boolean isEmpty() {
        return queue.isEmpty();
    }

    /**
     * Size int.
     *
     * @return the int
     */
    public synchronized int size() {
        return queue.size();
    }
}
