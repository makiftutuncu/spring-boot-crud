plugins {
    idea
    java
    `java-library`
    `maven-publish`
    signing
}

idea {
    module {
        isDownloadJavadoc = true
        isDownloadSources = true
    }
}

repositories {
    mavenCentral()
}

val springVersion = "3.0.1"
val junitVersion = "5.9.2"

dependencies {
    implementation(project(":api"))
    implementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:$springVersion")
    implementation("org.springframework.boot:spring-boot-starter-test:$springVersion")
    implementation("org.springframework.boot:spring-boot-starter-web:$springVersion")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
    testLogging {
        events = setOf(
            org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED,
            org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED,
            org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED
        )
        showCauses = true
        showExceptions = true
        showStackTraces = true
    }
}

java {
    withSourcesJar()
    withJavadocJar()
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = "${rootProject.name}-${project.name}"
            from(components["java"])
            versionMapping {
                usage("java-api") {
                    fromResolutionOf("runtimeClasspath")
                }
                usage("java-runtime") {
                    fromResolutionResult()
                }
            }
            pom {
                name.set(project.name)
                description.set("Opinionated, REST-ful and generic CRUD operations for Spring Boot applications")
                url.set("https://github.com/makiftutuncu/spring-boot-crud")
                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }
                developers {
                    developer {
                        id.set("makiftutuncu")
                        name.set("Mehmet Akif Tütüncü")
                        email.set("m.akif.tutuncu@gmail.com")
                        url.set("https://akif.dev")
                    }
                }
                scm {
                    url.set("https://github.com/makiftutuncu/spring-boot-crud")
                }
            }
        }
    }
}

signing {
    setRequired {
        !project.version.toString().endsWith("-SNAPSHOT")
                && gradle.taskGraph.allTasks.any { it is PublishToMavenRepository }
    }
    val signingKey = properties["signingKey"] as String?
    val signingPassword = properties["signingPassword"] as String?
    useInMemoryPgpKeys(signingKey, signingPassword)
    sign(publishing.publications["mavenJava"])
}

tasks.javadoc {
    (options as StandardJavadocDocletOptions).addBooleanOption("html5", true)
}
