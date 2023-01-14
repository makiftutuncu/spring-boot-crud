# spring-boot-crud
**spring-boot-crud** is a library that provides opinionated, REST-ful and generic CRUD (Create, Read, Update and Delete) operations on data models in a backend API application using [Spring Boot](https://spring.io/projects/spring-boot). Its purpose is to eliminate duplication.

It provides

* a controller base type with `create`, `getAll (list with pagination)`, `get`, `update` and `delete` endpoints
* a service base type with methods corresponding to provided endpoints
* a repository base type to provide data access interface
* DTO (Data Transfer Object) base types for creating, updating and getting to use at API layer
* Model base types for creating, updating and getting to use at service layer
* Entity base type to use at data layer
* Logging at different layers and levels
* OpenAPI documentation with Swagger UI

as long as your data models extend provided base types. You'll be required to provide concrete types to type parameters, provide implementations for a few abstract members, and you'll get all of the above for free.
