package dev.akif.crud;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@Validated
public abstract class CRUDController<
        I extends Serializable,
        E extends CRUDEntity<I, M>,
        M extends CRUDModel<I>,
        D extends CRUDDTO<I>,
        CM extends CRUDCreateModel<I, M, E>,
        UM extends CRUDUpdateModel<I, M, E>,
        CD extends CRUDCreateDTO<I, M, E, CM>,
        UD extends CRUDUpdateDTO<I, M, E, UM>,
        R extends CRUDRepository<I, M, E>,
        S extends CRUDService<I, M, E, CM, UM, R>> {
    protected final String modelName;
    protected final S service;

    protected CRUDController(final String modelName, final S service) {
        this.modelName = modelName;
        this.service = service;
    }

    protected abstract D toDTO(final M model);

    @ApiResponse(responseCode = CODE_CREATED, description = CREATE_RESPONSE)
    @ApiResponse(
            responseCode = CODE_CONFLICT,
            description = CONFLICT_RESPONSE,
            content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = CRUDError.class))
    )
    @Operation(summary = CREATE_SUMMARY, description = CREATE_DESCRIPTION)
    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(code = HttpStatus.CREATED)
    public D create(
            @Parameter(required = true)
            @RequestBody final CD createDTO
    ) {
        log.debug("Got request to create new {}: {}", modelName, createDTO);

        final var createModel = createDTO.toCreateModel();
        log.trace("Built Create{}: {}", modelName, createModel);

        final var model = service.create(createModel);
        log.trace("Created {}: {}", modelName, model);

        final var dto = toDTO(model);
        log.trace("Built {}DTO: {}", modelName, dto);

        return dto;
    }

    @ApiResponse(responseCode = CODE_OK, description = GET_ALL_RESPONSE)
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    @Operation(summary = GET_ALL_SUMMARY, description = GET_ALL_DESCRIPTION)
    public Paged<D> getAll(
            @Parameter(name = "page", description = PAGE_DESCRIPTION)
            @RequestParam(required = false, defaultValue = "0")
            int page,
            @Parameter(name = "perPage", description = PER_PAGE_DESCRIPTION)
            @RequestParam(required = false, defaultValue = "20")
            int perPage
    ) {
        final var pageRequest = PageRequest.of(page, perPage);
        log.debug("Got request to get {} {}", modelName, pageRequest);

        final var pagedModels = service.getAll(pageRequest);
        log.trace("Got {} {}: {}", modelName, pageRequest, pagedModels.getContent());

        final var pagedDTOs = new Paged<>(pagedModels).map(this::toDTO);
        log.trace("Built a paged DTOs of {}: {}", modelName, pagedModels.getContent());

        return pagedDTOs;
    }

    @ApiResponse(responseCode = CODE_OK, description = GET_RESPONSE)
    @ApiResponse(
            responseCode = CODE_NOT_FOUND,
            description = NOT_FOUND_RESPONSE,
            content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = CRUDError.class))
    )
    @GetMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE)
    @Operation(summary = GET_SUMMARY, description = GET_DESCRIPTION)
    public D get(
            @Parameter(name = "id", description = GET_ID_DESCRIPTION)
            @PathVariable final I id
    ) {
        log.debug("Got request to get {} {}", modelName, id);

        final var model = service.get(id).orElseThrow(() -> CRUDError.notFound(modelName, id));
        log.trace("Got {} {}: {}", modelName, id, model);

        final var dto = toDTO(model);
        log.trace("Built {}DTO {}: {}", modelName, id, dto);

        return dto;
    }

    @ApiResponse(responseCode = CODE_OK, description = UPDATE_RESPONSE)
    @ApiResponse(
            responseCode = CODE_NOT_FOUND,
            description = NOT_FOUND_RESPONSE,
            content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = CRUDError.class))
    )
    @ApiResponse(
            responseCode = CODE_CONFLICT,
            description = CONFLICT_RESPONSE,
            content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = CRUDError.class))
    )
    @Operation(summary = UPDATE_SUMMARY, description = UPDATE_DESCRIPTION)
    @PutMapping(path = "/{id}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public D update(
            @Parameter(name = "id", description = UPDATE_ID_DESCRIPTION)
            @PathVariable final I id,
            @Parameter(required = true)
            @RequestBody final UD updateDTO
    ) {
        log.debug("Got request to update {} {}: {}", modelName, id, updateDTO);

        final var updateModel = updateDTO.toUpdateModel();
        log.trace("Built Update{} {}: {}", modelName, id, updateModel);

        final var model = service.update(id, updateModel);
        log.trace("Updated {} {}: {}", modelName, id, model);

        final var dto = toDTO(model);
        log.trace("Built {}DTO {}: {}", modelName, id, dto);

        return dto;
    }

    @ApiResponse(responseCode = CODE_NO_CONTENT, description = DELETE_RESPONSE)
    @ApiResponse(
            responseCode = CODE_NOT_FOUND,
            description = NOT_FOUND_RESPONSE,
            content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = CRUDError.class))
    )
    @DeleteMapping("/{id}")
    @Operation(summary = DELETE_SUMMARY, description = DELETE_DESCRIPTION)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @Parameter(name = "id", description = DELETE_ID_DESCRIPTION)
            @PathVariable final I id
    ) {
        log.debug("Got request to delete {} {}", modelName, id);

        service.delete(id);
    }

    private static final String CODE_OK = "200";
    private static final String CODE_CREATED = "201";
    private static final String CODE_NO_CONTENT = "204";
    private static final String CODE_NOT_FOUND = "404";
    private static final String CODE_CONFLICT = "409";
    private static final String NOT_FOUND_RESPONSE = "Object is not found.";
    private static final String CONFLICT_RESPONSE = "Object with given data already exists.";
    private static final String PAGE_DESCRIPTION = "Number of the 0-based page of objects to request";
    private static final String PER_PAGE_DESCRIPTION = "Number of objects to request per page";

    private static final String CREATE_SUMMARY = "Create a new object";
    private static final String CREATE_DESCRIPTION = "Creates a new object with given data and returns created object.";
    private static final String CREATE_RESPONSE = "Object is created successfully.";
    private static final String GET_ALL_SUMMARY = "Get all objects";
    private static final String GET_ALL_DESCRIPTION = "Gets all objects with given pagination.";
    private static final String GET_ALL_RESPONSE = "Objects are returned successfully.";
    private static final String GET_SUMMARY = "Get object with given id";
    private static final String GET_DESCRIPTION = "Gets object with given id.";
    private static final String GET_ID_DESCRIPTION = "Id of the object to request";
    private static final String GET_RESPONSE = "Object is returned successfully.";
    private static final String UPDATE_SUMMARY = "Update object with given id";
    private static final String UPDATE_DESCRIPTION = "Updates object with given id with given data and returns updated object.";
    private static final String UPDATE_ID_DESCRIPTION = "Id of the object to update";
    private static final String UPDATE_RESPONSE = "Object is updated successfully.";
    private static final String DELETE_SUMMARY = "Delete object with given id";
    private static final String DELETE_DESCRIPTION = "Deletes object with given id.";
    private static final String DELETE_ID_DESCRIPTION = "Id of the object to delete";
    private static final String DELETE_RESPONSE = "Object is deleted successfully.";
}
