package br.com.subscontrol.domain.tier;

import br.com.subscontrol.domain.ProvidedEntity;
import br.com.subscontrol.domain.utils.InstantUtils;
import br.com.subscontrol.domain.validation.ValidationHandler;

import java.time.Instant;

public class Tier extends ProvidedEntity<TierID> {

    private String title;
    private String description;
    private String amount;

    protected Tier(
            final TierID id,
            final SubProviderID subProviderID,
            final String providedId,
            final String title,
            final String description,
            final String amount,
            final List<TierID> subTiers,
            final List<SubID> subscribers,
            final List<ContentID> contents,
            final boolean active,
            final Instant createdAt,
            final Instant updatedAt,
            final Instant deletedAt) {
        super(id, providedId, active, createdAt, updatedAt, deletedAt);
        this.subProviderID = subProviderID;
        this.title = title;
        this.description = description;
        this.amount = amount;
        this.subTiers = new ArrayList<>(subTiers != null ? subTiers : Collections.emptyList());
        this.subscribers = new ArrayList<>(subscribers != null ? subscribers : Collections.emptyList());
        this.contents = new ArrayList<>(contents != null ? contents : Collections.emptyList());
        selfValidate();
    }

    public static Tier create(
            final String subProviderID,
            final String providedId,
            final String title,
            final String description,
            final String amount) {
        final Instant now = Instant.now();
        return new Tier(
                TierID.unique(),
                SubProviderID.from(subProviderID),
                providedId,
                title,
                description,
                amount,
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                true,
                now,
                now,
                null);
    }

    public static Tier with(
            final String id,
            final String subProviderID,
            final String providedId,
            final String title,
            final String description,
            final String amount,
            final List<TierID> subTiers,
            final List<SubID> subscribers,
            final List<ContentID> contents,
            final boolean active,
            final Instant createdAt,
            final Instant updatedAt,
            final Instant deletedAt) {
        return new Tier(
                TierID.from(id),
                SubProviderID.from(subProviderID),
                providedId,
                title,
                description,
                amount,
                subTiers,
                subscribers,
                contents,
                active,
                createdAt,
                updatedAt,
                deletedAt);
    }

    public void update(
            final String title,
            final String description,
            final String amount,
            final List<TierID> subTiers,
            final List<SubID> subscribers,
            final List<ContentID> contents,
            final boolean isActive) {
        if (isActive) {
            activate();
        } else {
            deactivate();
        }
        this.title = title;
        this.description = description;
        this.amount = amount;
        this.subTiers = new ArrayList<>(subTiers != null ? subTiers : Collections.emptyList());
        this.subscribers = new ArrayList<>(subscribers != null ? subscribers : Collections.emptyList());
        this.contents = new ArrayList<>(contents != null ? contents : Collections.emptyList());
        this.updatedAt = InstantUtils.now();
        selfValidate();
    }

    public SubProviderID getSubProviderID() {
        return subProviderID;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getAmount() {
        return amount;
    }

    public List<TierID> getSubTiers() {
        return subTiers;
    }

    public List<SubID> getSubscribers() {
        return subscribers;
    }

    public List<ContentID> getContents() {
        return contents;
    }

    @Override
    public void validate(ValidationHandler handler) {
        new TierValidator(this, handler).validate();
    }

}
