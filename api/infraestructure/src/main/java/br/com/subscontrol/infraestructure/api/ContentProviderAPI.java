package br.com.subscontrol.infraestructure.api;

import br.com.subscontrol.domain.pagination.Pagination;
import br.com.subscontrol.infraestructure.provider.content.models.ContentProviderListResponse;
import br.com.subscontrol.infraestructure.provider.content.models.ContentProviderResponse;
import br.com.subscontrol.infraestructure.provider.content.models.CreateContentProviderRequest;
import br.com.subscontrol.infraestructure.provider.content.models.UpdateContentProviderRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(value = "contentproviders")
@Tag(name = "ContentProvider")
public interface ContentProviderAPI {

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Create a new content provider")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created successfully"),
            @ApiResponse(responseCode = "422", description = "A validation error was thrown"),
            @ApiResponse(responseCode = "500", description = "An internal server was thrown"),
    })
    ResponseEntity<?> createContentProvider(@RequestBody final CreateContentProviderRequest input);

    @GetMapping
    @Operation(summary = "List all content providers paginated")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listed successfully"),
            @ApiResponse(responseCode = "422", description = "A invalid parameter was received"),
            @ApiResponse(responseCode = "500", description = "An internal server was thrown"),
    })
    Pagination<ContentProviderListResponse> listContentProviders(
            @RequestParam(name = "search", required = false, defaultValue = "") final String search,
            @RequestParam(name = "page", required = false, defaultValue = "0") final int page,
            @RequestParam(name = "perPage", required = false, defaultValue = "10") final int perPage,
            @RequestParam(name = "sort", required = false, defaultValue = "name") final String sort,
            @RequestParam(name = "dir", required = false, defaultValue = "asc") final String direction
    );

    @GetMapping(
            value = "{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Get a content provider by it's identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Content Provider retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Content Provider was not found"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown"),
    })
    ContentProviderResponse getById(@PathVariable(name = "id") String id);

    @PutMapping(
            value = "{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Update a content provider by it's identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Content Provider updated successfully"),
            @ApiResponse(responseCode = "404", description = "Content Provider was not found"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown"),
    })
    ResponseEntity<?> updateById(@PathVariable(name = "id") String id, @RequestBody UpdateContentProviderRequest input);

    @DeleteMapping(
            value = "{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a content provider by it's identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Content Provider deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Content Provider was not found"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown"),
    })
    void deleteById(@PathVariable(name = "id") String id);
    
}
