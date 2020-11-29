package ru.job4j.forkjoin;

import org.junit.Test;

import java.util.Optional;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.IntStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class SearchIndexTest {
    @Test
    public void search() {
        String[] strings = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        Optional<Integer> index = forkJoinPool.invoke(new SearchIndex<>(strings, "2"));
        assertThat(index, is(Optional.of(2)));

        Optional<Integer> index1 = forkJoinPool.invoke(new SearchIndex<>(
                IntStream.range(0, 100).mapToObj(Integer::toString).toArray(),
                "2"));
        assertThat(index1, is(Optional.of(2)));
    }
}
