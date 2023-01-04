package ru.netology.util;

import com.github.javafaker.Faker;
import ru.netology.data.Cards;
import ru.netology.data.User;

import static ru.netology.util.DBhelper.queryUpdate;

public class DataHelper {

    private DataHelper() {
    }

    public static User getValidUser() {
        var faker = new Faker();
        var dataSQL = "INSERT INTO users(id, login, password, status) VALUES (?, ?,?,?);";
        String id = faker.regexify("[0-9]{10}");
        String login = faker.name().username();
        String password = "$2a$10$7yHUvStzBubZ1s7kZrwYGujHyNzGt5rmv29o.vgNBkqwQ.5bzKH9i";
        String status = "active";
        String[] userParams = {id, login, password, status};
        queryUpdate(dataSQL, userParams);
        return new User(id, login, "qwerty123", status);
    }

    public static Cards getValidCard(User user) {
        var faker = new Faker();
        var dataSQL = "INSERT INTO cards(id, user_id, number, balance_in_kopecks) VALUES (?, ?,?,?);";
        String id = faker.regexify("[0-9]{10}");
        String user_id = user.getId();
        String number = faker.regexify("5559 [0-9]{4} [0-9]{4} [0-9]{4}");
        String balance_in_kopecks = faker.regexify("[0-9]{6}00");
        String[] userParams = {id, user_id, number, balance_in_kopecks};
        queryUpdate(dataSQL, userParams);
        return new Cards(
                id,
                number,
                balance_in_kopecks
        );
    }
}