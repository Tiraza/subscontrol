package br.com.subscontrol.domain.tier;

import br.com.subscontrol.domain.utils.ValidationUtils;
import br.com.subscontrol.domain.validation.ErrorMessage;
import br.com.subscontrol.domain.validation.ValidationHandler;
import br.com.subscontrol.domain.validation.Validator;

public class TierValidator extends Validator {

    private final Tier tier;

    protected TierValidator(final Tier tier, final ValidationHandler handler) {
        super(handler);
        this.tier = tier;
    }

    @Override
    public void validate() {
        ValidationUtils.checkStringConstraints(tier.getTitle(), "title", validationHandler());
        ValidationUtils.checkIdentifierConstraints(tier.getSubProviderID(), "ProviderID", validationHandler());

        if (!tier.getSubTiers().isEmpty() && tier.getSubTiers().contains(tier.getId())) {
            validationHandler().append(new ErrorMessage("cyclic dependency in tier"));
        }
    }
}
