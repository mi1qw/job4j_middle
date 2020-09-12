package ru.job4j.emailnotification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EmailNotification {
    public static final Logger LOGGER = LoggerFactory.getLogger(EmailNotification.class);
    public static final String SUBJECT = "Notification {username} to email {email}";
    public static final String BODY = "Add a new event to {username}";
    public static final String LN = System.lineSeparator();
    private final ExecutorService pool = Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors());

    /**
     * Email to.
     *
     * @param user the user
     */
    public void emailTo(final User user) {
        pool.submit(() -> {
            String body = BODY.replace("{username}", user.username);
            String subject = SUBJECT.replace("{username}", user.username).
                    replace("{email}", user.email);
            send(subject, body, user.email);
        });
    }

    private void send(final String subject, final String body, final String email) {
        System.out.println(String.format("%s%s%s%s%s%s%s%s", Thread.currentThread().getName(), LN,
                subject, LN, body, LN, email, LN));
    }

    /**
     * Close.
     */
    void close() {
        pool.shutdown();
        while (!pool.isTerminated()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                LOGGER.error(e.getMessage(), e);
                Thread.currentThread().interrupt();
            }
        }
    }

    public static class User {
        private String username;
        private String email;

        public User(final String username, final String email) {
            this.username = username;
            this.email = email;
        }
    }
}
