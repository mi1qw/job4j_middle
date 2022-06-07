package ru.job4j.nonblockingcache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NonBlockingCache implements NonBlockingCacheInt {
    public static final Logger LOGGER = LoggerFactory.getLogger(NonBlockingCache.class);
    private final Map<Integer, Base> cache = new ConcurrentHashMap<>();

    /**
     * Add.
     *
     * @param model the model
     */
    @Override
    public Base add(final Base model) {
        return cache.computeIfAbsent(model.id, k -> model);
    }

    /**
     * Update.
     *
     * @param model the model
     */
    @Override
    public void update(final Base model) {
        cache.computeIfPresent(model.id, (k, v) -> {
            LOGGER.info("{} <- {}{}", v.name, model.name, System.lineSeparator());
            if (v.version != model.version) {
                throw new OptimisticException("Throw Exception in Thread");
            }
            ++v.version;
            v.name = model.name;
            return v;
        });
    }

    /**
     * Delete boolean.
     *
     * @param model the model
     * @return the boolean
     */
    @Override
    public Base delete(final Base model) {
        return cache.remove(model.id);
    }

    /**
     * Gets all.
     *
     * @return the all
     */
    @Override
    public List<Base> getAll() {
        return new ArrayList<>(cache.values());
    }

    public static class Base {
        private final int id;
        private String name;
        private int version;

        Base(final int id, final String name, final int version) {
            this.id = id;
            this.name = name;
            this.version = version;
        }

        /**
         * Gets name.
         *
         * @return the name
         */
        public String getName() {
            return name;
        }

        /**
         * Gets version.
         *
         * @return the version
         */
        public int getVersion() {
            return version;
        }

        /**
         * Returns a string representation of the object.
         *
         * @return a string representation of the object.
         */
        @Override
        public String toString() {
            return String.format("%s %s %s", id, name, version);
        }
    }
}
