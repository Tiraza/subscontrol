package br.com.subscontrol.infraestructure.api.controllers;

import br.com.subscontrol.domain.exceptions.DomainException;
import br.com.subscontrol.domain.exceptions.NotFoundException;
import br.com.subscontrol.domain.validation.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = DomainException.class)
    public ResponseEntity<?> handleDomainException(final DomainException ex) {
        return ResponseEntity.unprocessableEntity().body(ApiError.from(ex));
    }

    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<?> handleNotFoundException(final NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiError.from(ex));
    }

    record ApiError(String message, List<ErrorMessage> errors) {
        static ApiError from(DomainException ex) {
            return new ApiError(ex.getMessage(), ex.getErrors());
        }
    }
}
