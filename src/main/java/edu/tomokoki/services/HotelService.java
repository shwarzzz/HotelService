package edu.tomokoki.services;

import edu.tomokoki.entity.*;
import edu.tomokoki.exceptions.PersonCheckedIntoException;
import edu.tomokoki.exceptions.PersonDoesNotExistException;
import edu.tomokoki.exceptions.RoomCompletelyFullException;
import edu.tomokoki.exceptions.RoomDoesNoteExistException;
import jakarta.persistence.LockModeType;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class HotelService {
    private final SessionFactory sessionFactory;

    @Autowired
    public HotelService(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void createPerson(String fio, String email) {
        try (Session session = sessionFactory.getCurrentSession()) {
            Person person = new Person();
            person.setFio(fio);
            person.setEmail(email);
            try {
                session.beginTransaction();
                session.persist(person);
                session.getTransaction().commit();
                System.out.println(person + " добавлен");
            } catch (Exception e) {
                System.out.println("Не удалось добавить пользователя");
                rollbackTransaction(session.getTransaction());
            }
        }
    }

    public void deletePerson(Long personId) {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();
            try {
                Person person = (Person) session
                        .createQuery("FROM Person WHERE id = :id")
                        .setLockMode(LockModeType.WRITE)
                        .setParameter("id", personId)
                        .getSingleResult();
                if (person == null) {
                    System.out.println("Пользователь с id " + personId + " не существует");
                    throw new PersonDoesNotExistException();
                }
                System.out.println(person.getVisitsCount());
                session.remove(person);
                session.getTransaction().commit();
                System.out.println("Пользователь успешно удален");
            } catch (Exception e) {
                rollbackTransaction(session.getTransaction());
                System.out.println("Не удалось удалить пользователя");
            }
        }
    }

    public void checkPersonInto(Long personId, Long roomId) {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();
            try {
                Person person = (Person) session
                        .createQuery("FROM Person WHERE id = :id")
                        .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                        .setParameter("id", personId)
                        .getSingleResult();
                validatePersonBeforeCheckingInto(person, personId);
                Room room = (Room) session
                        .createQuery("FROM Room r WHERE id = :id")
                        .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                        .setParameter("id", roomId)
                        .getSingleResult();
                validateRoomBeforeCheckingInto(room, roomId);
                room.setCurrentGuestsCount(room.getCurrentGuestsCount() + 1);
                if (room.getCurrentGuestsCount() == room.getMaxGuests()) {
                    room.setIsAvailable(false);
                }
                person.setRoom(room);
                session.persist(createVisit(personId, room.getHotel().getId()));
                session.getTransaction().commit();
            } catch (Exception e) {
                System.out.println("Не удалось выполнить заселение");
                rollbackTransaction(session.getTransaction());
            }

        }
    }

    public void evictPerson(Long personId) {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();
            try {
                Person person = (Person) session
                        .createQuery("FROM Person WHERE id = :id")
                        .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                        .setParameter("id", personId)
                        .getSingleResult();
                checkDoesPersonExist(person, personId);
                if (person.getRoom() == null) {
                    System.out.println("Пользователь не заселен в номер");
                    session.getTransaction().commit();
                    return;
                }
                person.getRoom().setIsAvailable(true);
                person.getRoom().setCurrentGuestsCount(
                        person.getRoom().getCurrentGuestsCount() - 1);
                person.setRoom(null);
                session.getTransaction().commit();
                System.out.println("Пользователь успешно выселен");
            } catch (Exception e) {
                rollbackTransaction(session.getTransaction());
            }

        }
    }

    public void showPersons() {
        try (Session session = sessionFactory.getCurrentSession()) {
            try {
                session.beginTransaction();
                List<Person> persons = (List<Person>) session
                        .createQuery("FROM Person")
                        .setLockMode(LockModeType.PESSIMISTIC_READ)
                        .getResultList();
                System.out.println(persons.stream().map(Person::toString)
                        .collect(Collectors
                                .joining("\n", "Список пользователей:\n", "")));
                session.getTransaction().commit();
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println("Не удалось вывести список пользователей");
                rollbackTransaction(session.getTransaction());
            }
        }
    }

    public void showHotels() {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();
            try {
                List<Hotel> hotels = (List<Hotel>) session
                        .createQuery("FROM Hotel")
                        .setLockMode(LockModeType.PESSIMISTIC_READ)
                        .getResultList();
                String result = hotels.stream()
                        .map(Hotel::toString)
                        .collect(Collectors.joining("\n", "Список отелей:\n", ""));
                System.out.println(result);
                session.getTransaction().commit();
            } catch (Exception e) {
                System.out.println("Не удалось вывести список отелей");
                rollbackTransaction(session.getTransaction());
            }
        }
    }

    public void showAllRooms() {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();
            try {
                List<Room> rooms = (List<Room>) session.createQuery("FROM Room")
                        .setLockMode(LockModeType.PESSIMISTIC_READ)
                        .getResultList();
                if (rooms.isEmpty()) {
                    System.out.println("Список свободных комнат пуст");
                    session.getTransaction().commit();
                    return;
                }
                System.out.println(rooms.stream()
                        .map((Room::toString))
                        .collect(Collectors
                                .joining("\n", "Список всех доступных комнат:\n", "")));
                session.getTransaction().commit();
            } catch (Exception e) {
                System.out.println("Не удалось вывести список доступных комнат");
                rollbackTransaction(session.getTransaction());
            }
        }
    }

    public void showRoomsByHotel(String hotelName) {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();
            try {
                List<Room> rooms = session.createQuery("FROM Room r WHERE r.hotel.name = :name")
                        .setLockMode(LockModeType.PESSIMISTIC_READ)
                        .setParameter("name", hotelName)
                        .getResultList();
                if (rooms.isEmpty()) {
                    System.out.println("Нет доступных комнат для " + hotelName);
                    session.getTransaction().commit();
                    return;
                }
                System.out.println(rooms.stream()
                        .filter(Room::getIsAvailable)
                        .map(Room::toString)
                        .collect(Collectors.joining("\n", "Список всех доступных комнат:\n", "")));
                session.getTransaction().commit();
            } catch (Exception e) {
                System.out.println("Не удалось вывести список комнат для " + hotelName);
                rollbackTransaction(session.getTransaction());
            }
        }
    }

    public void showPersonVisits(Long personId) {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();
            try {
                Person person = session.get(Person.class, personId);
                checkDoesPersonExist(person, personId);
                List<Visit> visits = person.getVisits();
                if (visits.isEmpty()) {
                    System.out.println("Список посещений пуст");
                    session.getTransaction().commit();
                    return;
                }
                System.out.println("Кол-во посещений: " + person.getVisitsCount());
                System.out.println(visits.stream()
                        .map(visit -> visit.getHotel().getName())
                        .collect(Collectors.joining(
                                "\n",
                                "Список отелей, которые посетил " + person.getFio() + ":\n",
                                "")));
                session.getTransaction().commit();
            } catch (Exception e) {
                System.out.println("Не удалось вывести список посещений для пользователя");
                rollbackTransaction(session.getTransaction());
            }
        }

    }

    private void rollbackTransaction(Transaction transaction) {
        if (transaction.getStatus() == TransactionStatus.ACTIVE ||
                transaction.getStatus() == TransactionStatus.MARKED_ROLLBACK) {
            transaction.rollback();
        }
    }

    private void validatePersonBeforeCheckingInto(Person person, Long personId) {
        checkDoesPersonExist(person, personId);
        if (person.getRoom() != null) {
            System.out.println("Пользователь уже заселен в комнату с id " + person.getRoom().getId());
            throw new PersonCheckedIntoException();
        }
    }

    private void checkDoesPersonExist(Person person, Long personId) {
        if (person == null) {
            System.out.println("Пользователя с id " + personId + " не существует");
            throw new PersonDoesNotExistException();
        }
    }

    private void validateRoomBeforeCheckingInto(Room room, Long roomId) {
        if (room == null) {
            System.out.println("Комнаты с id " + roomId + " не существует");
            throw new RoomDoesNoteExistException();
        }
        if (room.getCurrentGuestsCount() == room.getMaxGuests()) {
            System.out.println("Нельзя выполнить заселение, т.к. комната полностью занята");
            throw new RoomCompletelyFullException();
        }
    }

    private Visit createVisit(Long personId, Long hotelId) {
        VisitKey key = new VisitKey();
        key.setPersonId(personId);
        key.setHotelId(hotelId);
        Visit visit = new Visit();
        visit.setVisitKey(key);
        return visit;
    }
}
