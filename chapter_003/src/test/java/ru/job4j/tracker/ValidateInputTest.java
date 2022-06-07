package ru.job4j.tracker;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ValidateInputTest {
    @Test
    public void whenInvalidInput() {
        ByteArrayOutputStream mem = new ByteArrayOutputStream();
        PrintStream out = System.out;
        System.setOut(new PrintStream(mem));
        ValidateInput input = new ValidateInput(
                new StubInput(new String[]{"on", "1"})
        );
        input.askInt("Enter");
        assertThat(
                mem.toString(),
                is("Please enter validate data again." + System.lineSeparator())
        );
        System.setOut(out);
    }

    @Test
    public void whenInvalidMaxInput() {
        ByteArrayOutputStream mem = new ByteArrayOutputStream();
        PrintStream out = System.out;
        System.setOut(new PrintStream(mem));
        ValidateInput input = new ValidateInput(
                new StubInput(new String[]{"6", "2"})
        );
        input.askInt("select", 6);
        assertThat(
                mem.toString(),
                is("Please select key from menu." + System.lineSeparator())
        );
        System.setOut(out);
    }
}
