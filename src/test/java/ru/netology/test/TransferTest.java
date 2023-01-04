package ru.netology.test;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import ru.netology.data.Cards;
import ru.netology.util.DataHelper;
import ru.netology.data.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.util.APIHelper.*;
import static ru.netology.util.DBhelper.deleteData;
import static ru.netology.util.DBhelper.getCurrentBalance;
import static ru.netology.util.DataHelper.getValidCard;


public class TransferTest {

    @AfterAll
    static void deleteAllData() {
        deleteData();
    }

    @Test
    void shouldTransferMoneyFromCardsOneLoginVerificationUser() {

        int amount = 1;
        User user1 = DataHelper.getValidUser();
        loginUser(user1);
        String token = getVerification(user1);
        Cards card1 = getValidCard(user1);
        Cards card2 = getValidCard(user1);

        var expectedBalanceFirstCard = Integer.parseInt(card1.getBalance()) - amount * 100;
        var expectedBalanceSecondCard = Integer.parseInt(card2.getBalance()) + amount * 100;
        TransferInfo transfer = new TransferInfo(card1.getNumber(), card2.getNumber(), Integer.toString(amount));
        makeTransfer(transfer, token, 200);
        var actualBalanceFirstCard = getCurrentBalance(card1.getNumber());
        var actualBalanceSecondCard = getCurrentBalance(card2.getNumber());
        assertEquals(expectedBalanceFirstCard, actualBalanceFirstCard);
        assertEquals(expectedBalanceSecondCard, actualBalanceSecondCard);
    }

    @Test
    void shouldTransferMoneyFromCardsOfTwoUsers() {

        int amount = 2;
        User user1 = DataHelper.getValidUser();
        User user2 = DataHelper.getValidUser();
        loginUser(user1);
        String token = getVerification(user1);
        Cards card1 = getValidCard(user1);
        Cards card2 = getValidCard(user2);

        var expectedBalanceFirstCard = Integer.parseInt(card1.getBalance()) - amount * 100;
        var expectedBalanceSecondCard = Integer.parseInt(card2.getBalance()) + amount * 100;
        TransferInfo transfer = new TransferInfo(card1.getNumber(), card2.getNumber(), Integer.toString(amount));
        makeTransfer(transfer, token, 200);
        var actualBalanceFirstCard = getCurrentBalance(card1.getNumber());
        var actualBalanceSecondCard = getCurrentBalance(card2.getNumber());
        assertEquals(expectedBalanceFirstCard, actualBalanceFirstCard);
        assertEquals(expectedBalanceSecondCard, actualBalanceSecondCard);
    }

    @Test
    void shouldTransferAllMoneyFromCard1LoginVerificationUser() {

        User user1 = DataHelper.getValidUser();
        loginUser(user1);
        String token = getVerification(user1);
        Cards card1 = getValidCard(user1);
        Cards card2 = getValidCard(user1);

        int amount = Integer.parseInt(card1.getBalance()) / 100;
        var expectedBalanceFirstCard = Integer.parseInt(card1.getBalance()) - amount * 100;
        var expectedBalanceSecondCard = Integer.parseInt(card2.getBalance()) + amount * 100;
        TransferInfo transfer = new TransferInfo(card1.getNumber(), card2.getNumber(), Integer.toString(amount));
        makeTransfer(transfer, token, 200);
        var actualBalanceFirstCard = getCurrentBalance(card1.getNumber());
        var actualBalanceSecondCard = getCurrentBalance(card2.getNumber());
        assertEquals(expectedBalanceFirstCard, actualBalanceFirstCard);
        assertEquals(expectedBalanceSecondCard, actualBalanceSecondCard);
    }

    @Test
    void shouldNotTransferMoneyMoreThenThereIsOnBalance() {

        User user1 = DataHelper.getValidUser();
        loginUser(user1);
        String token = getVerification(user1);
        Cards card1 = getValidCard(user1);
        Cards card2 = getValidCard(user1);

        int amount = Integer.parseInt(card1.getBalance()) / 100 + 1;
        var expectedBalanceFirstCard = Integer.parseInt(card1.getBalance());
        var expectedBalanceSecondCard = Integer.parseInt(card2.getBalance());
        TransferInfo transfer = new TransferInfo(card1.getNumber(), card2.getNumber(), Integer.toString(amount));
        makeTransfer(transfer, token, 400);
        var actualBalanceFirstCard = getCurrentBalance(card1.getNumber());
        var actualBalanceSecondCard = getCurrentBalance(card2.getNumber());
        assertEquals(expectedBalanceFirstCard, actualBalanceFirstCard);
        assertEquals(expectedBalanceSecondCard, actualBalanceSecondCard);
    }

