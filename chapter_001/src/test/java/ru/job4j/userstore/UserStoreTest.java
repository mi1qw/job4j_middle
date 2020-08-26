package ru.job4j.userstore;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.userstore.UserStore.User;

import static org.junit.Assert.assertTrue;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserStoreTest {
    public static final Logger LOGGER = LoggerFactory.getLogger(UserStoreTest.class);
    public static final String LN = System.lineSeparator();
    private final UserStore store = new UserStore();

    @Test
    public void a1add() throws InterruptedException {
        Thread ann = new Thread(() -> store.add(new User(1, "Ann", 100)),
                "new Ann");
        Thread ben = new Thread(() -> store.add(new User(2, "Ben", 150)),
                "new Ben");
        Thread zed = new Thread(() -> store.add(new User(3, "Zed", 90)),
                "new Zed");
        ann.start();
        ben.start();
        zed.start();
        ann.join();
        ben.join();
        zed.join();
        store.display();

        Thread transfer1 = new Thread(() -> store.transfer(2, 1, 70),
                "Ben->Ann");
        Thread transfer2 = new Thread(() -> store.transfer(3, 1, 95),
                "Zed->Ann Not enough money!");
        transfer1.start();
        transfer2.start();
        transfer1.join();
        transfer2.join();
        store.display();

        LOGGER.info("Zed update on 200{}", LN);
        store.update(new User(3, "Zed", 200));
        store.display();

        Thread transfer3 = new Thread(() -> store.transfer(3, 1, 95),
                "Zed->Ann");
        transfer3.start();
        transfer3.join();
        store.display();
        assertTrue(true);
    }
}
