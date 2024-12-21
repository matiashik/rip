package com.rip.alt.controllers.api;

import java.util.Collections;
import java.util.Map;
import java.util.NoSuchElementException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import jakarta.validation.ValidationException;

@ControllerAdvice
public class ApiExceptionHadnler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = ValidationException.class, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    protected @ResponseBody Map<String, String> handleValidationException(ValidationException ex) {
        return Collections.singletonMap("error", ex.getMessage());
    }

    @ExceptionHandler(value = NoSuchElementException.class, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected @ResponseBody Map<String, String> handleNoSuchElementException(NoSuchElementException ex) {
        return Collections.singletonMap("error", "codes [id.not_found];");
    }
}
