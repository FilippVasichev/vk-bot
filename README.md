## Описание
VK Bot Integration
Этот проект представляет собой интеграцию с BotAPI VK, реализованную с использованием kotlin и Spring Boot.
Бот будет цитировать присланные ему текстовые сообщения.

## Используемые технологии

- **Spring Boot**: 3.1.4
- **Kotlin**: 2.0.0
- **Retrofit**: 2.9.0
- **Ktlint**: 12.0.3
- **Тестирование**:
    - MockServer: 5.15.0
    - Testcontainers: 1.20.1
    - Rest-Assured: 5.3.2

## Настройки приложения

Конфигурация сервиса задается в файле `application.yml`:

```yaml
vk:
  baseUrl: "https://api.vk.com/method/"
  callback-verification-code: ${VERIFICATION_CODE}
  group-id: ${GROUP_ID}
  group-api-key: ${GROUP_API_KEY}
  api-version: ${API_VERSION}
```
- Ключи API можно задать через переменные окружения или заменить в файле `application.yaml.`

### Переменные окружения

Для работы сервиса необходимо задать следующие переменные окружения:

- `CALLBACK_VERIFICATION_CODE` — код подтверждения для обработки запросов от Vk CallBack Api.
- `GROUP_ID` — идентификатор группы в VK.
- `GROUP_API_KEY` — ключ API для доступа к VK API.
- `API_VERSION` — версия VK API.

Пример команд для запуска контейнера с заданием этих переменных окружения:

```
docker run -d \
  -e CALLBACK_VERIFICATION_CODE="111111111" \
  -e GROUP_ID="11111111" \
  -e GROUP_API_KEY="vk1.a.ваш токен" \
  -e API_VERSION="5.199" \
  -p 8080:8080 \
  vk-bot-backend
```

## Получение ключей API и настройка

- **Получение ключа доступа**: [Инструкция по получению сервисного ключа доступа](https://dev.vk.com/ru/api/access-token/getting-started#%D0%A1%D0%B5%D1%80%D0%B2%D0%B8%D1%81%D0%BD%D1%8B%D0%B9%20%D0%9A%D0%BB%D1%8E%D1%87%20%D0%B4%D0%BE%D1%81%D1%82%D1%83%D0%BF%D0%B0)

- **Подключение Callback API**: Для настройки и использования Callback API следуйте [инструкциям по подключению Callback API](https://dev.vk.com/ru/api/callback/getting-started#%D0%9F%D0%BE%D0%B4%D0%BA%D0%BB%D1%8E%D1%87%D0%B5%D0%BD%D0%B8%D0%B5%20Callback%20API).

## Установка и запуск

1. **Клонируйте репозиторий**:
    ```
    git clone git@github.com:FilippVasichev/vk-bot.git
    ```

2. **Перейдите в директорию проекта**:

3. **Соберите проект**:
    ```
    ./gradlew clean build
    ```

4. **Создайте Docker-образ**:
    ```
    docker build -t vk-bot-backend .
    ```

5. **Запустите Docker-контейнер**:
    ```
    docker run  \
    -e CALLBACK_VERIFICATION_CODE="111111111" \
    -e GROUP_ID="11111111" \
    -e GROUP_API_KEY="vk1.a.ваш токен" \
    -e API_VERSION="5.199" \
    -p 8080:8080 \
    vk-bot-backend
   
   Запускает контейнер из созданного образа и маппит порт 8080 внутри контейнера на порт 8080 на хосте.
    ```
