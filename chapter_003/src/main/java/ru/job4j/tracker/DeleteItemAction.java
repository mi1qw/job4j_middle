package ru.job4j.tracker;

public class DeleteItemAction implements UserAction {
    @Override
    public String name() {
        return "Удалить заявку";
    }

    @Override
    public boolean execute(final Input input, final Store memTracker) {
        System.out.println("==== Начинаем удаление заявки ====");
        Integer id = Integer.valueOf(input.askStr("Введите существующий ID заявки: "));
        if (memTracker.delete(id)) {
            System.out.println("Заявка успешно удалена");
        } else {
            System.out.println("ошибка");
        }
        return true;
    }
}
