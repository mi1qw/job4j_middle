package ru.job4j.tracker;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.StringJoiner;

import static org.junit.Assert.assertEquals;

public class FindByNameActionTest {
    @Test
    public void whenFindByName() throws UnsupportedEncodingException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream def = System.out;
        System.setOut(new PrintStream(out));
        Store tracker = new MemTracker();
        Item item = new Item("fix bug");
        tracker.add(item);
        FindItemByNameAction act = new FindItemByNameAction();
        act.execute(new StubInput(new String[]{item.getName()}), tracker);
        String expect = new StringJoiner(System.lineSeparator(), "", System.lineSeparator())
                .add("==== Начинаем поиск заявки по имени ====")
                .add("Найдено " + item.getName() + " " + item.getId())
                .toString();
        assertEquals(Arrays.toString(expect.getBytes()),
                Arrays.toString(out.toString().getBytes()));
        System.setOut(def);
    }
}
