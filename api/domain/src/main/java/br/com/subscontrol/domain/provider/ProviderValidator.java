package br.com.subscontrol.domain.provider;

import br.com.subscontrol.domain.utils.ValidationUtils;
import br.com.subscontrol.domain.validation.ErrorMessage;
import br.com.subscontrol.domain.validation.ValidationHandler;
import br.com.subscontrol.domain.validation.Validator;

import java.util.Objects;

public class ProviderValidator extends Validator {

    private final Provider<?> provider;

    protected ProviderValidator(final Provider<?> provider, final ValidationHandler handler) {
        super(handler);
        this.provider = provider;
    }

    @Override
    public void validate() {
        ValidationUtils.checkStringConstraints(provider.getName(), "name", validationHandler());

        if (provider.getBaseUrl() != null && !provider.getBaseUrl().isEmpty()) {
            ValidationUtils.checkUrlConstraints(provider.getBaseUrl(), validationHandler());
        }

        if (Objects.isNull(provider.getAuthentication())) {
            validationHandler().append(new ErrorMessage("A provider must have authentication"));
        }
    }
}
