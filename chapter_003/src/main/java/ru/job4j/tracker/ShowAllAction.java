package ru.job4j.tracker;

public class ShowAllAction implements UserAction {

    @Override
    public String name() {
        return "Показать все заявки";
    }

    @Override
    public boolean execute(final Input input, final Store memTracker) {
        System.out.println("==== Показать все заявки ====");
        memTracker.findAll();
        return true;
    }
}
