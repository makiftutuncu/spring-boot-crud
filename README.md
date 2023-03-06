
# spring-boot-crud

spring-boot-crud is a library to help build CRUD applications easily with Spring Boot. As a developer, you probably wrote simple CRUD applications many times and wished you could get rid of most of that boilerplate around designing the APIs, services, repositories, etc. It is repetitive, boring and error-prone. spring-boot-crud offers an abstract and opinionated way to avoid most of this. You only need to care about your data models. After defining data models and a few other glue types, you get a full-featured CRUD application (and its tests!) for free. The library is highly generic and abstract. It utilizes type parameters and type constraints for additional type-safety while enforcing some best practices.


| Latest Version | Java Version | Kotlin Version | Spring Boot Version |
| -------------- | ------------ | -------------- | ------------------- |
| 0.4.0          | 17           | 1.8.0          | 3.0.3               |

## Table of Contents

1. [Modules](#modules)
2. [Examples](#examples)
3. [Development & Testing](#development--testing)
4. [Releases](#releases)
5. [Contributing](#contributing)
6. [License](#license)

## Modules

spring-boot-crud consists of 2 modules. You can click on a module for more information and detailed instructions.

| Name                                    | Details              | Documentation                                                                                                                                                                        |
|-----------------------------------------|----------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [spring-boot-crud-api](api/README.md)   | API implementations  | [![](https://img.shields.io/badge/docs-0.4.0-brightgreen.svg?style=for-the-badge&logo=kotlin&color=0095d5&labelColor=333333)](https://javadoc.io/doc/dev.akif/spring-boot-crud-api)  |
| [spring-boot-crud-test](test/README.md) | Test implementations | [![](https://img.shields.io/badge/docs-0.4.0-brightgreen.svg?style=for-the-badge&logo=kotlin&color=0095d5&labelColor=333333)](https://javadoc.io/doc/dev.akif/spring-boot-crud-test) |

## Examples

TODO: Add real-world example projects here.

There are template projects you can use to create new repositories on GitHub with an existing project setup using spring-boot-crud.

* https://github.com/makiftutuncu/kotlin-spring-boot-template
* https://github.com/makiftutuncu/java-spring-boot-template

## Development & Testing

This project is written in Kotlin and built with Gradle. Standard Gradle tasks like `clean`, `compileKotlin`, `compileTestKotlin` and `test` can be used during development and testing.

If you don't have Gradle installed, you can replace `gradle` commands with `./gradlew` to use Gradle wrapper.

To test your changes during development:

1. Bump your version in [build.gradle.kts](build.gradle.kts#L9) and append `-SNAPSHOT`.
2. Run `gradle publishToMavenLocal` to publish artifacts with your changes to your local Maven repository.
3. In the project you use spring-boot-crud, update the version of spring-boot-crud dependencies to your new snapshot version. Make sure you have `mavenLocal()` in your `repositories` in your build definition for this to work.

## Releases

Artifacts of this project are published to Maven Central along with their sources and documentations. They are versioned according to [semantic versioning](https://semver.org). CI/CD is managed by GitHub Actions. See [.github](.github) for more details on these workflows.

## Contributing

All contributions are welcome, including requests to feature your project using this library. Please feel free to send a pull request. Thank you.

## License

This project is licensed with MIT License. See [LICENSE](LICENSE) for more details.
