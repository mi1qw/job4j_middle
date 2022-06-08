package ru.job4j.nonblockingcache;

import ru.job4j.nonblockingcache.NonBlockingCache.Base;

import java.util.List;

public interface NonBlockingCacheInt {
    /**
     * Add.
     *
     * @param model the model
     * @return Base
     */
    Base add(Base model);

    /**
     * Update.
     *
     * @param model the model
     */
    void update(Base model);

    /**
     * Delete boolean.
     *
     * @param model the model
     * @return the boolean
     */
    Base delete(Base model);


    /**
     * Gets all.
     *
     * @return the all
     */
    List<Base> getAll();
}
