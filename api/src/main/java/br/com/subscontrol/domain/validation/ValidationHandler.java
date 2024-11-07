package br.com.subscontrol.domain.validation;

import java.util.List;

public interface ValidationHandler {

    ValidationHandler append(ErrorMessage error);

    List<ErrorMessage> getErrors();

    default boolean hasError() {
        return getErrors() != null && !getErrors().isEmpty();
    }

    interface Validation<T> {
        T validate();
    }
}
