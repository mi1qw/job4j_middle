package ru.job4j.tracker;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;


public class MemTrackerTest {

    /**
     * тест проверяет добавление в массив tracker, использует поиск по Id.
     */
    @Test
    public void whenAddNewItemThenTrackerHasSameItem() {
        MemTracker memTracker = new MemTracker();
        Item item = new Item("test1");
        memTracker.add(item);
        Item result = memTracker.findById(item.getId());
        assertThat(result.getName(), is(item.getName()));
    }

    /**
     * проверяем метод поиска findAll.
     */
    @Test
    public void whenAdd5ItemsAndUniqueIDForEachThenTrue() {
        MemTracker memTracker = new MemTracker();
        Item item1 = new Item("test1");
        Item item2 = new Item("test2");
        Item item3 = new Item("test4");
        Item item4 = new Item("test2");
        Item item5 = new Item("test2");

        memTracker.add(item1);
        memTracker.add(item2);
        memTracker.add(item3);
        memTracker.add(item4);
        memTracker.add(item5);
        ArrayList<Item> expected = new ArrayList<>();
        expected.add(item1);
        expected.add(item2);
        expected.add(item3);
        expected.add(item4);
        expected.add(item5);

        List<Item> result = memTracker.findAll();
        assertThat(expected, is(result));
    }

    /**
     * проверяем метод поиска по имени.
     */
    @Test
    public void whenAdd5ItemsAnd3SameNamesThenTrackerHas3ItemBySameName() {
        MemTracker memTracker = new MemTracker();
        Item item1 = new Item("test1");
        Item item2 = new Item("test2");
        Item item3 = new Item("test4");
        Item item4 = new Item("test2");
        Item item5 = new Item("test2");

        memTracker.add(item1);
        memTracker.add(item2);
        memTracker.add(item3);
        memTracker.add(item4);
        memTracker.add(item5);
        ArrayList<Item> expected = new ArrayList<>();
        expected.add(item2);
        expected.add(item4);
        expected.add(item5);
        List<Item> result = memTracker.findByName("test2");
        assertThat(expected, is(result));
    }

    /**
     * проверяем метод findById.
     */
    @Test
    public void whenAdd5ItemsAddThenTheyAllInTracker() {
        MemTracker memTracker = new MemTracker();
        Item item1 = new Item("test1");
        Item item2 = new Item("test2");
        Item item3 = new Item("test4");
        Item item4 = new Item("test2");
        Item item5 = new Item("test2");

        memTracker.add(item1);
        memTracker.add(item2);
        memTracker.add(item3);
        memTracker.add(item4);
        memTracker.add(item5);
        Item result = memTracker.findById(item5.getId());
        assertThat(item5, is(result));
    }

    /**
     * метод replace.
     */
    @Test
    public void whenReplace() {
        MemTracker memTracker = new MemTracker();
        Item bug = new Item("Bug");
        memTracker.add(bug);
        int id = bug.getId();
        Item bugWithDesc = new Item("Bug with description");
        memTracker.replace(id, bugWithDesc);
        assertThat(memTracker.findById(id).getName(), is("Bug with description"));
    }

    /**
     * тест delete.
     */
    @Test
    public void whenDelete() {
        MemTracker memTracker = new MemTracker();
        Item bug = new Item("Bug");
        memTracker.add(bug);
        int id = bug.getId();
        memTracker.delete(id);
        assertThat(memTracker.findById(id), is(nullValue()));
    }
}
