package ru.job4j.ajax.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

class MyJson {
    public static final Logger LOGGER = LoggerFactory.getLogger(GreetingServlet.class);

    GreetingServlet.User fromJson(final String res) {
        ObjectMapper mapper = new ObjectMapper();
        GreetingServlet.User user = null;
        try {
            user = mapper.readValue(res, GreetingServlet.User.class);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return user;
    }

    String toJson(final GreetingServlet.User clas) {
        ObjectMapper mapper = new ObjectMapper();
        String user = null;
        try {
            user = mapper.writeValueAsString(clas);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return user;
    }
}
