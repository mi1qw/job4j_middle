package ru.job4j.rabbit;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Rabbit {
    private static final Map<String, Exchange> QUEUE = new ConcurrentHashMap<>();
    //private final String queueName;
    //, final String exchangeType

    /**
     * Queue declare.
     *
     * @param queueName    the queue name
     * @param exchangeType the exchange type
     */
    void queueDeclare(final String queueName, final ExchangeType exchangeType) {
        Exchange exchange = new Exchange(queueName, exchangeType);
        QUEUE.put(queueName, exchange);
    }
    //final String exchangeName,

    /**
     * Queue bind.
     *
     * @param queueName  the queue name
     * @param bindingKey the binding key
     */
    void queueBind(final String queueName, final String bindingKey) {
        QUEUE.get(queueName).queueBind(bindingKey);
        //QUEUES.put()
        //ExchangeType.DIRECT.exchangeName
    }

    /**
     * Basic publish.
     *
     * @param queueName the queue name
     * @param message   the message
     */
    void basicPublish(final String queueName, final String message) {
        Exchange exchange = QUEUE.get(queueName);
        exchange.add(message);
    }

    void basicConsume(final String queueName, final String message) {

    }

    public enum ExchangeType {
        DIRECT(new Direct()),
        TOPIC(new Topic()),
        FANOUT(null),
        HEADER(null);
        private final Exchangemethods methods;

        ExchangeType(final Exchangemethods methods) {
            this.methods = methods;
        }

        public Exchangemethods getMethods() {
            return this.methods;
        }
    }
}
