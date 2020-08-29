package ru.job4j.nonblockingcache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OptimisticException extends RuntimeException {
    public static final Logger LOGGER = LoggerFactory.getLogger(OptimisticException.class);

    OptimisticException() {
        super();
    }

    public final void log() {
        LOGGER.info("OptimisticException");
    }
}
