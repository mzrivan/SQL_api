package ru.netology.data;

import com.github.javafaker.Faker;
import lombok.Data;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.DriverManager;
import java.sql.SQLException;

@Data
public class DataHelper {

    private DataHelper() {
    }

    public static User getValidUser() throws SQLException {
        var faker = new Faker();
        var runner = new QueryRunner();
        var dataSQL = "INSERT INTO users(id, login, password, status) VALUES (?, ?,?,?);";
        String id = faker.regexify("[0-9]{10}");
        String login = faker.name().username();
        String password = "$2a$10$7yHUvStzBubZ1s7kZrwYGujHyNzGt5rmv29o.vgNBkqwQ.5bzKH9i";
        String status = "active";
        try (
                var conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/app", "app", "pass"
                );
        ) {
            runner.update(conn, dataSQL, id, login, password, status);
        }
        return new User(id, login, "qwerty123", status);
    }

    public static String getVerificationCode(User user) throws SQLException {
        var runner = new QueryRunner();
        var dataSQL = "SELECT code FROM auth_codes WHERE user_id = ?";
        try (
                var conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/app", "app", "pass"
                );
        ) {
            return runner.query(conn, dataSQL, new ScalarHandler<>(), user.getId());
        }
    }

    public static Cards getValidCards(User user) throws SQLException {
        var faker = new Faker();
        var runner = new QueryRunner();
        var dataSQL = "INSERT INTO cards(id, user_id, number, balance_in_kopecks) VALUES (?, ?,?,?);";
        String id = faker.regexify("[0-9]{10}");
        String user_id = user.getId();
        String number = faker.regexify("5559 [0-9]{4} [0-9]{4} [0-9]{4}");
        String balance_in_kopecks = faker.regexify("[0-9]{10}");
        try (
                var conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/app", "app", "pass"
                );
        ) {
            runner.update(conn, dataSQL, id, user_id, number, balance_in_kopecks);
        }
        return new Cards(
                id,
                number,
                balance_in_kopecks
        );
    }

}