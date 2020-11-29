package ru.job4j.cas;

import org.junit.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class CASCountTest {
    @Test
    public void increment() throws InterruptedException {
        List<Thread> threads = new ArrayList<>();
        CASCount count = new CASCount(0);
        for (int i = 0; i < 9; i++) {
            Thread th = new Thread(count::increment);
            threads.add(th);
            th.start();
        }

        while (threads.size() > 0) {
            if (threads.get(0).isAlive()) {
                await().atMost(Duration.ofMillis(100));
            } else {
                threads.remove(0);
            }
        }

        Thread thread = new Thread(count::increment);
        thread.start();
        thread.join();
        assertThat(count.get(), is(10));
    }
}
