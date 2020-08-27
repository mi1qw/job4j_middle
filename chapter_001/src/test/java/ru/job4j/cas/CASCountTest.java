package ru.job4j.cas;

import org.junit.Test;

import java.time.Duration;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class CASCountTest {
    @Test
    public void increment() throws InterruptedException {
        CASCount count = new CASCount(0);
        for (int i = 0; i < 9; i++) {
            new Thread(count::increment).start();
        }
        await().atMost(Duration.ofMillis(10));
        Thread thread = new Thread(count::increment);
        thread.start();
        thread.join();
        assertThat(count.get(), is(10));
    }
}
