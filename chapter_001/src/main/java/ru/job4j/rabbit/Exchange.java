package ru.job4j.rabbit;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

interface AddGet {
    String addGet(Exchange.InnerQueue innerQueue, String message);
}


class Exchange implements InQueue {
    private final String queueName;
    private final Rabbit.ExchangeType queueType;
    private final Map<String, InnerQueue> queues = new ConcurrentHashMap<>();
    private final Exchangemethods type;

    Exchange(final String queueName, final Rabbit.ExchangeType queueType) {
        this.queueName = queueName;
        this.queueType = queueType;
        this.type = queueType.getMethods();
    }

    /**
     * Instantiates a new Print queue.
     */
    public String[] printQueue() {
        String[] queue = null;
        System.out.println(queueName);
        for (Map.Entry<String, InnerQueue> innerQueue : queues.entrySet()) {
            String keyThis = innerQueue.getValue().getBindingKey();
            System.out.println(keyThis);
            queue = innerQueue.getValue().peekAll();
            System.out.println(Arrays.toString(queue));
        }
        return queue;
    }

    /**
     * Instantiates a new Print queue.
     */
    public Map<String, InnerQueue> getQueues() {
        return queues;
    }

    /**
     * queueBind.
     *
     * @param bindingKey queueBind
     */
    @Override
    public void queueBind(final String bindingKey) {
        //queue.add(new InnerQueue(bindingKey));
        //type.
        queues.put(bindingKey, new InnerQueue(bindingKey));
    }

    /**
     * add.
     *
     * @param message
     * @return
     */
    @Override
    public String add(final String routingKey, final String message) {
        return type.route(queues, routingKey, message,
                (n, m) -> {
                    if (!n.add(m)) {
                        return null;
                    } else {
                        return message;
                    }
                });
    }

    /**
     * get.
     */
    @Override
    public String get(final String routingKey) {
        return type.route(queues, routingKey, null,
                (n, m) -> n.poll());
    }

    void topic() {

    }

    void queue() {

    }

    protected final class InnerQueue {
        private final String bindingKey;
        private final ConcurrentLinkedQueue<String> innerqueue1;

        private InnerQueue(final String bindingKey) {
            this.bindingKey = bindingKey;
            this.innerqueue1 = new ConcurrentLinkedQueue<>();
        }

        public String getBindingKey() {
            return bindingKey;
        }

        public boolean add(final String message) {
            return innerqueue1.offer(message);
        }

        public String poll() {
            return innerqueue1.poll();
        }

        public String[] peekAll() {
            return innerqueue1.toArray(String[]::new);
        }

        private ConcurrentLinkedQueue<String> getInnerqueue1() {
            return innerqueue1;
        }
    }
}