    @Test
    void shouldNotTransferNegativeValue() {

        User user1 = DataHelper.getValidUser();
        loginUser(user1);
        String token = getVerification(user1);
        Cards card1 = getValidCard(user1);
        Cards card2 = getValidCard(user1);

        int amount = -1;
        var expectedBalanceFirstCard = Integer.parseInt(card1.getBalance());
        var expectedBalanceSecondCard = Integer.parseInt(card2.getBalance());
        TransferInfo transfer = new TransferInfo(card1.getNumber(), card2.getNumber(), Integer.toString(amount));
        makeTransfer(transfer, token, 400);
        var actualBalanceFirstCard = getCurrentBalance(card1.getNumber());
        var actualBalanceSecondCard = getCurrentBalance(card2.getNumber());
        assertEquals(expectedBalanceFirstCard, actualBalanceFirstCard);
        assertEquals(expectedBalanceSecondCard, actualBalanceSecondCard);
    }

    @Test
    void shouldNotTransferZeroValue() {

        User user1 = DataHelper.getValidUser();
        loginUser(user1);
        String token = getVerification(user1);
        Cards card1 = getValidCard(user1);
        Cards card2 = getValidCard(user1);

        int amount = 0;
        var expectedBalanceFirstCard = Integer.parseInt(card1.getBalance());
        var expectedBalanceSecondCard = Integer.parseInt(card2.getBalance());
        TransferInfo transfer = new TransferInfo(card1.getNumber(), card2.getNumber(), Integer.toString(amount));
        makeTransfer(transfer, token, 400);
        var actualBalanceFirstCard = getCurrentBalance(card1.getNumber());
        var actualBalanceSecondCard = getCurrentBalance(card2.getNumber());
        assertEquals(expectedBalanceFirstCard, actualBalanceFirstCard);
        assertEquals(expectedBalanceSecondCard, actualBalanceSecondCard);
    }

    @Test
    void shouldNotTransferBetweenOneCard() {

        User user1 = DataHelper.getValidUser();
        loginUser(user1);
        String token = getVerification(user1);
        Cards card1 = getValidCard(user1);

        int amount = 1;

        TransferInfo transfer = new TransferInfo(card1.getNumber(), card1.getNumber(), Integer.toString(amount));
        makeTransfer(transfer, token, 400);

    }

    @Test
    void shouldNotTransferWithInvalidToken() {

        int amount = 1000;
        User user1 = DataHelper.getValidUser();
        loginUser(user1);
        Cards card1 = getValidCard(user1);
        Cards card2 = getValidCard(user1);

        var expectedBalanceFirstCard = Integer.parseInt(card1.getBalance());
        var expectedBalanceSecondCard = Integer.parseInt(card2.getBalance());
        TransferInfo transfer = new TransferInfo(card1.getNumber(), card2.getNumber(), Integer.toString(amount));
        makeTransfer(transfer, "12345", 401);
        var actualBalanceFirstCard = getCurrentBalance(card1.getNumber());
        var actualBalanceSecondCard = getCurrentBalance(card2.getNumber());
        assertEquals(expectedBalanceFirstCard, actualBalanceFirstCard);
        assertEquals(expectedBalanceSecondCard, actualBalanceSecondCard);
    }

    @Test
    void shouldNotTransferMoneyFromCardsOfTwoUsersByNotOwnerCard() {

        int amount = 1;
        User user1 = DataHelper.getValidUser();
        User user2 = DataHelper.getValidUser();
        loginUser(user1);
        String token = getVerification(user1);
        Cards card1 = getValidCard(user1);
        Cards card2 = getValidCard(user2);

        var expectedBalanceFirstCard = Integer.parseInt(card1.getBalance());
        var expectedBalanceSecondCard = Integer.parseInt(card2.getBalance());
        TransferInfo transfer = new TransferInfo(card2.getNumber(), card1.getNumber(), Integer.toString(amount));
        makeTransfer(transfer, token, 401);
        var actualBalanceFirstCard = getCurrentBalance(card1.getNumber());
        var actualBalanceSecondCard = getCurrentBalance(card2.getNumber());
        assertEquals(expectedBalanceFirstCard, actualBalanceFirstCard);
        assertEquals(expectedBalanceSecondCard, actualBalanceSecondCard);
    }
}
