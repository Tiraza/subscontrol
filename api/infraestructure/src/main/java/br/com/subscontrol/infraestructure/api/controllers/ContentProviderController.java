package br.com.subscontrol.infraestructure.api.controllers;

import br.com.subscontrol.application.provider.content.create.CreateContentProviderCommand;
import br.com.subscontrol.application.provider.content.create.CreateContentProviderUseCase;
import br.com.subscontrol.application.provider.content.delete.DeleteContentProviderUseCase;
import br.com.subscontrol.application.provider.content.retrieve.get.GetContentProviderUseCase;
import br.com.subscontrol.application.provider.content.retrieve.list.ListContentProviderUseCase;
import br.com.subscontrol.application.provider.content.update.UpdateContentProviderCommand;
import br.com.subscontrol.application.provider.content.update.UpdateContentProviderUseCase;
import br.com.subscontrol.domain.pagination.Pagination;
import br.com.subscontrol.domain.pagination.SearchQuery;
import br.com.subscontrol.infraestructure.api.ContentProviderAPI;
import br.com.subscontrol.infraestructure.provider.content.models.ContentProviderListResponse;
import br.com.subscontrol.infraestructure.provider.content.models.ContentProviderResponse;
import br.com.subscontrol.infraestructure.provider.content.models.CreateContentProviderRequest;
import br.com.subscontrol.infraestructure.provider.content.models.UpdateContentProviderRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;

@RestController
public class ContentProviderController implements ContentProviderAPI {

    private final CreateContentProviderUseCase createContentProviderUseCase;
    private final GetContentProviderUseCase getContentProviderUseCase;
    private final UpdateContentProviderUseCase updateContentProviderUseCase;
    private final ListContentProviderUseCase listContentProviderUseCase;
    private final DeleteContentProviderUseCase deleteContentProviderUseCase;

    public ContentProviderController(
            final CreateContentProviderUseCase createContentProviderUseCase,
            final GetContentProviderUseCase getContentProviderUseCase,
            final UpdateContentProviderUseCase updateContentProviderUseCase,
            final ListContentProviderUseCase listContentProviderUseCase,
            final DeleteContentProviderUseCase deleteContentProviderUseCase
    ) {
        this.createContentProviderUseCase = Objects.requireNonNull(createContentProviderUseCase);
        this.getContentProviderUseCase = Objects.requireNonNull(getContentProviderUseCase);
        this.updateContentProviderUseCase = Objects.requireNonNull(updateContentProviderUseCase);
        this.listContentProviderUseCase = Objects.requireNonNull(listContentProviderUseCase);
        this.deleteContentProviderUseCase = Objects.requireNonNull(deleteContentProviderUseCase);
    }

    @Override
    public ResponseEntity<?> createContentProvider(final CreateContentProviderRequest input) {
        final var command = CreateContentProviderCommand.with(
                input.type(),
                input.name(),
                input.baseUrl(),
                input.authenticationType(),
                input.clientId(),
                input.clientSecret(),
                input.authorizationUrl(),
                input.tokenUrl(),
                input.fileBase64()
        );

        final var output = this.createContentProviderUseCase.execute(command);
        return ResponseEntity.created(URI.create("/contentproviders/" + output.id())).body(output);
    }

    @Override
    public Pagination<ContentProviderListResponse> listContentProviders(
            final String search,
            final int page,
            final int perPage,
            final String sort,
            final String direction
    ) {
        return this.listContentProviderUseCase.execute(new SearchQuery(page, perPage, search, sort, direction))
                .map(ContentProviderListResponse::from);
    }

    @Override
    public ContentProviderResponse getById(final String id) {
        return ContentProviderResponse.from(this.getContentProviderUseCase.execute(id));
    }

    @Override
    public ResponseEntity<?> updateById(final String id, final UpdateContentProviderRequest input) {
        final var command = UpdateContentProviderCommand.with(
                id,
                input.name(),
                input.baseUrl(),
                input.active(),
                input.authenticationType(),
                input.clientId(),
                input.clientSecret(),
                input.authorizationUrl(),
                input.tokenUrl(),
                input.fileBase64()
        );

        return ResponseEntity.ok(this.updateContentProviderUseCase.execute(command));
    }

    @Override
    public void deleteById(final String id) {
        this.deleteContentProviderUseCase.execute(id);
    }

}
