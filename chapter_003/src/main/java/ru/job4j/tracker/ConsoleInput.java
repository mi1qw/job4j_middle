package ru.job4j.tracker;

import java.util.Scanner;

public class ConsoleInput implements Input {
    /**
     * get scanner.
     */
    private Scanner scanner = new Scanner(System.in);

    @Override
    public String askStr(final String question) {
        System.out.print(question);
        return scanner.nextLine();
    }

    @Override
    public int askInt(final String question) {
        return Integer.parseInt(askStr(question));
    }

    @Override
    public int askInt(final String question, final int max) {
        int select = askInt(question);
        if (select < 0 || select >= max) {
            throw new IllegalStateException(
                    String.format("Out of about %s > [0, %s]", select, max));
        }
        return select;
    }
}
