package ru.job4j.rabbit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

class Queues implements Exchangemethods {
    public static final Logger LOGGER = LoggerFactory.getLogger(Queues.class);
    private static final String LN = System.lineSeparator();
    private String wasntAded = "The message wasn't added";

    @Override
    public String route(final Map<String, Exchange.InnerQueue> queues,
                        final String routingKey, final String message,
                        final AddGet function, final boolean isGet) {
        String out = null;
        out = new Route().route(queues, "#",
                message, function, isGet);
        if (out == null) {
            out = wasntAded;
            LOGGER.warn("{}{}", wasntAded, LN);
        }
        return out;
    }
}
