# spring-boot-crud

**spring-boot-crud** is a library that provides opinionated, REST-ful and generic CRUD (Create, Read, Update and Delete)
operations on data models of a backend API application using Spring Boot. Its purpose is to eliminate duplication on
mostly straightforward CRUD operations and enforce some best practices.

It provides

* a controller base type with `create`, `getAll (list with pagination)`, `get`, `update` and `delete` endpoints
* a service base type with methods corresponding to provided endpoints
* a repository base type to provide data access interface
* DTO (Data Transfer Object) base types to use at API layer for getting, creating and updating
* Model base types to use at service layer for getting, creating, updating
* Entity base type to use at data layer
* Logging at different layers and levels
* OpenAPI documentation with Swagger UI

* as long as your data models extend provided base types. You'll be required to provide concrete types to type
  parameters, provide implementations for a few abstract members, and you'll get all of the above for free.

See

* [CRUDController](api/src/main/java/dev/akif/crud/CRUDController.java)
* [CRUDService](api/src/main/java/dev/akif/crud/CRUDService.java)
* [CRUDRepository](api/src/main/java/dev/akif/crud/CRUDRepository.java)
* [CRUDDTO](api/src/main/java/dev/akif/crud/CRUDDTO.java)
* [CRUDModel](api/src/main/java/dev/akif/crud/CRUDModel.java)
* [CRUDEntity](api/src/main/java/dev/akif/crud/CRUDEntity.java)

and their `Simple`, `Simpler` and `Simplest` variants.
