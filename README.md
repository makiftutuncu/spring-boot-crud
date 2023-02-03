# spring-boot-crud

spring-boot-crud is a library that provides opinionated, REST-ful, and generic CRUD (Create, Read, Update, and Delete) operations on data models of a Java or Kotlin backend API application. 
Its purpose is to eliminate duplication on mostly straightforward CRUD operations and enforce some best practices.

### It provides:

- a controller base type with create, getAll (list with pagination), get, update, and delete endpoints
- a service base type with methods corresponding to provided endpoints
- a repository base type to provide data access interface
- DTO (Data Transfer Object) base types to use at the API layer for getting, creating, and updating
- Model base types to use at the service layer for getting, creating, updating
- Entity base type to use at the data layer
- Logging at different layers and levels
- OpenAPI documentation with Swagger UI

As long as your data models extend provided base types, you'll be required to provide concrete types to type parameters, provide implementations for a few abstract members, and you'll get all of the above for free.

### Prerequisites

- Java 17 or higher
- Maven 3.6 or higher

### Getting Started

To get started with spring-boot-crud, you need to have a project with Spring Boot Starter installed. 
Please note that you should be using Spring Boot version 3 or higher and Java version 17 or higher.


You can set up your project with spring-boot-crud by adding it as a maven dependency, or by using start.spring.io. 
When using [start.spring.io](http://start.spring.io), make sure to select Spring Boot version 3 or higher, Java version 17 or higher, and the following dependencies:

- spring-web
- validation
- spring-data-jpa <em>(optional)</em> 
- a database driver of your choice <em>(optional)</em> 
- testcontainers <em>(optional)</em> 

Once you have set up your project, you can start using spring-boot-crud by extending the provided base types in your data models.

## Adding to your Application
To get started with spring-boot-crud, you will want to begin by creating a controller that extends CRUDController. 
You may choose to use one of the variants of CRUDController such as CRUDControllerSimple, CRUDControllerSimpler, or CRUDControllerSimplest depending on your needs. 
Once you have selected a variant, make sure you are consistent in your usage of it within your package.

Next, you will want to add your DTO and Domain objects. 
The generic types on the `CRUDController`, `CRUDService`, and `CRUDRepository` classes will enforce you to add the objects to the correct places. 
By using these base types, you will be provided with standard CRUD operations out-of-the-box, and can focus on implementing the custom functionality for your application.

For a detailed look into the provided components, see the following files:

* [CRUDController](api/src/main/java/dev/akif/crud/CRUDController.java)
* [CRUDService](api/src/main/java/dev/akif/crud/CRUDService.java)
* [CRUDRepository](api/src/main/java/dev/akif/crud/CRUDRepository.java)
* [CRUDDTO](api/src/main/java/dev/akif/crud/CRUDDTO.java)
* [CRUDModel](api/src/main/java/dev/akif/crud/CRUDModel.java)
* [CRUDEntity](api/src/main/java/dev/akif/crud/CRUDEntity.java)

and their Simple, Simpler, and Simplest variants.

## Code Examples

### Writing a controller

The code snippet represents a Java Controller that implements the CRUDController interface.

The CRUDController is a base type that provides endpoints for basic CRUD (Create, Read, Update, Delete) operations. 
It is the entry point for incoming HTTP requests, and it delegates to a CRUDService for business logic. 
By extending the CRUDController, developers can quickly set up a working RESTful API for their data models.

```java
@RestController
public class MyController extends CRUDController<
        Long,
        MyEntity,
        MyModel,
        MyDTO,
        MyCreateModel,
        MyUpdateModel,
        MyCreateDTO,
        MyUpdateDTO,
        MyMapper,
        MyDTOMapper,
        MyService> {

    public MyController(MyService service, MyDTOMapper dtoMapper) {
        super(service, dtoMapper);
    }
}

```

The CRUDController uses generics to ensure that the correct types are used for each layer of the application. The following types are defined as generic parameters:

- `I`: the type of the ID of the entities
- `E`: the type of the Entity objects
- `M`: the type of the Model objects
- `D`: the type of the Data Transfer Object (DTO) objects
- `CM`: the type of the Create Model objects
- `UM`: the type of the Update Model objects
- `CD`: the type of the Create DTO objects
- `UD`: the type of the Update DTO objects
- `Mapper`: the type of the mapper class used to map between Entity and Model objects
- `DTOMappe`r: the type of the mapper class used to map between DTO and Model objects
- `S`: the type of the CRUDService implementation

By using these generics, the CRUDController ensures that the correct types are used for each layer of the application, and it ensures that everything works together seamlessly.

To use the CRUDController in your application, simply extend it with your specific types and provide implementations for the abstract methods.

```java
@Service
public class ExampleService implements CRUDService<
                Long, 
                ExampleModel, 
                ExampleEntity, 
                ExampleCreateModel,
                ExampleUpdateModel, 
                ExampleMapper> {


    private final ExampleRepository repository;
    private final ExampleMapper mapper;

    public ExampleService(ExampleRepository repository, ExampleMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<ExampleModel> getAll(int page, int size) {
        Page<ExampleEntity> entities = repository.findAll(PageRequest.of(page, size));
        return entities.getContent().stream().map(mapper::toModel).collect(Collectors.toList());
    }

    @Override
    public ExampleModel get(Long id) {
        return mapper.toModel(repository.findById(id).orElseThrow());
    }

    @Override
    public ExampleModel create(ExampleCreateModel createModel) {
        ExampleEntity entity = mapper.toEntity(createModel);
        ExampleEntity savedEntity = repository.save(entity);
        return mapper.toModel(savedEntity);
    }

    @Override
    public ExampleModel update(Long id, ExampleUpdateModel updateModel) {
        ExampleEntity entity = repository.findById(id).orElseThrow();
        mapper.updateEntity(entity, updateModel);
        ExampleEntity savedEntity = repository.save(entity);
        return mapper.toModel(savedEntity);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
```

The code snippet is an example of a Java service class that implements the CRUDService interface. This interface provides a base type for CRUD operations and takes several type parameters as inputs:

- `I`: A type that extends Serializable and represents the ID of the entity
- `M`: A type that extends CRUDModel and represents the CRUD model
- `E`: A type that extends CRUDEntity and represents the CRUD entity
- `CM`: A type that extends CRUDCreateModel and represents the CRUD create model
- `UM`: A type that extends CRUDUpdateModel and represents the CRUD update model- 
- `Mapper`: A type that extends CRUDMapper and represents the CRUD mapper.

By implementing this interface, the class provides a base type for CRUD operations, and it is responsible for the implementation of basic CRUD operations such as create, get, update, and delete. The implementation details will be different based on the actual requirements, but it provides a well-defined structure that makes it easier to implement CRUD operations in a consistent manner. The methods in the service class will be used in the controller layer to handle CRUD operations and return responses to clients.

## API Endpoints

The following API endpoints will be made available:

| Method  | Endpoint  | Description  |
|---|---|---|
| GET	 | /api/v1/examples	        | Retrieve a list of examples |
| GET	 | /api/v1/examples/{id}	| Retrieve a single examples by id |
| POST	 | /api/v1/examples     	| Create a new examples |
| PUT	 | /api/v1/examples/{id}	| Update an existing examples |
| DELETE | 	/api/v1/examples/{id}	| Delete an existing examples |


### License

This project is licensed under the MIT License - see the LICENSE file for details.