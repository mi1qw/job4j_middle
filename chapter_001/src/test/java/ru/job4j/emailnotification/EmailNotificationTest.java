package ru.job4j.emailnotification;

import org.junit.Test;
import ru.job4j.emailnotification.EmailNotification.User;

public class EmailNotificationTest {
    @Test
    public void emailTo() throws InterruptedException {
        EmailNotification ntfcn = new EmailNotification();
        ntfcn.emailTo(new User("Ann", "ann@gmail.com"));
        ntfcn.emailTo(new User("Ben", "ben@gmail.com"));
        ntfcn.close();
    }
}
