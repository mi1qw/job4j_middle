package ru.job4j.nonblockingcache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class NonBlockingCache implements NonBlockingCacheInt {
    public static final Logger LOGGER = LoggerFactory.getLogger(NonBlockingCache.class);
    private Map<Integer, Base> cache = new ConcurrentHashMap<>();

    /**
     * Add.
     *
     * @param model the model
     */
    @Override
    public Base add(final Base model) {
        if (!cache.containsKey(model.id)) {
            return cache.put(model.id, model);
        }
        return null;
    }

    /**
     * Update.
     *
     * @param model the model
     */
    @Override
    public void update(final Base model) {
        cache.computeIfPresent(model.id, (k, v) -> {
            int ref;
            while (true) {
                try {
                    AtomicInteger atom = v.version;
                    ref = atom.get();
                    v.name = model.name;
                    if (atom.compareAndSet(ref, ref + 1)) {
                        return v;
                    }
                    throw new OptimisticException();
                } catch (OptimisticException e) {
                    LOGGER.warn("OptimisticException");
                }
            }
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
        private AtomicInteger version;

        Base(final int id, final String name) {
            this.id = id;
            this.name = name;
            this.version = new AtomicInteger(0);
        }

        /**
         * Returns a string representation of the object.
         *
         * @return a string representation of the object.
         */
        @Override
        public String toString() {
            return String.format("%s %s", id, name);
        }
    }
}


