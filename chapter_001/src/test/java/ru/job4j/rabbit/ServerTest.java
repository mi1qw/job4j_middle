package ru.job4j.rabbit;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertEquals;

public class ServerTest {
    private static final String LN = System.lineSeparator();
    private Deque<String> result = new LinkedList<>();
    public static final Logger LOGGER = LoggerFactory.getLogger(ServerTest.class);
    private Rabbit rabbit = new Rabbit();
    private List<String> list = List.of("element 1", "element 2");

    @Test
    public void queue() {
        rabbit.queueDeclare("Queues", Rabbit.ExchangeType.QUEUES);
        rabbit.basicPublish("Queues", null, "element 1");
        rabbit.basicPublish("Queues", "1", "element 2");
        rabbit.printQueue("Queues");
        String el1 = rabbit.basicConsume("Queues", null);
        String el2 = rabbit.basicConsume("Queues", null);
        String error = rabbit.basicConsume("Queues", null);
        assertEquals("The message wasn't added", error);
        assertThat(list, containsInAnyOrder(el1, el2));
    }

    @Test
    public void direct() {
        rabbit.queueDeclare("direct", Rabbit.ExchangeType.DIRECT);
        rabbit.queueBind("direct", "winter");
        rabbit.queueBind("direct", "summer");
        rabbit.queueBind("direct1", "?");
        rabbit.queueBind("direct", "summer.*");

        rabbit.basicPublish("direct", "winter", "element 1");
        rabbit.basicPublish("direct", "winter", "element 2");
        rabbit.basicPublish("direct", "summer", "summer 1");
        String error = rabbit.basicPublish("direct", "summer1", "?");
        assertEquals("The message wasn't added", error);
        rabbit.printQueue("direct");

        String el1 = rabbit.basicConsume("direct", "winter");
        String el2 = rabbit.basicConsume("direct", "winter");
        String el3 = rabbit.basicConsume("direct", "summer");
        String el4 = rabbit.basicConsume("direct", "summer.*");
        String el5 = rabbit.basicConsume("direct", "summer1");
        String el6 = rabbit.basicConsume("direct", "summer");
        assertThat(list, containsInAnyOrder(el1, el2));
        assertEquals("summer 1", el3);
        assertEquals("Wrong routingKey, must be without '*' or '#'", el4);
        assertEquals("The message wasn't added", el5);
        assertEquals("The message wasn't added", el6);
    }

    @Test
    public void topic() {
        rabbit.queueDeclare("weather", Rabbit.ExchangeType.TOPIC);
        rabbit.queueBind("weather", "weather.*");
        rabbit.queueBind("weather", "weather.city.*");
        rabbit.queueBind("weather", "population.city.*");
        rabbit.queueBind("weather", "#.Mytown");

        rabbit.basicPublish("weather", "weather.vilage", "10 C");
        rabbit.basicPublish("weather", "weather.city", "20 C");
        rabbit.basicPublish("weather", "weather.city.Mytown", "15 C");
        rabbit.basicPublish("weather", "population.city.Mytown",
                "population 20 00 people");
        rabbit.printQueue("weather");

        String el1 = rabbit.basicConsume("weather", "weather.*");
        String el2 = rabbit.basicConsume("weather", "weather.*");
        assertThat(List.of("10 C", "20 C"), containsInAnyOrder(el1, el2));

        el1 = rabbit.basicConsume("weather", "#.Mytown");
        el2 = rabbit.basicConsume("weather", "#.Mytown");
        assertThat(List.of("15 C", "population 20 00 people"),
                containsInAnyOrder(el1, el2));

        assertEquals("population 20 00 people",
                rabbit.basicConsume("weather", "population.city.*"));
        assertEquals("15 C",
                rabbit.basicConsume("weather", "weather.city.*"));
    }
}
