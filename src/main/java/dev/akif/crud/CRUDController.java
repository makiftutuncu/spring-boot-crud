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

/**
 * Base implementation of a CRUD controller for API layer
 * <p>
 * For some <em>Foo</em> data that have <em>Long</em> ids,
 * a <em>FooController</em> defined as
 * <pre>{@code
 * @RestController
 * @RequestMapping("/foo")
 * public class FooController
 *     extends CRUDController<
 *         Long,
 *         FooDTO,
 *         Foo,
 *         FooEntity,
 *         CreateFoo,
 *         UpdateFoo,
 *         CreateFooDTO,
 *         UpdateFooDTO,
 *         FooRepository,
 *         FooService> {}
 * }</pre>
 * automatically implements
 * <pre>{@code
 * @PostMapping("/")
 * public FooDTO create(CreateFooDTO createDTO);
 *
 * @GetMapping("/")
 * public Paged<FooDTO> getAll(int page, int perPage);
 *
 * @GetMapping("/{id}")
 * public FooDTO get(Long id);
 *
 * @PutMapping("/{id}")
 * public FooDTO update(Long id, UpdateFooDTO updateDTO);
 *
 * @DeleteMapping("/{id}")
 * public void delete(Long id);
 * }
 * </pre>
 * <p>
 * This is meant to be extended from a @{@link RestController} class,
 * ideally also with a @{@link RequestMapping} with some path prefix for the endpoints.
 *
 * @param <I>  Id type of the data
 * @param <D>  DTO type of the data
 * @param <M>  Model type of the data
 * @param <E>  Entity type of the data
 * @param <CM> Create model type of the data
 * @param <UM> Update model type of the data
 * @param <CD> Create DTO type of the data
 * @param <UD> Update DTO type of the data
 * @param <R>  Repository type of the data
 * @param <S>  Service type of the data
 */
@Slf4j
@Validated
public abstract class CRUDController<
        I extends Serializable,
        D extends BaseDTO<I>,
        M extends BaseModel<I>,
        E extends BaseEntity<I, M>,
        CM extends BaseCreateModel<I, M, E>,
        UM extends BaseUpdateModel<I, M, E>,
        CD extends BaseCreateDTO<I, M, E, CM>,
        UD extends BaseUpdateDTO<I, M, E, UM>,
        R extends CRUDRepository<I, M, E>,
        S extends CRUDService<I, M, E, CM, UM, R>> {
    /** Type name of the data this controller manages */
    protected final String type;
    /** Service dependency of this controller */
    protected final S service;

    /**
     * Constructor to provide type name and dependencies to this controller
     *
     * @param type    Type name of the data this controller manages
     * @param service Service dependency of this controller
     */
    protected CRUDController(final String type, final S service) {
        this.type = type;
        this.service = service;
    }

    /**
     * Mapper to convert from model to DTO
     *
     * @param model Model to convert
     *
     * @return DTO built from given model
     */
    protected abstract D toDTO(final M model);

    /**
     * Default implementation for creating a new entity from given create DTO
     *
     * @param createDTO Create DTO containing data of the entity to create
     *
     * @return DTO of the created entity
     */
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
        log.debug("Got request to create new {}: {}", type, createDTO);

        final var createModel = createDTO.toCreateModel();
        log.trace("Built Create{}: {}", type, createModel);

        final var model = service.create(createModel);
        log.trace("Created {}: {}", type, model);

        final var dto = toDTO(model);
        log.trace("Built {}DTO: {}", type, dto);

        return dto;
    }

    /**
     * Default implementation for listing entities with given pagination
     *
     * @param page    Number of the 0-based page of entities to list
     * @param perPage Number of entities to list per page
     *
     * @return {@link Paged} of DTOs of entities
     */
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
        log.debug("Got request to get {} {}", type, pageRequest);

        final var pagedModels = service.getAll(pageRequest);
        log.trace("Got {} {}: {}", type, pageRequest, pagedModels.getContent());

        final var pagedDTOs = new Paged<>(pagedModels).map(this::toDTO);
        log.trace("Built a paged DTOs of {}: {}", type, pagedModels.getContent());

        return pagedDTOs;
    }

    /**
     * Default implementation for getting an entity with given id
     *
     * @param id Id of the entity
     *
     * @return DTO of the entity with given id
     */
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
        log.debug("Got request to get {} {}", type, id);

        final var model = service.get(id).orElseThrow(() -> CRUDErrorException.notFound(type, id));
        log.trace("Got {} {}: {}", type, id, model);

        final var dto = toDTO(model);
        log.trace("Built {}DTO {}: {}", type, id, dto);

        return dto;
    }

    /**
     * Default implementation for updating an entity with given id with given update DTO data
     *
     * @param id        Id of the entity to update
     * @param updateDTO Update DTO containing data to be updated
     *
     * @return DTO of the updated entity
     */
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
        log.debug("Got request to update {} {}: {}", type, id, updateDTO);

        final var updateModel = updateDTO.toUpdateModel();
        log.trace("Built Update{} {}: {}", type, id, updateModel);

        final var model = service.update(id, updateModel);
        log.trace("Updated {} {}: {}", type, id, model);

        final var dto = toDTO(model);
        log.trace("Built {}DTO {}: {}", type, id, dto);

        return dto;
    }

    /**
     * Default implementation for deleting an entity with given id
     *
     * @param id Id of the entity to delete
     */
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
        log.debug("Got request to delete {} {}", type, id);

        service.delete(id);
    }

    private static final String CODE_OK = "200";
    private static final String CODE_CREATED = "201";
    private static final String CODE_NO_CONTENT = "204";
    private static final String CODE_NOT_FOUND = "404";
    private static final String CODE_CONFLICT = "409";
    private static final String NOT_FOUND_RESPONSE = "entity is not found.";
    private static final String CONFLICT_RESPONSE = "entity with given data already exists.";
    private static final String PAGE_DESCRIPTION = "Number of the 0-based page of entities to request";
    private static final String PER_PAGE_DESCRIPTION = "Number of entities to request per page";

    private static final String CREATE_SUMMARY = "Create a new entity";
    private static final String CREATE_DESCRIPTION = "Creates a new entity with given data and returns created entity.";
    private static final String CREATE_RESPONSE = "entity is created successfully.";
    private static final String GET_ALL_SUMMARY = "Get all entities";
    private static final String GET_ALL_DESCRIPTION = "Gets all entities with given pagination.";
    private static final String GET_ALL_RESPONSE = "entities are returned successfully.";
    private static final String GET_SUMMARY = "Get entity with given id";
    private static final String GET_DESCRIPTION = "Gets entity with given id.";
    private static final String GET_ID_DESCRIPTION = "Id of the entity to request";
    private static final String GET_RESPONSE = "entity is returned successfully.";
    private static final String UPDATE_SUMMARY = "Update entity with given id";
    private static final String UPDATE_DESCRIPTION = "Updates entity with given id with given data and returns updated entity.";
    private static final String UPDATE_ID_DESCRIPTION = "Id of the entity to update";
    private static final String UPDATE_RESPONSE = "entity is updated successfully.";
    private static final String DELETE_SUMMARY = "Delete entity with given id";
    private static final String DELETE_DESCRIPTION = "Deletes entity with given id.";
    private static final String DELETE_ID_DESCRIPTION = "Id of the entity to delete";
    private static final String DELETE_RESPONSE = "entity is deleted successfully.";
}
