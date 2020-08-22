package ru.job4j.threadsafedynamiclist;

import java.util.*;

public class SimpleArrayList<T> implements Iterable<T> {
    private int modCount = 0;
    private Object[] array;
    private int size;
    private static final int DEFAULT_CAPACITY = 10;

    /**
     * Конструктор.
     * Создаём Object,размерность по умолчанию или size
     *
     * @param initCapacity фиксированно, нединамическая коллекция
     */
    public SimpleArrayList(final int initCapacity) {
        if (initCapacity > 0) {
            this.array = new Object[initCapacity];
        } else {
            this.array = new Object[DEFAULT_CAPACITY];
        }
    }

    /**
     * Constructs an empty list with an initial capacity of ten.
     */
    public SimpleArrayList() {
        this.array = new Object[DEFAULT_CAPACITY];
    }

    /**
     * Конструктор.
     * Инициализация какой-то коллекцией  Collection<? extends T> array
     *
     * @param origArray the orig array
     */
    public SimpleArrayList(final Collection<? extends T> origArray) {
        this.array = origArray.toArray();
        this.size = this.array.length;
        if (size == 0) {
            this.array = new Object[DEFAULT_CAPACITY];
        }
    }

    /**
     * Get t.
     *
     * @param index the index
     * @return the t
     */
    @SuppressWarnings("unchecked")
    public T get(final int index) {
        Objects.checkIndex(index, size);
        return (T) this.array[index];
    }

    /**
     * Add.
     * При заполнении массива, созодаём новый в два раза больший
     *
     * @param model the model
     */
    public void add(final T model) {
        ++modCount;
        if (this.size == array.length) {
            this.array = Arrays.copyOf(array, array.length * 2);
        }
        array[size++] = model;
    }

    /**
     * Returns the number of elements in this list.
     *
     * @return the number of elements in this list
     */
    public int size() {
        return size;
    }

    private void checkForModification(final int expectedModCount) {
        if (modCount != expectedModCount) {
            throw new ConcurrentModificationException();
        }
    }

    /**
     * Iterator.
     *
     * @return Iterator
     */
    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private int expectedModCount = modCount;
            private int it = 0;

            /**
             * Returns {@code true} if the iteration has more elements.
             * (In other words, returns {@code true} if {@link #next} would
             * return an element rather than throwing an exception.)
             *
             * @return {@code true} if the iteration has more elements
             */
            @Override
            public boolean hasNext() {
                return it < size;
            }

            /**
             * Returns the next element in the iteration.
             *
             * @return the next element in the iteration
             * @throws NoSuchElementException if the iteration has no more elements
             */
            @SuppressWarnings("unchecked")
            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                checkForModification(expectedModCount);
                return (T) array[it++];
            }
        };
    }
}
