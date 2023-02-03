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

