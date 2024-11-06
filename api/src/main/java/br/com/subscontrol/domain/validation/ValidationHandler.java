package br.com.subscontrol.domain.validation;

import java.util.List;

public interface ValidationHandler {

    ValidationHandler append(ErrorMessage error);

    ValidationHandler append(ValidationHandler handler);

    <T> T validate(Validation<T> aValidation);

    List<ErrorMessage> getErrors();

    default boolean hasError() {
        return getErrors() != null && !getErrors().isEmpty();
    }

    default ErrorMessage firstError() {
        if (getErrors() != null && !getErrors().isEmpty()) {
            return getErrors().getFirst();
        }
        return null;
    }

    interface Validation<T> {
        T validate();
    }
}
