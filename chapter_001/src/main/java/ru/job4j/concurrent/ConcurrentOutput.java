package ru.job4j.concurrent;

class ConcurrentOutput {
    /**
     * Instantiates a new Concurrent output.
     */
    protected ConcurrentOutput() {
        throw new IllegalStateException("Utility class");
    }

    public static void main(final String[] args) {
        Thread another = new Thread(
                () -> System.out.println(Thread.currentThread().getName())
        );
        another.setName("first");
        Thread second = new Thread(
                () -> System.out.println(Thread.currentThread().getName())
        );
        second.setName("second");
        another.start();
        second.start();
        System.out.println(Thread.currentThread().getName());
    }
}
