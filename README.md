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

- `VERIFICATION_CODE` — Строка, которую должен вернуть сервер
- `GROUP_ID` — Айди группы
- `GROUP_API_KEY` — Ключ доступа сообщества
- `API_VERSION` — Версия vk api
