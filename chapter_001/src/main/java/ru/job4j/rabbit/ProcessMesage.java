package ru.job4j.rabbit;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.StringReader;

public class ProcessMesage {
    private String body;
    private ObjectMapper mapper = new ObjectMapper();
    private Weather weather;
    private Rabbit rabbit = Rabbit.getRabbit();

    public ProcessMesage(final String body) {
        this.body = body;
    }

    /**
     * Process.
     */
    void process() throws IOException {
        //System.out.println(body + "System.out.println(body+");
        StringReader reader = new StringReader(body);
        weather = mapper.readValue(reader, Weather.class);
        //System.out.println(weather);
        String postGet = weather.getPostGet();
        if (postGet.equals("POST")) {
            post();
        } else if (postGet.equals("GET")) {
            get();
        }
    }

    /**
     * Post.
     */
    private void post() {
        //System.out.println("post();");
        rabbit.basicPublish(
                weather.getQueue(),
                weather.getRoutingKey(),
                weather.getText());

        //System.out.println(rabbit.basicConsume("weather", "weather.town") + "  weather.town");
    }

    /**
     * Get.
     */
    private void get() throws IOException {
        String str = rabbit.basicConsume(weather.getQueue(), weather.getRoutingKey());
        System.out.println(str + "    System.out.println(\"get\")");

        //Weather messageJson = new Weather(
        //        "POST",
        //        "weather",
        //        "weather.town",
        //        "temperature +18 C");
        //StringWriter writer = new StringWriter();
        //mapper.writeValue(writer, messageJson);
        //String result = writer.toString();
        //System.out.println(result);
    }

    public static class MessageJson {
        private String postGet;
        private Weather weather;

        public MessageJson(final String postGet, final Weather weather) {
            this.postGet = postGet;
            this.weather = weather;
        }

        MessageJson() {
        }

        /**
         * Gets post get.
         *
         * @return the post get
         */
        public String getPostGet() {
            return postGet;
        }

        /**
         * Gets weather.
         *
         * @return the weather
         */
        public Weather getWeather() {
            return weather;
        }

        /**
         * toString.
         *
         * @return
         */
        @Override
        public String toString() {
            return "MessageJson{" + "postGet='" + postGet + '\''
                    + ", weather=" + weather + '}';
        }
    }
}


@JsonAutoDetect
class Weather {
    private String postGet;
    private String queue;
    private String routingKey;
    private String text;

    Weather(final String postGet, final String queue, final String routingKey, final String text) {
        this.postGet = postGet;
        this.queue = queue;
        this.routingKey = routingKey;
        this.text = text;
    }

    Weather() {
    }

    public String getPostGet() {
        return postGet;
    }

    public String getQueue() {
        return queue;
    }

    public String getRoutingKey() {
        return routingKey;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "Weather{"
                + "postGet='" + postGet + '\''
                + "queue='" + queue + '\''
                + ", routingKey='" + routingKey + '\''
                + ", text='" + text + '\'' + '}';
    }
}