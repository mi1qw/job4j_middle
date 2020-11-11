package ru.job4j.videocameradata;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

class DataAndURL {
    public static final Logger LOGGER = LoggerFactory.getLogger(DataAndURL.class);

    String jsonFromURL(final URL url) {
        String res = "null";
        try (InputStream in = url.openStream()) {
            res = new String(in.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return res;
    }

    <T> T get(final String strurl, final Class<T> tClass) {
        String json = jsonFromURL(strToURL(strurl));
        return jsonToClass(json, tClass);
    }

    <T> T jsonToClass(final String json, final Class<T> tClass) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode node = mapper.readTree(json);
            return mapper.treeToValue(node, tClass);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    URL strToURL(final String str) {
        URL url = null;
        try {
            url = new URL(str);
        } catch (MalformedURLException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return url;
    }

    DataURLCameras[] arrayFromJson(final String str) {
        ObjectMapper mapper = new ObjectMapper();
        DataURLCameras[] array = null;
        try {
            array = mapper.readValue(str, DataURLCameras[].class);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return array;
    }

    /**
     * Get list of Cameras.
     *
     * @return arrayCameras
     */
    DataURLCameras[] getlist(final URL urlCameras) {
        String json = jsonFromURL(urlCameras);
        return arrayFromJson(json);
    }
}
