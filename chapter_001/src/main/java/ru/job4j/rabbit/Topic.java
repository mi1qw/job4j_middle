package ru.job4j.rabbit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

class Topic implements Exchangemethods {
    public static final Logger LOGGER = LoggerFactory.getLogger(Topic.class);
    private static final String LN = System.lineSeparator();
    private String wasntAded = "The message wasn't added";

    @Override
    public String route(final Map<String, Exchange.InnerQueue> queues,
                        final String routingKey, final String message,
                        final AddGet function) {
        String out = null;
        Route rt = new Route();
        out = rt.route(queues, routingKey, message, function);
        if (out == null) {
            out = wasntAded;
            LOGGER.warn("{}{}", wasntAded, LN);
        }
        return out;
    }

    @Override
    public String add(final String message) {
        //Exchange.InnerQueue
        System.out.println(message);
        return null;
    }

    @Override
    public String get() {
        System.out.println("message");
        return null;
    }
}
