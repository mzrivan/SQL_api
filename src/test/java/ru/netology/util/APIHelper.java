package ru.netology.util;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import ru.netology.data.User;
import lombok.Value;

import static io.restassured.RestAssured.given;
import static ru.netology.util.DBhelper.getVerificationCode;

public class APIHelper {
    public static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    public static void loginUser(User user) {

        given() // "дано"
                .spec(requestSpec) // указываем, какую спецификацию используем
                .body(new LoginInfo(user.getLogin(), user.getPassword())) // передаём в теле объект, который будет преобразован в JSON
                .when() // "когда"
                .post("/api/auth") // на какой путь, относительно BaseUri отправляем запрос
                .then() // "тогда ожидаем"
                .statusCode(200); // код 200 ok
    }

    public static String getVerification(User user) {
        return
                given() // "дано"
                        .spec(requestSpec) // указываем, какую спецификацию используем
                        .body(new VerificationInfo(user.getLogin(), getVerificationCode(user))) // передаём в теле объект, который будет преобразован в JSON
                        .when() // "когда"
                        .post("/api/auth/verification") // на какой путь, относительно BaseUri отправляем запрос
                        .then() // "тогда ожидаем"
                        .statusCode(200) // код 200 OK
                        .extract()
                        .path("token");
    }

    public static void makeTransfer(TransferInfo transfer, String token, int status) {
        given() // "дано"
                .spec(requestSpec) // указываем, какую спецификацию используем
                .headers(
                        "Authorization",
                        "Bearer " + token)
                .body(transfer) // передаём в теле объект, который будет преобразован в JSON
                .when() // "когда"
                .post("/api/transfer") // на какой путь, относительно BaseUri отправляем запрос
                .then() // "тогда ожидаем"
                .statusCode(status);
    }

    @Value
    public static class LoginInfo {
        String login;
        String password;
    }

    @Value
    public static class VerificationInfo {
        String login;
        String code;
    }

    @Value
    public static class TransferInfo {
        String from;
        String to;
        String amount;
    }
}
