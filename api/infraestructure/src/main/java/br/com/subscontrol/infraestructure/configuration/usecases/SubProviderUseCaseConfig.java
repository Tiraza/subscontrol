package br.com.subscontrol.infraestructure.configuration.usecases;

import br.com.subscontrol.application.provider.sub.create.CreateSubProviderUseCase;
import br.com.subscontrol.application.provider.sub.create.DefaultCreateSubProviderUseCase;
import br.com.subscontrol.application.provider.sub.delete.DefaultDeleteSubProviderUseCase;
import br.com.subscontrol.application.provider.sub.delete.DeleteSubProviderUseCase;
import br.com.subscontrol.application.provider.sub.retrieve.get.DefaultGetSubProviderUseCase;
import br.com.subscontrol.application.provider.sub.retrieve.get.GetSubProviderUseCase;
import br.com.subscontrol.application.provider.sub.retrieve.list.DefaultListSubProviderUseCase;
import br.com.subscontrol.application.provider.sub.retrieve.list.ListSubProviderUseCase;
import br.com.subscontrol.application.provider.sub.update.DefaultUpdateSubProviderUseCase;
import br.com.subscontrol.application.provider.sub.update.UpdateSubProviderUseCase;
import br.com.subscontrol.domain.provider.sub.SubProviderGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class SubProviderUseCaseConfig {

    private final SubProviderGateway gateway;

    public SubProviderUseCaseConfig(final SubProviderGateway gateway) {
        this.gateway = Objects.requireNonNull(gateway);
    }

    @Bean
    public CreateSubProviderUseCase createSubProviderUseCase() {
        return new DefaultCreateSubProviderUseCase(gateway);
    }

    @Bean
    public DeleteSubProviderUseCase deleteSubProviderUseCase() {
        return new DefaultDeleteSubProviderUseCase(gateway);
    }

    @Bean
    public GetSubProviderUseCase getSubProviderUseCase() {
        return new DefaultGetSubProviderUseCase(gateway);
    }

    @Bean
    public ListSubProviderUseCase listSubProviderUseCase() {
        return new DefaultListSubProviderUseCase(gateway);
    }

    @Bean
    public UpdateSubProviderUseCase updateSubProviderUseCase() {
        return new DefaultUpdateSubProviderUseCase(gateway);
    }
}

