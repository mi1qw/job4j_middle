package ru.job4j.tracker;

public class ReplaceItemAction implements UserAction {
    @Override
    public String name() {
        return ("Редактирование заявки");
    }

    @Override
    public boolean execute(final Input input, final Store tracker) {
        System.out.println("==== Начинаем замену заявки ====");
        Integer id = Integer.parseInt(input.askStr("Введите существующий ID заявки: "));
        String name = input.askStr("Введите новое имя заявки: ");
        Item item = new Item(name);
        if (tracker.replace(id, item)) {
            System.out.println("Заявка успешно заменена");
        } else {
            System.out.println("ошибка");
        }
        return true;
    }
}
