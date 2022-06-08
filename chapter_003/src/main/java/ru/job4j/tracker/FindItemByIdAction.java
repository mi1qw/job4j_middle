package ru.job4j.tracker;

public class FindItemByIdAction implements UserAction {
    @Override
    public String name() {
        return "Поиск по Id";
    }

    @Override
    public boolean execute(final Input input, final Store memTracker) {
        System.out.println("==== Начинаем поиск заявки по ID ====");
        Integer id = Integer.parseInt(input.askStr("Введите ID заявки для поиска: "));
        Item finding = memTracker.findById(id);
        if (finding != null) {
            System.out.println("Найдено " + finding.getName() + " " + finding.getId());
        } else {
            System.out.println("Ничего не найдено");
        }
        return true;
    }
}
