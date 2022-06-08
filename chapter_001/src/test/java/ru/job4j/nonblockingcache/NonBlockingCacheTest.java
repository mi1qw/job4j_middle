package ru.job4j.nonblockingcache;


import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.nonblockingcache.NonBlockingCache.Base;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;


public class NonBlockingCacheTest {
    private NonBlockingCache cache = new NonBlockingCache();
    public static final Logger LOGGER = LoggerFactory.getLogger(NonBlockingCacheTest.class);

    @Test
    public void a1update() throws InterruptedException {
        AtomicReference<Exception> ex = new AtomicReference<>();
        cache.add(new Base(1, "11", 0));
        LOGGER.info(cache.getAll().toString());
        Thread thread = new Thread(
                () -> {
                    try {
                        cache.update(new Base(1, "22", 1));
                    } catch (OptimisticException e) {
                        ex.set(e);
                    }
                }
        );
        thread.start();
        thread.join();
        LOGGER.info(cache.getAll().toString());
        assertThat(ex.get().getMessage(), is("Throw Exception in Thread"));
    }

    @Test
    public void a2add() throws InterruptedException {
        List<Base> list;
        cache.add(new Base(1, "1234", 0));
        list = cache.getAll();
        assertThat(list.size(), is(1));
        Thread thread = new Thread(
                () -> {
                    cache.update(new Base(1,
                            "test version", 0));
                }
        );
        thread.start();
        thread.join();
        Base base = cache.getAll().get(0);
        assertThat(base.getName(), is("test version"));
        assertThat(base.getVersion(), is(1));
        cache.delete(base);
        assertThat(cache.getAll().size(), is(0));
    }
}

