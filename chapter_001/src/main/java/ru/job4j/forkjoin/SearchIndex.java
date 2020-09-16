package ru.job4j.forkjoin;

import java.util.Optional;
import java.util.concurrent.RecursiveTask;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class SearchIndex<V> extends RecursiveTask<Optional<Integer>> {
    public static final int THRESHOLD = 2;
    private final V[] array;
    private int from;
    private int to;
    private V value;

    SearchIndex(final V[] array, final int from, final int to, final V value) {
        this.array = array;
        this.from = from;
        this.to = to;
        this.value = value;
    }

    SearchIndex(final V[] array, final V value) {
        this(array, 0, array.length - 1, value);
    }

    /**
     * The main computation performed by this task.
     *
     * @return the result of the computation
     */
    @Override
    protected Optional<Integer> compute() {
        if (to - from <= THRESHOLD) {
            for (int i = from; i <= to; i++) {
                if (value.equals(array[i])) {
                    return Optional.of(i);
                }
            }
            return Optional.empty();
        } else {
            int mid = (from + to) / 2;
            SearchIndex<V> left = new SearchIndex<>(array, from, mid, value);
            SearchIndex<V> righht = new SearchIndex<>(array, mid + 1, to, value);
            invokeAll(left, righht);
            return Stream.<Supplier<Optional<Integer>>>of(left::join, righht::join)
                    .map(Supplier::get)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .findFirst();
        }
    }
}
