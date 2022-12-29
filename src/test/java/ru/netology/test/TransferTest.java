package ru.netology.test;

import groovy.transform.TailRecursive;
import org.apache.commons.dbutils.QueryRunner;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import ru.netology.data.Cards;
import ru.netology.data.DataHelper;
import ru.netology.data.User;
import ru.netology.data.Transfer;

import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.data.DataHelper.getCurrentBalance;
import static ru.netology.data.DataHelper.getValidCards;
import static ru.netology.test.LoginAPi.Login.loginUser;
import static ru.netology.test.TransferAPI.makeTransfer;
import static ru.netology.test.VerificationAPI.Verification.getVerification;

public class TransferTest {


    @Test
    void shouldAuthorizationValidUserValidCode() throws SQLException {

        User user1 = DataHelper.getValidUser();
        getValidCards(user1);
        loginUser(user1);
        String token = getVerification(user1);
        Cards card1 = getValidCards(user1);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "1",
            "2",
    })
    void shouldTransferMoneyFromCardsOneLoginVerificationUser(int amount) throws SQLException {

        User user1 = DataHelper.getValidUser();
        getValidCards(user1);
        loginUser(user1);
        String token = getVerification(user1);
        Cards card1 = getValidCards(user1);
        Cards card2 = getValidCards(user1);

        var expectedBalanceFirstCard = Integer.parseInt(card1.getBalance()) - amount;
        var expectedBalanceSecondCard = Integer.parseInt(card2.getBalance()) + amount;
        Transfer transfer = new Transfer(card1.getNumber(), card1.getNumber() , Integer.toString(amount));
        makeTransfer(transfer, token);
        var actualBalanceFirstCard = getCurrentBalance(card1.getNumber());
        var actualBalanceSecondCard = getCurrentBalance(card2.getNumber());
        assertEquals(expectedBalanceFirstCard, actualBalanceFirstCard);
        assertEquals(expectedBalanceSecondCard, actualBalanceSecondCard);
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
