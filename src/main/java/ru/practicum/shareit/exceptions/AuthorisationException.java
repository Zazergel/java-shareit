package ru.practicum.shareit.exceptions;

public class AuthorisationException extends RuntimeException {
    public AuthorisationException(String message) {
        super(message);
    }
}
