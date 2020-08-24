package ru.job4j.withoutsharedresources;

public class ShareNotSafe {
    public static void main(final String[] args) throws InterruptedException {
        UserCache cache = new UserCache();
        User user = User.of("name");
        cache.add(user);
        User user1 = User.of("FIO");
        cache.add(user1);
        Thread first = new Thread(
                () -> {
                    user.setName("rename");
                    user1.setName("rename1");
                }
        );
        first.start();
        first.join();
        System.out.println(cache.findById(1).getName());
        cache.findAll().forEach(n -> System.out.println(n.getName()));
    }
}
