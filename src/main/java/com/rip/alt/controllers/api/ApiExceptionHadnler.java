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

import com.rip.alt.exceptions.AuthenticationException;
import com.rip.alt.exceptions.AuthorizationException;
import jakarta.validation.ValidationException;

@ControllerAdvice
public class ApiExceptionHadnler extends ResponseEntityExceptionHandler {

    /**
     * Handle {@link ValidationException} and return a 422 response with the validation error message.
     *
     * @param ex the exception
     * @return a map containing the error message
     */
    @ExceptionHandler(value = ValidationException.class, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    protected @ResponseBody Map<String, String> handleValidationException(ValidationException ex) {
        return Collections.singletonMap("error", ex.getMessage());
    }


    /**
     * Handle {@link NoSuchElementException} and return a 404 response with the error message.
     * This exception is thrown by Spring Data JPA when it cannot find a record by id.
     *
     * @param ex the exception
     * @return a map containing the error message
     */
    @ExceptionHandler(value = NoSuchElementException.class, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected @ResponseBody Map<String, String> handleNoSuchElementException(NoSuchElementException ex) {
        return Collections.singletonMap("error", "codes [id.not_found];");
    }

    /**
     * Handle {@link AuthorizationException} and return a 403 response with the error message.
     *
     * @param ex the exception
     * @return a map containing the error message
     */
    @ExceptionHandler(value = AuthorizationException.class, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    protected @ResponseBody Map<String, String> handleAuthorizationException(AuthorizationException ex) {
        return Collections.singletonMap("error", "codes [id.not_authorized];");
    }

    /**
     * Handle {@link AuthenticationException} and return a 401 response with the error message.
     * This exception is thrown when the user is not authenticated.
     *
     * @param ex the exception
     * @return a map containing the error message
     */
    @ExceptionHandler(value = AuthenticationException.class, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    protected @ResponseBody Map<String, String> handleAuthorizationException(AuthenticationException ex) {
        return Collections.singletonMap("error", "codes [id.not_authenticated];");
    }
}
