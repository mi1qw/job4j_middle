package ru.job4j.nonblockingcache;

import org.junit.FixMethodOrder;
import org.junit.Test;
import ru.job4j.nonblockingcache.NonBlockingCache.Base;

@FixMethodOrder
public class NonBlockingCacheTest {
    private NonBlockingCache cache = new NonBlockingCache();

    @Test
    public void a1add() {
        cache.add(new Base(1, String.valueOf(Math.random())));
        for (int i = 0; i < 1000; i++) {
            new Thread(
                    () -> {
                        cache.update(new Base(1, String.valueOf(Math.random())));
                    }
            ).start();
        }
        System.out.println(cache.getAll());
    }
}

