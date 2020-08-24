package ru.job4j.immutablelinked;

import javax.annotation.concurrent.ThreadSafe;

public class Linked {
    @ThreadSafe
    public class Node<T> {
        private final Node<T> next;
        private final T value;

        public Node(final Node<T> next, final T value) {
            this.next = next;
            this.value = value;
        }

        /**
         * Gets next.
         *
         * @return the next
         */
        public Node<T> getNext() {
            return next;
        }

        /**
         * Gets value.
         *
         * @return the value
         */
        public T getValue() {
            return value;
        }
    }
}
