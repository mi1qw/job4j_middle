package ru.job4j.threadsafedynamiclist;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.Iterator;

@ThreadSafe
public class SingleLockList<T> implements Iterable<T> {
    @GuardedBy("this")
    private SimpleArrayList<T> list = new SimpleArrayList<>();

    /**
     * Add.
     *
     * @param value the value
     */
    public synchronized void add(final T value) {
        list.add(value);
    }

    /**
     * Get t.
     *
     * @param index the index
     * @return the t
     */
    public synchronized T get(final int index) {
        return list.get(index);
    }

    /**
     * fail-safe Iterator.
     *
     * @return Iterator
     */
    @Override
    public synchronized Iterator<T> iterator() {
        SimpleArrayList<T> itList = new SimpleArrayList<>();
        list.iterator().forEachRemaining(itList::add);
        return itList.iterator();
    }
}
