package ru.job4j.waitcountbarrier;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

@ThreadSafe
public class CountBarrier {
    private final Object monitor = this;
    private final int total;
    @GuardedBy("monitor")
    private int count;

    public static void main(final String[] args) {
        int call = 5;
        CountBarrier countBarrier = new CountBarrier(call);
        Thread master = new Thread(
                () -> {
                    System.out.println(Thread.currentThread().getName() + " started");
                    for (int i = 0; i < call; i++) {
                        countBarrier.count();
                    }
                }, "master"
        );
        Thread slave = new Thread(
                () -> {
                    System.out.println(Thread.currentThread().getName() + "  started");
                    countBarrier.await();
                }, "slave"
        );
        Thread brave = new Thread(
                () -> {
                    System.out.println(Thread.currentThread().getName() + "  started");
                    countBarrier.await();
                }, "brave"
        );
        master.start();
        slave.start();
        brave.start();
    }

    public CountBarrier(final int total) {
        this.total = total;
    }

    /**
     * Count.
     */
    public void count() {
        synchronized (monitor) {
            ++count;
            System.out.println(Thread.currentThread().getName() + " " + count);
            monitor.notifyAll();
        }
    }

    /**
     * Await.
     */
    public void await() {
        synchronized (monitor) {
            while (count < total) {
                try {
                    monitor.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            System.out.println(Thread.currentThread().getName() + "  finally waited for someone!");
        }
    }
}
