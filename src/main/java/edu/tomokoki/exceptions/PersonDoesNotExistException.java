package edu.tomokoki.exceptions;

public class PersonDoesNotExistException extends RuntimeException {
    public PersonDoesNotExistException() {
        super();
    }

    public PersonDoesNotExistException(String message) {
        super(message);
    }
}
