package ru.yandex.practicum.filmorate.validation.exceptions;

import lombok.Getter;
import ru.yandex.practicum.filmorate.validation.error.ValidationErrorResponse;

@Getter
public class ValidationRequestNotFoundException extends ValidationRequestException {
    public ValidationRequestNotFoundException(ValidationErrorResponse validationErrorResponse) {
        super(validationErrorResponse);
    }
}
