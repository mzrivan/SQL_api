package ru.netology.test;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import ru.netology.data.User;
import lombok.Value;

import static io.restassured.RestAssured.given;

public class LoginAPi {
    private static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    private static void sendRequest(LoginDto user) {
        given() // "дано"
                .spec(requestSpec) // указываем, какую спецификацию используем
                .body(user) // передаём в теле объект, который будет преобразован в JSON
                .when() // "когда"
                .post("/api/auth") // на какой путь, относительно BaseUri отправляем запрос
                .then() // "тогда ожидаем"
                .statusCode(200); // код 200 OK
    }

    public static class Login {

        public static LoginDto getUser(User user) {
            return new LoginDto(
                    user.getLogin(),
                    user.getPassword()
            );
        }

        public static void loginUser(User user) {
            var registeredUser = getUser(user);
            sendRequest(registeredUser);
        }
    }

    @Value
    public static class LoginDto {
        String login;
        String password;
    }
}
