package ru.job4j.tracker;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.StringJoiner;

import static org.junit.Assert.assertEquals;

public class ShowAllActionTest {
    @Test
    public void whenCheckOutput() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream def = System.out;
        System.setOut(new PrintStream(out));
        Store memTracker = (Store) new MemTracker();
        Item item = new Item("fix bug");
        memTracker.add(item);
        ShowAllAction act = new ShowAllAction();
        act.execute(new StubInput(new String[] {}), memTracker);
        String expect = new StringJoiner(System.lineSeparator(), "", System.lineSeparator())
                .add("==== Показать все заявки ====")
                .add(item.getId() + " " + item.getName())
                .toString();
        assertEquals(Arrays.toString(expect.getBytes()),
                Arrays.toString(out.toString().getBytes()));
        System.setOut(def);
    }
}
