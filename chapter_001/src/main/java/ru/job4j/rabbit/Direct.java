package ru.job4j.rabbit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

class Direct implements Exchangemethods {
    public static final Logger LOGGER = LoggerFactory.getLogger(Direct.class);
    private static final String LN = System.lineSeparator();

    @Override
    public String route(final Map<String, Exchange.InnerQueue> queues, final String routingKey,
                        final String message, final AddGet function) {
        String out = null;
        if (!routingKey.matches(".*[*|#].*")) {
            Route rt = new Route();
            out = rt.route(queues, routingKey, message, function);
            if (out == null) {
                LOGGER.warn("The message wasn't aded{}", LN);
            }
        } else {
            LOGGER.warn("Wrong routingKey, must be without '*' or '#'{}", LN);
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


