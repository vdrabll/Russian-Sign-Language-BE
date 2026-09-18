# API изучения русского жестового языка

Spring Boot 3 / Java 21 бэкенд для приложения в духе Duolingo: **тема → уроки → жесты**.
Словарь — это те же жесты, отдельной таблицы словаря нет. Видео в БД не кладутся, только URL.

## Как запустить

Нужны Java 21+ и Docker.

```bash
docker compose up -d postgres
```

В IntelliJ: открой `pom.xml` как Maven-проект и запусти `ru.rsl.api.RslApiApplication`.

Или из терминала, если установлен Maven:

```bash
./mvnw spring-boot:run
```

Swagger: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## Авторизация

Её нет. Создай пользователя `POST /users`, дальше во все запросы клади заголовок `X-User-Id`.

## Курс

Сиды: Введение, Приветствия, Вежливость.

Следующая тема открывается, когда закрыты все уроки предыдущей **и** сдана контрольная.

Тип пользователя — `hearing` / `deaf`.

Ошибки жеста — enum в коде (`wrong_handshape` и т.д.), в `practice_attempts` пишется JSON.
Анализ жеста пока заглушка: в URL записи добавь `fail` или `unclear`, чтобы получить ошибку.
