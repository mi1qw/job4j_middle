package ru.job4j.visibilityparsefile;

import org.junit.Test;

import java.nio.file.Paths;

import static org.junit.Assert.assertTrue;

public class ParseFileTest {
    @Test
    public void setFile() {
        ParseFile parseFile = new ParseFile();
        parseFile.setFile(Paths.get("src/main/resources/log4j2.xml").toFile());
        parseFile.getContent();
        assertTrue(true);
    }
}
