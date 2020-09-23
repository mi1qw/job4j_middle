package ru.job4j.rabbit;

interface InQueue {
    String add(String routingKey, String message);

    String get(String routingKey);

    void queueBind(String queueBind);
}
