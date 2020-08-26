package ru.job4j.userstore;

import ru.job4j.userstore.UserStore.User;

public interface Store {
    /**
     * Add boolean.
     *
     * @param user the user
     * @return the boolean
     */
    boolean add(User user);

    /**
     * Update boolean.
     *
     * @param user the user
     * @return the boolean
     */
    boolean update(User user);

    /**
     * Delete boolean.
     *
     * @param user the user
     * @return the boolean
     */
    boolean delete(User user);

    /**
     * Transfer.
     *
     * @param fromId the from id
     * @param toId   the to id
     * @param amount the amount
     */
    void transfer(int fromId, int toId, int amount);

    /**
     * Find by id user.
     *
     * @param id the id
     * @return the user
     */
    User findById(int id);

    /**
     * Display.
     */
    void display();
}
