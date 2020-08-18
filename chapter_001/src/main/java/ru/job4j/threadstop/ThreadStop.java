package ru.job4j.threadstop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreadStop {
    public static final Logger LOGGER = LoggerFactory.getLogger(ThreadStop.class);

    protected ThreadStop() {
        throw new IllegalStateException("Utility class");
    }

    public static void main(final String[] args) throws InterruptedException {
        Thread ball = new Thread(new ConsoleProgress());
        ball.setName("ball");
        Thread thread = new Thread(
                () -> {
                    System.out.println("Prepare to start");
                    ball.start();
                    try {
                        while (!Thread.currentThread().isInterrupted()) {
                            Thread.sleep(10);
                        }
                    } catch (InterruptedException e) {
                        ball.interrupt();
                        Thread.currentThread().interrupt();
                    }
                }
        );
        thread.start();
        Thread.sleep(4000);
        thread.interrupt();
    }

    private static class ConsoleProgress implements Runnable {
        /**
         * When an object implementing interface <code>Runnable</code> is used
         * to create a thread, starting the thread causes the object's
         * <code>run</code> method to be called in that separately executing
         * thread.
         * <p>
         * The general contract of the method <code>run</code> is that it may
         * take any action whatsoever.
         *
         * @see Thread#run()
         */
        @Override
        public void run() {
            int load = 0;
            try {
                String ball = "";
                while (!Thread.currentThread().isInterrupted()) {
                    load = ++load % 4;
                    if (load == 0) {
                        ball = "\\";
                    } else if (load == 1) {
                        ball = "|";
                    } else if (load == 2) {
                        ball = "/";
                    } else {
                        ball = "—";
                    }
                    System.out.print("\r Loading..." + ball);
                    Thread.sleep(500);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
