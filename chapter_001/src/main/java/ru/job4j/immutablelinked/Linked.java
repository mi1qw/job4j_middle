package ru.job4j.immutablelinked;

import javax.annotation.concurrent.NotThreadSafe;

public class Linked {
    @NotThreadSafe
    public class Node<T> {
        private volatile Node next;
        private volatile T value;

        /**
         * Gets next.
         *
         * @return the next
         */
        public Node getNext() {
            return next;
        }

        /**
         * Sets next.
         *
         * @param next the next
         */
        public void setNext(final Node next) {
            this.next = next;
        }

        /**
         * Gets value.
         *
         * @return the value
         */
        public T getValue() {
            return value;
        }

        /**
         * Sets value.
         *
         * @param value the value
         */
        public void setValue(final T value) {
            this.value = value;
        }
    }
}
