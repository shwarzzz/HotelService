package edu.tomokoki.application;

import edu.tomokoki.services.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.InputMismatchException;
import java.util.Scanner;

@Component
public class ConsoleApplication implements Application {
    private static final int ADD_PERSON = 1;
    private static final int REMOVE_PERSON = 2;
    private static final int SHOW_ALL_PERSONS = 3;
    private static final int SHOW_HOTELS = 4;
    private static final int SHOW_ROOMS_ALL = 5;
    private static final int SHOW_ROOMS_BY_HOTEL = 6;
    private static final int CHECK_PERSON_INTO_ROOM = 7;
    private static final int EVICT_PERSON_FROM_ROOM = 8;
    private static final int SHOW_PERSON_VISITS = 9;
    private static final int EXIT = 0;
    private final HotelService service;
    private int userCommand;

    @Autowired
    public ConsoleApplication(HotelService service) {
        this.service = service;
    }

    @Override
    public void start() {
        String name;
        String email;
        long personId;
        long roomId;
        try (Scanner inp = new Scanner(System.in)) {
            while (true) {
                showMenu();
                inputUserCommand(inp);
                if (userCommand == ADD_PERSON) {
                    System.out.println("Введите ФИО:");
                    name = inp.nextLine();
                    System.out.println("Введите email:");
                    email = inp.nextLine();
                    service.createPerson(name, email);
                } else if (userCommand == REMOVE_PERSON) {
                    service.deletePerson(inputId(inp));
                } else if (userCommand == SHOW_ALL_PERSONS) {
                    service.showPersons();
                } else if (userCommand == SHOW_HOTELS) {
                    service.showHotels();
                } else if (userCommand == SHOW_ROOMS_ALL) {
                    service.showAllRooms();
                } else if (userCommand == SHOW_ROOMS_BY_HOTEL) {
                    System.out.println("Введите название отеля");
                    name = inp.nextLine();
                    service.showRoomsByHotel(name);
                } else if (userCommand == CHECK_PERSON_INTO_ROOM) {
                    System.out.println("Данные пользователя:");
                    personId = inputId(inp);
                    System.out.println("Данные номера:");
                    roomId = inputId(inp);
                    service.checkPersonInto(personId, roomId);
                } else if (userCommand == EVICT_PERSON_FROM_ROOM) {
                    System.out.println("Данные пользователя:");
                    personId = inputId(inp);
                    service.evictPerson(personId);
                } else if (userCommand == SHOW_PERSON_VISITS) {
                    service.showPersonVisits(inputId(inp));
                } else if (userCommand == EXIT) {
                    System.out.println("Работа программы завершена!");
                    break;
                } else {
                    System.out.println("Ошибка! Нет такой команды. Попробуйте снова!");
                }
            }
        }
    }

    private void showMenu() {
        System.out.println("--------MENU--------");
        System.out.println("1. Добавить пользователя");
        System.out.println("2. Удалить пользователя");
        System.out.println("3. Вывести список пользователей");
        System.out.println("4. Вывести список отелей");
        System.out.println("5. Вывести полный список номеров во всех отелях");
        System.out.println("6. Вывести список свободных номеров в конкретном отеле");
        System.out.println("7. Заселить пользователя в номер");
        System.out.println("8. Выселить пользователя из номера");
        System.out.println("9. Информация о визитах пользователя");
        System.out.println("0. Выход");
    }

    private void inputUserCommand(Scanner inp) {
        System.out.println("Введите команду:");
        while (true) {
            try {
                userCommand = inp.nextInt();
                break;
            } catch (InputMismatchException e) {
                System.out.println("Некорректный ввод команды, попробуйте снова");
            } finally {
                inp.nextLine();
            }
        }
    }

    private long inputId(Scanner inp) {
        System.out.println("Введите id:");
        while (true) {
            try {
                return inp.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Ошибка при вводе команды, попробуйте снова");
            } finally {
                inp.nextLine();
            }
        }
    }
}
