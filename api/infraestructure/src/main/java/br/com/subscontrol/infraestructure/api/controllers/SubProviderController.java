package br.com.subscontrol.infraestructure.api.controllers;

import br.com.subscontrol.application.provider.sub.create.CreateSubProviderCommand;
import br.com.subscontrol.application.provider.sub.create.CreateSubProviderUseCase;
import br.com.subscontrol.application.provider.sub.delete.DeleteSubProviderUseCase;
import br.com.subscontrol.application.provider.sub.retrieve.get.GetSubProviderUseCase;
import br.com.subscontrol.application.provider.sub.retrieve.list.ListSubProviderUseCase;
import br.com.subscontrol.application.provider.sub.update.UpdateSubProviderCommand;
import br.com.subscontrol.application.provider.sub.update.UpdateSubProviderUseCase;
import br.com.subscontrol.domain.pagination.Pagination;
import br.com.subscontrol.domain.pagination.SearchQuery;
import br.com.subscontrol.infraestructure.api.SubProviderAPI;
import br.com.subscontrol.infraestructure.provider.sub.models.CreateSubProviderRequest;
import br.com.subscontrol.infraestructure.provider.sub.models.SubProviderListResponse;
import br.com.subscontrol.infraestructure.provider.sub.models.SubProviderResponse;
import br.com.subscontrol.infraestructure.provider.sub.models.UpdateSubProviderRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;

@RestController
public class SubProviderController implements SubProviderAPI {

    private final CreateSubProviderUseCase createSubProviderUseCase;
    private final GetSubProviderUseCase getSubProviderUseCase;
    private final UpdateSubProviderUseCase updateSubProviderUseCase;
    private final ListSubProviderUseCase listSubProviderUseCase;
    private final DeleteSubProviderUseCase deleteSubProviderUseCase;

    public SubProviderController(
            final CreateSubProviderUseCase createSubProviderUseCase,
            final GetSubProviderUseCase getSubProviderUseCase,
            final UpdateSubProviderUseCase updateSubProviderUseCase,
            final ListSubProviderUseCase listSubProviderUseCase,
            final DeleteSubProviderUseCase deleteSubProviderUseCase
    ) {
        this.createSubProviderUseCase = Objects.requireNonNull(createSubProviderUseCase);
        this.getSubProviderUseCase = Objects.requireNonNull(getSubProviderUseCase);
        this.updateSubProviderUseCase = Objects.requireNonNull(updateSubProviderUseCase);
        this.listSubProviderUseCase = Objects.requireNonNull(listSubProviderUseCase);
        this.deleteSubProviderUseCase = Objects.requireNonNull(deleteSubProviderUseCase);
    }

    @Override
    public ResponseEntity<?> createSubProvider(final CreateSubProviderRequest input) {
        final var command = CreateSubProviderCommand.with(
                input.type(),
                input.name(),
                input.baseUrl(),
                input.clientId(),
                input.clientSecret(),
                input.authorizationUrl(),
                input.tokenUrl()
        );

        final var output = this.createSubProviderUseCase.execute(command);
        return ResponseEntity.created(URI.create("/subproviders/" + output.id())).body(output);
    }

    @Override
    public Pagination<SubProviderListResponse> listSubProviders(
            final String search,
            final int page,
            final int perPage,
            final String sort,
            final String direction
    ) {
        return this.listSubProviderUseCase.execute(new SearchQuery(page, perPage, search, sort, direction))
                .map(SubProviderListResponse::from);
    }

    @Override
    public SubProviderResponse getById(final String id) {
        return SubProviderResponse.from(this.getSubProviderUseCase.execute(id));
    }

    @Override
    public ResponseEntity<?> updateById(final String id, final UpdateSubProviderRequest input) {
        final var command = UpdateSubProviderCommand.with(
                id,
                input.name(),
                input.baseUrl(),
                input.active(),
                input.clientId(),
                input.clientSecret(),
                input.authorizationUrl(),
                input.tokenUrl()
        );

        return  ResponseEntity.ok(this.updateSubProviderUseCase.execute(command));
    }

    @Override
    public void deleteById(final String id) {
        this.deleteSubProviderUseCase.execute(id);
    }
}
