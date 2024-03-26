package ru.yandex.practicum.filmorate.exceptions;

public class NoSuchObjectException extends RuntimeException {
    public NoSuchObjectException(String message) {
        super(message);
    }
}
