package br.com.subscontrol.infraestructure.api;

import br.com.subscontrol.domain.pagination.Pagination;
import br.com.subscontrol.infraestructure.provider.sub.models.CreateSubProviderRequest;
import br.com.subscontrol.infraestructure.provider.sub.models.SubProviderListResponse;
import br.com.subscontrol.infraestructure.provider.sub.models.SubProviderResponse;
import br.com.subscontrol.infraestructure.provider.sub.models.UpdateSubProviderRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(value = "subproviders")
@Tag(name = "SubProvider")
public interface SubProviderAPI {

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Create a new sub provider")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created successfully"),
            @ApiResponse(responseCode = "422", description = "A validation error was thrown"),
            @ApiResponse(responseCode = "500", description = "An internal server was thrown"),
    })
    ResponseEntity<?> createSubProvider(@RequestBody final CreateSubProviderRequest input);

    @GetMapping
    @Operation(summary = "List all sub providers paginated")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listed successfully"),
            @ApiResponse(responseCode = "422", description = "A invalid parameter was received"),
            @ApiResponse(responseCode = "500", description = "An internal server was thrown"),
    })
    Pagination<SubProviderListResponse> listSubProviders(
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
    @Operation(summary = "Get a sub provider by it's identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sub Provider retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Sub Provider was not found"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown"),
    })
    SubProviderResponse getById(@PathVariable(name = "id") String id);

    @PutMapping(
            value = "{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Update a sub provider by it's identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sub Provider updated successfully"),
            @ApiResponse(responseCode = "404", description = "Sub Provider was not found"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown"),
    })
    ResponseEntity<?> updateById(@PathVariable(name = "id") String id, @RequestBody UpdateSubProviderRequest input);

    @DeleteMapping(
            value = "{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a sub provider by it's identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Sub Provider deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Sub Provider was not found"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown"),
    })
    void deleteById(@PathVariable(name = "id") String id);

}
