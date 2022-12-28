package ru.netology.test;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.Value;
import ru.netology.data.User;

import java.sql.SQLException;

import static io.restassured.RestAssured.given;
import static ru.netology.data.DataHelper.getVerificationCode;

public class VerificationAPI {

    private static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();
    private static void sendRequest(VerificationDto user) {
        given() // "дано"
                .spec(requestSpec) // указываем, какую спецификацию используем
                .body(user) // передаём в теле объект, который будет преобразован в JSON
                .when() // "когда"
                .post("/api/auth/verification") // на какой путь, относительно BaseUri отправляем запрос
                .then() // "тогда ожидаем"
                .statusCode(200); // код 200 OK
    }

    public static class Verification {

        public static VerificationDto getCode(User user) throws SQLException {
            return new VerificationDto(
                    user.getLogin(),
                    getVerificationCode(user)
            );
        }

        public static void getVerification(User user) throws SQLException {
            var verificationUser = getCode(user);
            sendRequest(verificationUser);
        }
    }

    @Value
    public static class VerificationDto {
        String login;
        String code;
    }
}

