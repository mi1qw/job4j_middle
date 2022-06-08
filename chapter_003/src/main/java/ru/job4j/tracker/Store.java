package ru.job4j.tracker;

import java.util.List;

/**
 * ru.job4j.tracker.Store .
 *
 * @author romanvohmin
 * @since 14.05.2020
 */
public interface Store extends AutoCloseable {
    void init();

    Item add(Item item);

    boolean replace(Integer id, Item item);

    boolean delete(Integer id);

    List<Item> findAll();

    List<Item> findByName(String key);

    Item findById(Integer id);
}
