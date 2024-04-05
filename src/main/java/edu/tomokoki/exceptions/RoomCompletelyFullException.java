package edu.tomokoki.exceptions;

public class RoomCompletelyFullException extends RuntimeException {
    public RoomCompletelyFullException() {
        super();
    }

    public RoomCompletelyFullException(String message) {
        super(message);
    }
}
