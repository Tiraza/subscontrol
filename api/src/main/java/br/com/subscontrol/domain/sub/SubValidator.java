package br.com.subscontrol.domain.sub;

import br.com.subscontrol.domain.utils.ValidationUtils;
import br.com.subscontrol.domain.validation.ValidationHandler;
import br.com.subscontrol.domain.validation.Validator;

public class SubValidator extends Validator {

    private final Sub sub;

    protected SubValidator(final Sub sub, final ValidationHandler handler) {
        super(handler);
        this.sub = sub;
    }

    @Override
    public void validate() {
        ValidationUtils.checkStringConstraints(sub.getName(), "name", validationHandler());
        ValidationUtils.checkStringConstraints(sub.getEmail(), "email", validationHandler());
    }
}
