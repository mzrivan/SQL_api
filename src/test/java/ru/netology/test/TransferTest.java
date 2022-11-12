package ru.netology.test;

import org.apache.commons.dbutils.QueryRunner;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import ru.netology.data.*;

import java.sql.DriverManager;
import java.sql.SQLException;

import static ru.netology.data.DataHelper.getValidCards;

public class TransferTest {

   // User user;

    @Test
    void shouldAuthorizationValidUserValidCode() throws SQLException {

        User user = DataHelper.getValidUser();
        getValidCards(user);
    }


    @AfterAll
    public static void deleteData() throws SQLException {
        var delTableTransfer = "DELETE FROM card_transactions;";
        var delTableCard = "DELETE FROM cards;";
        var delTableCode = "DELETE FROM auth_codes;";
        var delTableUser = "DELETE FROM users;";
        var runner = new QueryRunner();
        try (
                var conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/app", "app", "pass"
                );
        ) {
            runner.update(conn, delTableTransfer);
            runner.update(conn, delTableCard);
            runner.update(conn, delTableCode);
            runner.update(conn, delTableUser);
        }
    }
}
