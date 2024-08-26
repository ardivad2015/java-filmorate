package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.validation.error.ValidationErrorResponse;
import ru.yandex.practicum.filmorate.validation.error.Violation;
import ru.yandex.practicum.filmorate.validation.exceptions.ValidationRequestException;
import ru.yandex.practicum.filmorate.validation.exceptions.ValidationRequestNotFoundException;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ErrorHandlingControllerAdvice {

    private final Logger logger = LoggerFactory.getLogger(ErrorHandlingControllerAdvice.class);

    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse onMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        logger.error("Error while validate objects properties by controller", e);
        final List<Violation> violations = e.getBindingResult().getFieldErrors().stream()
                .map(error -> new Violation(error.getField(), error.getDefaultMessage(),
                        Objects.isNull(error.getRejectedValue()) ? null : error.getRejectedValue().toString()))
                .collect(Collectors.toList());
        return new ValidationErrorResponse(violations);
    }

    @ResponseBody
    @ExceptionHandler(ValidationRequestNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ValidationErrorResponse onValidationRequestException(ValidationRequestNotFoundException e) {
        logger.error("Error. The server cannot find the object", e);
        return e.getValidationErrorResponse();
    }

    @ResponseBody
    @ExceptionHandler(ValidationRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse onValidationRequestException(ValidationRequestException e) {
        logger.error("Error while validate objects properties by service.", e);
        return e.getValidationErrorResponse();
    }
}