package br.com.subscontrol.infraestructure.api;

import br.com.subscontrol.domain.exceptions.DomainException;
import br.com.subscontrol.domain.validation.ErrorMessage;

import java.util.List;

public record ApiError(String message, List<ErrorMessage> errors) {
    public static ApiError from(DomainException ex) {
        return new ApiError(ex.getMessage(), ex.getErrors());
    }
}
