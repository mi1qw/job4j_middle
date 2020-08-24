package ru.job4j.withoutsharedresources;

import javax.annotation.concurrent.ThreadSafe;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@ThreadSafe
public class UserCache {
    private final ConcurrentHashMap<Integer, User> users = new ConcurrentHashMap<>();
    private final AtomicInteger id = new AtomicInteger();

    /**
     * Add.
     *
     * @param user the user
     */
    public void add(final User user) {
        users.put(id.incrementAndGet(), User.of(user.getName()));
    }

    /**
     * Find by id user.
     *
     * @param id the id
     * @return the user
     */
    public User findById(final int id) {
        return User.of(users.get(id).getName());
    }

    /**
     * Find all list.
     *
     * @return the list
     */
    public List<User> findAll() {
        return users.values().stream().
                map(n -> User.of(n.getName())).collect(Collectors.toList());
    }
}
