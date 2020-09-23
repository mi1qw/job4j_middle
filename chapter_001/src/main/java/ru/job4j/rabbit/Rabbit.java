package ru.job4j.rabbit;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Rabbit {
    private static final Map<String, Exchange> QUEUE = new ConcurrentHashMap<>();
    private Server server = new Server();
    private static Rabbit rabbit;

    public Rabbit() {
        server.start();
        rabbit = this;
    }

    public static Rabbit getRabbit() {
        return rabbit;
    }

    /**
     * Gets server.
     *
     * @return the server
     */
    public Thread getThreadServer() {
        return server.getThread();
    }

    /**
     * Gets server.
     *
     * @return the server
     */
    public Server getServer() {
        return server;
    }

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

    //channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes("UTF-8"));
    private AtomicInteger basicPublish = new AtomicInteger(0);

    //public AtomicInteger getBasicPublish() {
    //    return basicPublish;
    //}

    /**
     * Basic publish.
     *
     * @param queueName the queue name
     * @param message   the message
     */
    void basicPublish(final String queueName, final String routingKey, final String message) {
        //basicPublish.getAndIncrement();
        Exchange exchange = QUEUE.get(queueName);
        exchange.add(routingKey, message);
    }

    /**
     * Print queue.
     *
     * @param queueName the queue name
     */
    String[] printQueue(final String queueName) {
        Exchange exchange = QUEUE.get(queueName);
        return exchange.printQueue();
    }

    /**
     * Gets queues.
     *
     * @param queueName the queue name
     * @return the queues
     */
    Map<String, Exchange.InnerQueue> getQueues(final String queueName) {
        Exchange exchange = QUEUE.get(queueName);
        return exchange.getQueues();
    }

    /**
     * Basic consume string.
     *
     * @param queueName  the queue name
     * @param routingKey the routing key
     * @return the string
     */
    //channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });
    String basicConsume(final String queueName, final String routingKey) {
        Exchange exchange = QUEUE.get(queueName);
        return exchange.get(routingKey);
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
