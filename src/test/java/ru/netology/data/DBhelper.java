package ru.netology.data;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static java.lang.Integer.parseInt;

public class DBhelper {

    private static QueryRunner runner = new QueryRunner();

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/app", "app", "pass");
    }

    public static void deleteData() {
        var delTableTransfer = "DELETE FROM card_transactions;";
        var delTableCard = "DELETE FROM cards;";
        var delTableCode = "DELETE FROM auth_codes;";
        var delTableUser = "DELETE FROM users;";
        try (Connection conn = getConnection()) {
            runner.update(conn, delTableTransfer);
            runner.update(conn, delTableCard);
            runner.update(conn, delTableCode);
            runner.update(conn, delTableUser);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void queryUpdate(String sql, String[] object) {
        try (Connection conn = getConnection()) {
            runner.update(conn, sql, object[0], object[1], object[2], object[3]);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static String getVerificationCode(User user) {
        var sql = "SELECT code FROM auth_codes WHERE user_id = ?";
        String code = null;
        try (Connection conn = getConnection()) {
            code = runner.query(conn, sql, new ScalarHandler<>(), user.getId());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return code;
    }

    public static int getCurrentBalance(String cardNumber) {
        var dataSQLId = "SELECT balance_in_kopecks FROM cards WHERE number =?";
        int balance = 0;
        try (Connection conn = getConnection()) {
            balance = runner.query(conn, dataSQLId, new ScalarHandler<>(), cardNumber);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return balance;
    }
}
