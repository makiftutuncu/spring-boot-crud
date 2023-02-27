package dev.akif.crud

import dev.akif.crud.common.Paged
import dev.akif.crud.error.CRUDError
import dev.akif.crud.error.CRUDErrorException
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.io.Serializable

/**
 * Base implementation of a controller for API layer providing CRUD operations
 *
 * This is meant to be extended from a **@RestController** class,
 * ideally also with a **@RequestMapping** with some path prefix for the endpoints.
 *
 * @param I         Id type of the data
 * @param E         Entity type of the data which is a [CRUDEntity]
 * @param M         Model type of the data which is a [CRUDModel]
 * @param D         DTO type of the data which is a [CRUDDTO]
 * @param CM        Create model type of the data which is a [CRUDCreateModel]
 * @param UM        Update model type of the data which is a [CRUDUpdateModel]
 * @param CD        Create DTO type of the data which is a [CRUDCreateDTO]
 * @param UD        Update DTO type of the data which is a [CRUDUpdateDTO]
 * @param Mapper    Mapper type of the data which is a [CRUDMapper]
 * @param DTOMapper DTO mapper type of the data which is a [CRUDDTOMapper]
 * @param R         Repository type of the data which is a [CRUDRepository]
 * @param S         Service type of the data which is a [CRUDService]
 *
 * @property typeName Type name of the data this controller manages
 * @property service  Service dependency of this controller which is a [CRUDService]
 * @property mapper   DTO mapper dependency of this controller which is a [CRUDDTOMapper]
 */
