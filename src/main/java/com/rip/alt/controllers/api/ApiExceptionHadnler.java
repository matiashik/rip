package com.rip.alt.controllers.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class ApiExceptionHadnler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = ConstraintViolationException.class, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    protected @ResponseBody Map<String, ArrayList<String>> handleConstraintViolationException(
            ConstraintViolationException ex) {

        var errors = new HashMap<String, ArrayList<String>>();
        ex.getConstraintViolations().forEach(cv -> {
            var path = cv.getPropertyPath().toString();
            errors.computeIfAbsent(path, k -> new ArrayList<>()).add(cv.getMessage());
        });
        return errors;
    }

    @ExceptionHandler(value = NoSuchElementException.class, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected @ResponseBody Map<String, String> handleNoSuchElementException(NoSuchElementException ex) {
        return Collections.singletonMap("error", "not-found");
    }
}
