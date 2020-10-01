package ru.job4j.rabbit;

import java.util.Map;

public class Route {
    /**
     * Route string.
     *
     * @param queues     the queues
     * @param routingKey the routing key
     * @param message    the message
     * @param function   the function
     * @return the string
     */
    public String route(final Map<String, Exchange.InnerQueue> queues,
                        final String routingKey, final String message,
                        final AddGet function, final boolean isGet) {
        String out = null;
        for (Map.Entry<String, Exchange.InnerQueue> innerQueue : queues.entrySet()) {
            String keyThis = innerQueue.getValue().getBindingKey();
            String[] keys = keyThis.split(",\\s*");
            for (String key : keys) {
                boolean bo = new CompareMask(routingKey, key).compare();
                if (bo) {
                    Exchange.InnerQueue innerQueue1 = queues.get(keyThis);
                    out = function.addGet(innerQueue1, message);
                    if (out != null && isGet) {
                        return out;
                    }
                }
            }
        }
        return out;
    }
}
