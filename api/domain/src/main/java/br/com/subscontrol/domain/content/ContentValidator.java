package br.com.subscontrol.domain.content;

import br.com.subscontrol.domain.utils.ValidationUtils;
import br.com.subscontrol.domain.validation.ValidationHandler;
import br.com.subscontrol.domain.validation.Validator;

public class ContentValidator extends Validator {

    private final Content content;

    protected ContentValidator(final Content content, final ValidationHandler handler) {
        super(handler);
        this.content = content;
    }

    @Override
    public void validate() {
        ValidationUtils.checkStringConstraints(content.getLabel(), "label", validationHandler());
    }
}
