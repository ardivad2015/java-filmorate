package ru.yandex.practicum.filmorate.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.yandex.practicum.filmorate.validation.validator.DateAfterValidator;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = DateAfterValidator.class)
@Documented
public @interface DateAfter {

    String message() default "Дата должна быть позже {startDate}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    String startDate();
}