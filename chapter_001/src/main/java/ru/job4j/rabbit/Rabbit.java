package ru.job4j.rabbit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Rabbit {
    public static final Logger LOGGER = LoggerFactory.getLogger(Rabbit.class);
    private static final String LN = System.lineSeparator();
    private static final Map<String, Exchange> QUEUE = new ConcurrentHashMap<>();
    private Server server = new Server();
    private static Rabbit rabbit;
    private String wrongNamQue = "Wrong name of Queue";

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
        if (QUEUE.putIfAbsent(queueName, exchange) == null) {
            if (exchangeType == ExchangeType.QUEUES) {
                exchange.queueBind("#");
            }
        } else {
            LOGGER.warn("There is existing Queue with the same name{}", LN);
        }
    }

    /**
     * Queue bind.
     *
     * @param queueName  the queue name
     * @param bindingKey the binding key
     */
    void queueBind(final String queueName, final String bindingKey) {
        Exchange exchange = QUEUE.get(queueName);
        if (exchange == null) {
            LOGGER.warn("There is no queue with such names{}", LN);
        } else if (exchange.getQueueType() != ExchangeType.QUEUES) {
            exchange.queueBind(bindingKey);
        }
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
    String basicPublish(final String queueName, final String routingKey, final String message) {
        //basicPublish.getAndIncrement();
        Exchange exchange = QUEUE.get(queueName);
        if (exchange == null) {
            LOGGER.warn("{}{}", wrongNamQue, LN);
            return wrongNamQue;
        } else {
            return exchange.add(routingKey, message);
        }
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

    public enum ExchangeType {
        QUEUES(new Queues()),
        DIRECT(new Direct()),
        TOPIC(new Topic());
        private final Exchangemethods methods;

        ExchangeType(final Exchangemethods methods) {
            this.methods = methods;
        }

        public Exchangemethods getMethods() {
            return this.methods;
        }
    }
}
