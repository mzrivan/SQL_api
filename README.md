## Задача №2: backend vs frontend (необязательная)

Бэкенд-разработчики сказали, что они всё уже сделали, это фронтендщики тормозят. Поэтому функцию перевода денег с карты на карту мы протестировать через веб-интерфейс не можем.

Зато они выдали нам описание REST API, которое позволяет это сделать, использовать нужно тот же `app-deadline.jar`.

Вот описание API:

- Логин
```http
POST http://localhost:9999/api/auth
Content-Type: application/json
{
  "login": "vasya",
  "password": "qwerty123"
}
```

- Верификация
```http
POST http://localhost:9999/api/auth/verification
Content-Type: application/json
{
  "login": "vasya",
  "code": "599640"
}
```
В ответе, в поле «token» придёт токен аутентификации, который нужно использовать в последующих запросах.

<details>
<summary>Подсказка по REST-assured</summary>

Если вам приходит в ответ следующий JSON:
```json
{
  "status": "ok"
}
```

то вы можете вытащить значение из ответа с помощью REST-assured следующим образом:

```java
      String status = ... // ваш обычный запрос  
      .then()
          .statusCode(200)
      .extract()
          .path("status")
      ;
      // используются matcher'ы Hamcrest
      assertThat(status, equalTo("ok"));
```

Если вам нужно вытащить весь ответ, чтобы потом искать по нему, например, если нужно несколько полей, то:

```java
      Response response = ... // ваш обычный запрос  
      .then()
          .statusCode(200)
      .extract()
          .response()
      ;
      String status = response.path("status");
      // используются matcher'ы Hamcrest
      assertThat(status, equalTo("ok"));
```

</details>

- Просмотр карт
```http
GET http://localhost:9999/api/cards
Content-Type: application/json
Authorization: Bearer {{token}}
```

Где {{token}} — это значение «token» с предыдущего шага. Фигурные скобки писать не нужно.

- Перевод с карты на карту (любую)
```
POST http://localhost:9999/api/transfer
Content-Type: application/json
Authorization: Bearer {{token}}
{
  "from": "5559 0000 0000 0002",
  "to": "5559 0000 0000 0008",
  "amount": 5000
}
```

Внимательно изучите запросы и ответы и, используя любой инструмент, который вам нравится, реализуйте тесты API.

[Репорты по выявленным дефектам приложения](https://github.com/mzrivan/SQL_api/issues)
