package ru.job4j.simpleblockingqueue;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.LinkedList;
import java.util.Queue;

@ThreadSafe
public class SimpleBlockingQueue<T> {
    private int bufMaxSize;

    public SimpleBlockingQueue(final int bufmaxsize) {
        this.bufMaxSize = bufmaxsize;
    }

    @GuardedBy("this")
    private Queue<T> queue = new LinkedList<>();

    /**
     * Offer.
     *
     * @param value the value
     * @return
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
}
