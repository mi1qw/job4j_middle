package ru.job4j.userstore;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@ThreadSafe
public class UserStore implements Store {
    public static final Logger LOGGER = LoggerFactory.getLogger(UserStore.class);
    public static final String LN = System.lineSeparator();
    @GuardedBy("this")
    private final ConcurrentMap<Integer, User> store = new ConcurrentHashMap<>();

    /**
     * Add boolean.
     *
     * @param user the user
     * @return the boolean
     */
    @Override
    public synchronized boolean add(final User user) {
        return store.put(user.getId(), user) != null;
    }

    /**
     * Update boolean.
     *
     * @param user the user
     * @return the boolean
     */
    @Override
    public synchronized boolean update(final User user) {
        return store.put(user.getId(), user) != null;
    }

    /**
     * Delete boolean.
     *
     * @param user the user
     * @return the boolean
     */
    @Override
    public synchronized boolean delete(final User user) {
        return store.remove(user.getId()) != null;
    }

    /**
     * Transfer.
     *
     * @param fromId the from id
     * @param toId   the to id
     * @param amount the amount
     */
    @Override
    public synchronized void transfer(final int fromId, final int toId, final int amount) {
        User userFrom = findById(fromId);
        User userTo = findById(toId);
        if (userFrom.getAmount() >= amount) {
            userFrom.changeAmount(-amount);
            userTo.changeAmount(amount);
        } else {
            LOGGER.warn("Not enough money!{}", LN);
        }
    }

    /**
     * Find by id user.
     *
     * @param id the id
     * @return the user
     */
    @Override
    public synchronized User findById(final int id) {
        return store.get(id);
    }

    /**
     * Display.
     */
    @Override
    public synchronized void display() {
        store.forEach((k, v) -> System.out.println(v));
    }

    public static class User {
        private final int id;
        private int amount;
        private final String name;

        public User(final int id, final String name, final int amount) {
            this.id = id;
            this.name = name;
            this.amount = amount;
        }

        /**
         * Change amount.
         *
         * @param sum the sum
         */
        public boolean changeAmount(final int sum) {
            int balance = amount + sum;
            if (balance >= 0) {
                amount = balance;
                LOGGER.info("User {} {} = {}{}", name,
                        sum > 0 ? "+".concat(String.valueOf(sum)) : sum,
                        amount, LN);
                return true;
            } else {
                LOGGER.warn("Not enough money!{}", LN);
                return false;
            }
        }

        /**
         * Gets id.
         *
         * @return the id
         */
        public int getId() {
            return id;
        }

        /**
         * Gets amount.
         *
         * @return the amount
         */
        public int getAmount() {
            return amount;
        }

        /**
         * Returns a string representation of the object. In general, the
         *
         * @return a string representation of the object.
         */
        @Override
        public String toString() {
            return String.format("%s %d", name, amount);
        }

        @Override
        public final boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            User user = (User) o;
            return id == user.id && name.equals(user.name);
        }

        @Override
        public final int hashCode() {
            return Objects.hash(id, name);
        }
    }
}
