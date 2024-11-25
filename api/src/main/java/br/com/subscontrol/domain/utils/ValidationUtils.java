package br.com.subscontrol.domain.utils;

import br.com.subscontrol.domain.validation.ErrorMessage;
import br.com.subscontrol.domain.validation.ValidationHandler;

import java.net.URI;

public final class ValidationUtils {

    public static final int NAME_MAX_LENGTH = 255;
    public static final int NAME_MIN_LENGTH = 1;

    private ValidationUtils() {}

    public static void checkStringConstraints(final String value, final String fieldName, final ValidationHandler handler) {
        if (value == null) {
            handler.append(new ErrorMessage(String.format("'%s' should not be null", fieldName)));
            return;
        }

        if (value.isBlank()) {
            handler.append(new ErrorMessage(String.format("'%s' should not be empty", fieldName)));
            return;
        }

        final int length = value.trim().length();
        if (length > NAME_MAX_LENGTH || length < NAME_MIN_LENGTH) {
            handler.append(new ErrorMessage(String.format("'%s' must be between 1 and 255 characters", fieldName)));
        }
    }

    public static void checkUrlConstraints(final String urlString, final ValidationHandler handler) {
        try {
            new URI(urlString).toURL();
        } catch (Exception e) {
            handler.append(new ErrorMessage("URL is not valid"));
        }
    }
}
