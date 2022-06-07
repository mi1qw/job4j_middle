package ru.job4j.tracker;

public class CreateAction implements UserAction {
    @Override
    public String name() {
        return "Добавить новую заявку ";
    }

    @Override
    public boolean execute(final Input input, final Store tracker) {
        System.out.println("==== Начинаем создание заявки ====");
        String name = input.askStr("Введите имя: ");
        Item item = new Item(name);
        tracker.add(item);
        return true;
    }
}
