package edu.tomokoki.exceptions;

public class PersonCheckedIntoException extends RuntimeException {
    public PersonCheckedIntoException() {
        super();
    }

    public PersonCheckedIntoException(String message) {
        super(message);
    }
}
