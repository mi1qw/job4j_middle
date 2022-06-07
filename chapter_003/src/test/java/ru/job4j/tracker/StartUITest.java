package ru.job4j.tracker;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class StartUITest {

    @Test
    public void whenInit() {
        StubInput input = new StubInput(
                new String[] {"0"}
        );
        StubAction action = new StubAction();
        List<UserAction> act = new ArrayList<>();
        act.add(action);
        new StartUI().init(input, new MemTracker(), act);
        assertThat(action.isCall(), is(true));
    }
}
