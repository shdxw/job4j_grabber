package ru.job4j.grabber;

import ru.job4j.html.Post;

import java.io.InputStream;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PsqlStore implements Store, AutoCloseable {

    private Connection cnn;

    public PsqlStore() {
        try (InputStream in = PsqlStore.class.getClassLoader()
                .getResourceAsStream("app.properties")) {
            Properties config = new Properties();
            config.load(in);
            Class.forName(config.getProperty("jdbc.driver"));
            cnn = DriverManager.getConnection(
                    config.getProperty("url"),
                    config.getProperty("username"),
                    config.getProperty("password")
            );
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void save(Post post) {
        try (PreparedStatement pr = cnn
                .prepareStatement("Insert into post(name, info, link, created) " +
                        "values(?, ?, ?, ?);");

        ) {
            pr.setString(1, post.getName());
            pr.setString(2, post.getText());
            pr.setString(3, post.getLink());
            pr.setTimestamp(4, Timestamp.valueOf(post.getDateTime()));
            pr.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    @Override
    public List<Post> getAll() {
        List<Post> rsl = new ArrayList<>();
        try (PreparedStatement statement = cnn.prepareStatement("select * from post;")) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    rsl.add(new Post(
                            resultSet.getString("name"),
                            resultSet.getTimestamp("created").toLocalDateTime(),
                            resultSet.getString("info"),
                            resultSet.getString("link")
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rsl;
    }

    @Override
    public Post findById(int id) {
        try (PreparedStatement statement = cnn.prepareStatement("select * from post where id = ?;")) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Post(
                            resultSet.getString("name"),
                            resultSet.getTimestamp("created").toLocalDateTime(),
                            resultSet.getString("info"),
                            resultSet.getString("link")
                    );
                } else {
                    return null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void close() throws Exception {
        if (cnn != null) {
            cnn.close();
        }
    }

    public static void main(String[] args) {
        Store store = new PsqlStore();
        store.save(new Post("rabota", LocalDateTime.now(), "test", "1job.ru"));
        System.out.println(store.getAll().toString());
        System.out.println(store.findById(1).toString());
    }
}
