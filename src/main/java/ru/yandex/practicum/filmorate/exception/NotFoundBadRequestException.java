package ru.yandex.practicum.filmorate.exception;

public class NotFoundBadRequestException extends NotFoundException {
    public NotFoundBadRequestException(String message) {
        super(message);
    }
}
