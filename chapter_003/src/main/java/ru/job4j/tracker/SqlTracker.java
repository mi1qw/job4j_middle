package ru.job4j.tracker;

import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * job4j_tracker ru.job4j.tracker.SqlTracker .
 *
 * @author romanvohmin
 * @since 14.05.2020 09:43
 */
public class SqlTracker implements Store {
    private Connection cn;

    public SqlTracker() {
    }

    public SqlTracker(final Connection cn) {
        this.cn = cn;
    }

    public void init() {

        try (InputStream in = getClass().getClassLoader().getResourceAsStream(
                "app.properties")) {
            Properties config = new Properties();
            config.load(in);
            Class.forName(config.getProperty("driver-class-name"));
            cn = DriverManager.getConnection(
                    config.getProperty("url"),
                    config.getProperty("username"),
                    config.getProperty("password")
            );
            PreparedStatement st = cn.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS items (id serial primary key, name varchar(100))");
            st.executeUpdate();
            st.close();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void close() throws SQLException {
        if (cn != null) {
            cn.close();
        }
    }

    @Override
    public Item add(final Item item) {
        try (PreparedStatement ps = cn.prepareStatement("INSERT INTO items (name) VALUES (?)",
                Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, item.getName());
            ps.executeUpdate();
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    item.setId(generatedKeys.getInt(1));
                    return item;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return item;
    }

    @Override
    public boolean replace(final Integer id, final Item item) {
        boolean rsl = false;
        try (PreparedStatement ps = cn.prepareStatement("UPDATE items SET name = ? WHERE id = ?")) {
            ps.setString(1, item.getName());
            ps.setInt(2, id);
            int rows = ps.executeUpdate();
            rsl = rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rsl;
    }

    @Override
    public boolean delete(final Integer id) {
        boolean rsl = false;
        try (PreparedStatement ps = cn.prepareStatement("DELETE FROM items WHERE id = ?")) {
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            rsl = rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rsl;
    }

    @Override
    public List<Item> findAll() {
        List<Item> list = new ArrayList<>();
        try (Statement st = cn.createStatement()) {
            ResultSet resultSet = st.executeQuery("SELECT * FROM items");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                Item item = new Item(name);
                item.setId(id);
                list.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        int index = 1;
        for (Item item : list) {
            System.out.println(String.format("Заявка %d. id = %s, name = %s;", index, item.getId(),
                    item.getName()));
            index++;
        }
        return list;
    }

    @Override
    public List<Item> findByName(final String key) {
        List<Item> list = new ArrayList<>();
        try (PreparedStatement st = cn.prepareStatement("SELECT * FROM items WHERE name LIKE ?")) {
            st.setString(1, "%" + key + "%");
            ResultSet resultSet = st.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                Item item = new Item(name);
                item.setId(id);
                list.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        int index = 1;
        for (Item item : list) {
            System.out.println(String.format("Заявка %d. id = %s, name = %s;", index, item.getId(),
                    item.getName()));
            index++;
        }
        return list;
    }

    @Override
    public Item findById(final Integer id) {
        Item rsl = null;
        try (PreparedStatement st = cn.prepareStatement("SELECT * FROM items WHERE id = ?")) {
            st.setInt(1, id);
            ResultSet resultSet = st.executeQuery();
            if (resultSet.next()) {
                int itemId = resultSet.getInt("id");
                String name = resultSet.getString("name");
                rsl = new Item(name);
                rsl.setId(itemId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(rsl);
        return rsl;
    }
}
