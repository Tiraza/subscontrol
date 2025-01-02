package br.com.subscontrol.infraestructure.configuration.usecases;

import br.com.subscontrol.application.provider.content.create.CreateContentProviderUseCase;
import br.com.subscontrol.application.provider.content.create.DefaultCreateContentProviderUseCase;
import br.com.subscontrol.application.provider.content.delete.DefaultDeleteContentProviderUseCase;
import br.com.subscontrol.application.provider.content.delete.DeleteContentProviderUseCase;
import br.com.subscontrol.application.provider.content.retrieve.get.DefaultGetContentProviderUseCase;
import br.com.subscontrol.application.provider.content.retrieve.get.GetContentProviderUseCase;
import br.com.subscontrol.application.provider.content.retrieve.list.DefaultListContentProviderUseCase;
import br.com.subscontrol.application.provider.content.retrieve.list.ListContentProviderUseCase;
import br.com.subscontrol.application.provider.content.update.DefaultUpdateContentProviderUseCase;
import br.com.subscontrol.application.provider.content.update.UpdateContentProviderUseCase;
import br.com.subscontrol.domain.provider.content.ContentProviderGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class ContentProviderUseCaseConfig {

    private final ContentProviderGateway gateway;

    public ContentProviderUseCaseConfig(final ContentProviderGateway gateway) {
        this.gateway = Objects.requireNonNull(gateway);
    }

    @Bean
    public CreateContentProviderUseCase createContentProviderUseCase() {
        return new DefaultCreateContentProviderUseCase(gateway);
    }

    @Bean
    public DeleteContentProviderUseCase deleteContentProviderUseCase() {
        return new DefaultDeleteContentProviderUseCase(gateway);
    }

    @Bean
    public GetContentProviderUseCase getContentProviderUseCase() {
        return new DefaultGetContentProviderUseCase(gateway);
    }

    @Bean
    public ListContentProviderUseCase listContentProviderUseCase() {
        return new DefaultListContentProviderUseCase(gateway);
    }

    @Bean
    public UpdateContentProviderUseCase updateContentProviderUseCase() {
        return new DefaultUpdateContentProviderUseCase(gateway);
    }
}

