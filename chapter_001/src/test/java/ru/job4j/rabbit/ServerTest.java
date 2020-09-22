package ru.job4j.rabbit;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

public class ServerTest {
    private Server server = new Server();

    @Test
    public void start() throws IOException {
        //System.out.println(Arrays.toString(Rabbit.ExchangeType.values()));
        //System.out.println(ExchangeType.valueOf("DIRECT"));
        //System.out.println(ExchangeType.valueOf("TOPIC").ordinal());
        //System.out.println(ExchangeType.DIRECT.getExchangeName());
        Rabbit rabbit = new Rabbit();
        rabbit.queueDeclare("1", Rabbit.ExchangeType.TOPIC);
        rabbit.queueBind("1", "aaa");
        rabbit.queueBind("1", "bbb");
        rabbit.basicPublish("1", "aaa  *.#");

        server.start();

        //создание объекта для сериализации в JSON
        //Cat cat = new Cat();
        Cat cat = new Cat("Murka", 5, 4);
        //cat.name = "Murka";
        //cat.age = 5;
        //cat.weight = 4;

        //писать результат сериализации будем во Writer(StringWriter)
        StringWriter writer = new StringWriter();
        //это объект Jackson, который выполняет сериализацию
        ObjectMapper mapper = new ObjectMapper();
        // сама сериализация: 1-куда, 2-что
        mapper.writeValue(writer, cat);
        //преобразовываем все записанное во StringWriter в строку
        String result = writer.toString();
        System.out.println(result);
        String jsonString = "{ \"name\":\"Murka\", \"age\":5, \"weight\":4}";
        StringReader reader = new StringReader(jsonString);

        Cat cat1 = mapper.readValue(reader, Cat.class);
        System.out.println(cat1);
    }

    @Test
    public void stop() {
    }
}


@JsonAutoDetect
class Cat {
    private String name;
    private int age;
    private int weight;

    Cat(final String name, final int age, final int weight) {
        this.name = name;
        this.age = age;
        this.weight = weight;
    }

    Cat() {
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public int getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return "Cat{"
                + "name='" + name + '\''
                + ", age=" + age
                + ", weight=" + weight + '}';
    }
}
