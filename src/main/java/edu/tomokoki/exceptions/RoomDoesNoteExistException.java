package edu.tomokoki.exceptions;

public class RoomDoesNoteExistException extends RuntimeException {
    public RoomDoesNoteExistException() {
        super();
    }

    public RoomDoesNoteExistException(String message) {
        super(message);
    }
}
