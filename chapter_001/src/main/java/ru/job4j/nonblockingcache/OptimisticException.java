package ru.job4j.nonblockingcache;

public class OptimisticException extends RuntimeException {
    OptimisticException(final String e) {
        super(e);
    }
}