@Validated
abstract class CRUDController<
        I : Serializable,
        E : CRUDEntity<I>,
        M : CRUDModel<I>,
        out D : CRUDDTO<I>,
        CM : CRUDCreateModel,
        UM : CRUDUpdateModel,
        in CD : CRUDCreateDTO,
        in UD : CRUDUpdateDTO,
        out Mapper : CRUDMapper<I, E, M, CM, UM>,
        out DTOMapper : CRUDDTOMapper<I, M, D, CM, UM, CD, UD>,
        out R : CRUDRepository<I, E>,
        out S : CRUDService<I, E, M, CM, UM, R, Mapper>>(
    protected open val typeName: String,
    protected open val service: S,
    protected open val mapper: DTOMapper
) {
    /**
     * Default implementation for creating a new entity from given create DTO
     *
     * @param createDTO Create DTO containing data of the entity to create
     * @return DTO of the created entity
     */
    @ApiResponse(responseCode = CODE_CREATED, description = CREATE_RESPONSE)
    @ApiResponse(
        responseCode = CODE_CONFLICT,
        description = CONFLICT_RESPONSE,
        content = [Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = Schema(implementation = CRUDError::class)
        )]
    )
    @Operation(summary = CREATE_SUMMARY, description = CREATE_DESCRIPTION)
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(code = HttpStatus.CREATED)
    fun create(
        @Parameter(required = true) @RequestBody createDTO: CD
    ): D {
        log.debug("Got request to create new $typeName: $createDTO")
        val createModel = mapper.createDTOToCreateModel(createDTO)
        log.trace("Built Create$typeName: $createModel")
        val model = service.create(createModel)
        log.trace("Created $typeName: $model")
        val dto = mapper.modelToDTO(model)
        log.trace("Built ${typeName}DTO: $dto")
        return dto
    }

    /**
     * Default implementation for listing entities with given pagination
     *
     * @param page    Number of the 0-based page of entities to list
     * @param perPage Number of entities to list per page
     * @return [Paged] of DTOs of entities
     */
    @ApiResponse(responseCode = CODE_OK, description = GET_ALL_RESPONSE)
    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    @Operation(summary = GET_ALL_SUMMARY, description = GET_ALL_DESCRIPTION)
    fun getAll(
        @Parameter(name = "page", description = PAGE_DESCRIPTION) @RequestParam(
            name = "page",
            required = false,
            defaultValue = "0"
        ) page: Int,
        @Parameter(name = "perPage", description = PER_PAGE_DESCRIPTION) @RequestParam(
            name = "perPage",
            required = false,
            defaultValue = "20"
        ) perPage: Int
    ): Paged<D> {
        val pageRequest = PageRequest.of(page, perPage)
        log.debug("Got request to get $typeName $pageRequest")
        val pagedModels = service.getAll(pageRequest)
        log.trace("Got $typeName $pageRequest: ${pagedModels.data}")
        val pagedDTOs = pagedModels.map(mapper::modelToDTO)
        log.trace("Built a paged DTOs of $typeName: ${pagedDTOs.data}")
        return pagedDTOs
    }

    /**
     * Default implementation for getting an entity with given id
     *
     * @param id Id of the entity
     * @return DTO of the entity with given id
     */
    @ApiResponse(responseCode = CODE_OK, description = GET_RESPONSE)
    @ApiResponse(
        responseCode = CODE_NOT_FOUND,
        description = NOT_FOUND_RESPONSE,
        content = [Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = Schema(implementation = CRUDError::class)
        )]
    )
    @GetMapping(path = ["/{id}"], produces = [MediaType.APPLICATION_JSON_VALUE])
    @Operation(summary = GET_SUMMARY, description = GET_DESCRIPTION)
    fun get(
        @Parameter(name = "id", description = GET_ID_DESCRIPTION) @PathVariable("id") id: I
    ): D {
        log.debug("Got request to get $typeName $id")
        val model = service.get(id) ?: throw CRUDErrorException.notFound(typeName, id)
        log.trace("Got $typeName $id: $model")
        val dto = mapper.modelToDTO(model)
        log.trace("Built ${typeName}DTO $id: $dto")
        return dto
    }

    /**
     * Default implementation for updating an entity with given id with given update DTO data
     *
     * @param id        Id of the entity to update
     * @param updateDTO Update DTO containing data to be updated
     * @return DTO of the updated entity
     */
    @ApiResponse(responseCode = CODE_OK, description = UPDATE_RESPONSE)
    @ApiResponse(
        responseCode = CODE_NOT_FOUND,
        description = NOT_FOUND_RESPONSE,
        content = [Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = Schema(implementation = CRUDError::class)
        )]
    )
    @ApiResponse(
        responseCode = CODE_CONFLICT,
        description = CONFLICT_RESPONSE,
        content = [Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = Schema(implementation = CRUDError::class)
        )]
    )
    @Operation(summary = UPDATE_SUMMARY, description = UPDATE_DESCRIPTION)
    @PutMapping(
        path = ["/{id}"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun update(
        @Parameter(name = "id", description = UPDATE_ID_DESCRIPTION) @PathVariable("id") id: I,
        @Parameter(required = true) @RequestBody updateDTO: UD
    ): D {
        log.debug("Got request to update $typeName $id: $updateDTO")
        val updateModel = mapper.updateDTOToUpdateModel(updateDTO)
        log.trace("Built Update$typeName $id: $updateModel")
        val model = service.update(id, updateModel)
        log.trace("Updated $typeName $id: $model")
        val dto = mapper.modelToDTO(model)
        log.trace("Built ${typeName}DTO $id: $dto")
        return dto
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
        content = [Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = Schema(implementation = CRUDError::class)
        )]
    )
    @DeleteMapping("/{id}")
    @Operation(summary = DELETE_SUMMARY, description = DELETE_DESCRIPTION)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(
        @Parameter(name = "id", description = DELETE_ID_DESCRIPTION) @PathVariable("id") id: I
    ) {
        log.debug("Got request to delete $typeName $id")
        service.delete(id)
    }

    /** @suppress */
    companion object {
        @JvmStatic
        protected val log: Logger = LoggerFactory.getLogger(CRUDController::class.java)

        private const val CODE_OK = "200"
        private const val CODE_CREATED = "201"
        private const val CODE_NO_CONTENT = "204"
        private const val CODE_NOT_FOUND = "404"
        private const val CODE_CONFLICT = "409"
        private const val NOT_FOUND_RESPONSE = "Entity is not found."
        private const val CONFLICT_RESPONSE = "Entity with given data already exists."
        private const val PAGE_DESCRIPTION = "Number of the 0-based page of entities to request"
        private const val PER_PAGE_DESCRIPTION = "Number of entities to request per page"
        private const val CREATE_SUMMARY = "Create a new entity"
        private const val CREATE_DESCRIPTION = "Creates a new entity with given data and returns created entity."
        private const val CREATE_RESPONSE = "Entity is created successfully."
        private const val GET_ALL_SUMMARY = "Get all entities"
        private const val GET_ALL_DESCRIPTION = "Gets all entities with given pagination."
        private const val GET_ALL_RESPONSE = "Entities are returned successfully."
        private const val GET_SUMMARY = "Get entity with given id"
        private const val GET_DESCRIPTION = "Gets entity with given id."
        private const val GET_ID_DESCRIPTION = "Id of the entity to request"
        private const val GET_RESPONSE = "Entity is returned successfully."
        private const val UPDATE_SUMMARY = "Update entity with given id"
        private const val UPDATE_DESCRIPTION =
            "Updates entity with given id with given data and returns updated entity."
        private const val UPDATE_ID_DESCRIPTION = "Id of the entity to update"
        private const val UPDATE_RESPONSE = "Entity is updated successfully."
        private const val DELETE_SUMMARY = "Delete entity with given id"
        private const val DELETE_DESCRIPTION = "Deletes entity with given id."
        private const val DELETE_ID_DESCRIPTION = "Id of the entity to delete"
        private const val DELETE_RESPONSE = "Entity is deleted successfully."
    }
}
