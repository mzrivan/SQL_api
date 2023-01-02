package ru.netology.api;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.Value;
import ru.netology.data.User;

import static io.restassured.RestAssured.given;
import static ru.netology.data.DBhelper.getVerificationCode;

public class VerificationAPI {

    private static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    private static String sendRequest(VerificationDto user) {
        return
                given() // "дано"
                        .spec(requestSpec) // указываем, какую спецификацию используем
                        .body(user) // передаём в теле объект, который будет преобразован в JSON
                        .when() // "когда"
                        .post("/api/auth/verification") // на какой путь, относительно BaseUri отправляем запрос
                        .then() // "тогда ожидаем"
                        .statusCode(200) // код 200 OK
                        .extract()
                        .path("token");
    }

    public static class Verification {

        public static VerificationDto getCode(User user) {
            return new VerificationDto(
                    user.getLogin(),
                    getVerificationCode(user)
            );
        }

        public static String getVerification(User user) {
            var verificationUser = getCode(user);
            return sendRequest(verificationUser);
        }
    }

    @Value
    public static class VerificationDto {
        String login;
        String code;
    }
}

