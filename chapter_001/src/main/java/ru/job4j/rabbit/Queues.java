package ru.job4j.rabbit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

class Queues implements Exchangemethods {
    public static final Logger LOGGER = LoggerFactory.getLogger(Direct.class);
    private static final String LN = System.lineSeparator();

    @Override
    public String route(final Map<String, Exchange.InnerQueue> queues,
                        final String routingKey, final String message,
                        final AddGet function) {
        String out = null;
        out = new Route().route(queues, "#",
                message, function);
        if (out == null) {
            LOGGER.warn("The message wasn't aded{}", LN);
        }
        return out;
    }

    @Override
    public String add(final String message) {
        return null;
    }

    @Override
    public String get() {
        return null;
    }
}
