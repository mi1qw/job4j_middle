package ru.job4j.rabbit;

import java.util.Map;

interface Exchangemethods {
    String route(Map<String, Exchange.InnerQueue> queues, String routingKey,
                 String message, AddGet function);

    String add(String message);

    String get();
}
