package ru.yandex.practicum.filmorate.validation.exceptions;

import lombok.Getter;
import ru.yandex.practicum.filmorate.validation.error.ValidationErrorResponse;

@Getter
public class ValidationRequestException extends RuntimeException {
    private final ValidationErrorResponse validationErrorResponse;

    public ValidationRequestException(final ValidationErrorResponse validationErrorResponse) {
        this.validationErrorResponse = validationErrorResponse;
    }
}
