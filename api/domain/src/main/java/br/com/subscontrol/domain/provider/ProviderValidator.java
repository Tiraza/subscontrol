package br.com.subscontrol.domain.provider;

import br.com.subscontrol.domain.utils.ValidationUtils;
import br.com.subscontrol.domain.validation.ValidationHandler;
import br.com.subscontrol.domain.validation.Validator;

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
    }
}
