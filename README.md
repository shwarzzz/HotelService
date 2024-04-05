# Hotel Service

В данном проекте представлена реализация сервиса для заселения, выселения, регистрации и удаления пользователей в отеле. В качестве СУБД использовалась PostgreSQL. Проект написан с использованием JDK 21, Spring Context и ORM Hibernate. 

## Требования к проекту

- Программа должна быть разработана на языке Java версии 21;
- Должен присутствовать следующий функционал:
  - Создание нового пользователя в базе;
  - Удаление пользователя из базы по ID;
  - Иметь возможность просмотреть списки всех пользователей, всех номеров в отелях и свободных номеров в конкректных отелях;
  - Использовать пессимистическую блокировку;
  - Подготовить простой консольный интерфейс;
- Подготовить sql скрипты для создания таблиц и вставки тестовых данных;

## Настройка

Для начала необходимо открыть файл `src/main/resources/my.hiber.cfg.xml`. В нем необходимо указать свои данные для URL, username, password базы данных.

Также можно запустить скрипт `/src/main/resources/data.sql` для вставки данных. 

## Запуск и установка приложения

Сначала необходимо собрать архив с приложением. Для этого выполните следующую команду:
```bash
mvn clean package
```
После успешной сборки для запуска выполните следующую команду:
```bash
java -jar target/hotel-service.jar
```
