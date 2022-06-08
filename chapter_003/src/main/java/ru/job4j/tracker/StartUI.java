package ru.job4j.tracker;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class StartUI {

    public void init(final Input input, final Store memTracker, final List<UserAction> actions) {
        boolean run = true;
        while (run) {
            this.showMenu(actions);
            int select = input.askInt("Выберите действие ", actions.size());
            UserAction action = actions.get(select);
            run = action.execute(input, memTracker);
        }
    }

    private void showMenu(final List<UserAction> actions) {
        Consumer<String> show = System.out::println;
        show.accept("Меню: ");
        for (UserAction item : actions) {
            show.accept(actions.indexOf(item) + ". " + item.name());
        }
    }

    public static void main(final String[] args) {
        Input input = new ConsoleInput();
        Input validate = new ValidateInput(input);
        try (Store tracker = new SqlTracker()) {
            tracker.init();
            List<UserAction> actions = new ArrayList<>();
            actions.add(new CreateAction());
            actions.add(new ShowAllAction());
            actions.add(new ReplaceItemAction());
            actions.add(new DeleteItemAction());
            actions.add(new FindItemByIdAction());
            actions.add(new FindItemByNameAction());
            actions.add(new ExitAction());
            new StartUI().init(validate, tracker, actions);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

