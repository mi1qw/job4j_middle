package ru.job4j.tracker;

public class StubInput implements Input {
    private String[] answers;
    private int position = 0;

    public StubInput(final String[] answers) {
        this.answers = answers;
    }

    @Override
    public String askStr(final String question) {
        return answers[position++];
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
