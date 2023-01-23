/**
 * <em>spring-boot-crud</em> is a library that provides opinionated, REST-ful and
 * generic CRUD (Create, Read, Update and Delete) operations on data models of a
 * backend API application using Spring Boot. Its purpose is to eliminate duplication
 * on mostly straightforward CRUD operations and enforce some best practices.
 * <p>
 * It provides
 * <ul>
 * <li>a controller base type with <em>create</em>, <em>getAll (list with pagination)</em>,
 * <em>get</em>, <em>update</em> and <em>delete</em> endpoints</li>
 * <li>a service base type with methods corresponding to provided endpoints</li>
 * <li>a repository base type to provide data access interface</li>
 * <li>DTO (Data Transfer Object) base types to use at API layer for getting, creating and updating</li>
 * <li>Model base types to use at service layer for getting, creating, updating</li>
 * <li>Entity base type to use at data layer</li>
 * <li>Logging at different layers and levels</li>
 * <li>OpenAPI documentation with Swagger UI</li>
 * </ul>
 * as long as your data models extend provided base types. You'll be required to
 * provide concrete types to type parameters, provide implementations for a few
 * abstract members, and you'll get all of the above for free.
 * <p>
 * See
 * <ul>
 * <li>{@link dev.akif.crud.CRUDController}</li>
 * <li>{@link dev.akif.crud.CRUDService}</li>
 * <li>{@link dev.akif.crud.CRUDRepository}</li>
 * <li>{@link dev.akif.crud.CRUDDTO}</li>
 * <li>{@link dev.akif.crud.CRUDModel}</li>
 * <li>{@link dev.akif.crud.CRUDEntity}</li>
 * </ul>
 * and their <em>Simple</em>, <em>Simpler</em> and <em>Simplest</em> variants.
 */
package dev.akif.crud;
