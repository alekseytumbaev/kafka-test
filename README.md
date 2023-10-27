## Запуск в докер-контейнере
1. `./gradlew bootJar`
2. `docker compose up`

## Использование
1. Подключитесь к контейнеру кафки с помощью сторонней программы или расширения в ide. Адрес: `kafka:39092`
2. Если все хорошо, вы увидите две группы: `documents-in`, `documents-out`
3. Отправьте запрос в приложение `POST http://localhost:8080/test`. Запрос отправит следующее сообщение в топик `documents-in`:
    ```json
    {
      "id": 1,
      "type": "type1",
      "organization": "org1",
      "description": "descr1",
      "patient": "patient1",
      "date": "<текущая дата в формате: 2021-01-01T00:00:00.000+0000>",
      "status": {
        "code": "IN_PROCESS",
        "name": "В процессе"
      }
    }
    ```
4. Убедитесь, что сообщение пришло в кафку и отправьте (с помощью сторонней программы) сообщение в топик `documents-out`:
    ```json
    {
      "id": 1,
      "statusCode": "ACCEPTED"
    }
    ```
    Приложение выведет лог: `log.info("RECEIVED: {}", resultDto);`


## Запуск приложения локально
1. В `docker-compose.yml` закомментируйте сервис `backend`. В сервисе `kafka` поменяйте переменную окружения:
    ```yml
    - KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://kafka:9092,PLAINTEXT_BACKEND://backend:29092,PLAINTEXT_LOCAL://localhost:39092
    ```
    на следующую:
    ```yml
    - KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://kafka:9092,PLAINTEXT_BACKEND://localhost:29092,PLAINTEXT_LOCAL://localhost:39092
    ```
2. В `src/main/resources/application.yml` поменяйте свойство
    ```yml
    spring:
      kafka:
        bootstrap-servers: kafka:29092
    ```
    на следующее:
    ```yml
    spring:
      kafka:
        bootstrap-servers: localhost:29092
    ```
3. Удалите докер-контейнеры и связанные с ними вольюмы
4. Запустите кафку `docker-compose up`
5. Запустите приложение `./gradlew bootRun`


## Проблема
При запуске приложения локально все работает, при запуске в докер-контейнере создается только топик `docements-out`,
при том приложение из него не читает. Выдается лог с ошибкой:
```
2023-10-27 05:36:27.124  INFO 1 --- [| adminclient-1] org.apache.kafka.clients.NetworkClient   : [AdminClient clientId=adminclient-1] Node 1 disconnected.
2023-10-27T05:36:27.127303040Z 2023-10-27 05:36:27.126  WARN 1 --- [| adminclient-1] org.apache.kafka.clients.NetworkClient   : [AdminClient clientId=adminclient-1] Connection to node 1 (backend/172.19.0.4:29092) could not be established. Broker may not be available.
2023-10-27T05:36:27.231483190Z 
```
Через некоторое время выдает
```
2023-10-27 05:36:56.539 ERROR 1 --- [           main] o.springframework.kafka.core.KafkaAdmin  : Could not configure topics
2023-10-27T05:36:56.541740454Z 
2023-10-27T05:36:56.541761964Z org.springframework.kafka.KafkaException: Timed out waiting to get existing topics; nested exception is java.util.concurrent.TimeoutException
2023-10-27T05:36:56.541775239Z 
```
и снова пишет в лог то же, что в начале.

Приложение также не обрабатывает http-запрос, клиент просто бесконечно ждет ответа.