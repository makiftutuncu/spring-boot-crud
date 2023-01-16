plugins {
    idea
    java
    `java-library`
    `maven-publish`
    signing
    id("io.github.gradle-nexus.publish-plugin") version "1.1.0"
}

group = "dev.akif"
version = "0.2.0"

idea {
    module {
        isDownloadJavadoc = true
        isDownloadSources = true
    }
}

repositories {
    mavenCentral()
}

val lombokVersion = "1.18.24"
val springVersion = "3.0.1"
val springdocVersion = "2.0.2"
val junitVersion = "5.9.2"
val mockitoJUnitVersion = "5.0.0"

dependencies {
    annotationProcessor("org.projectlombok:lombok:$lombokVersion")
    compileOnly("org.projectlombok:lombok:$lombokVersion")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:$springVersion")
    implementation("org.springframework.boot:spring-boot-starter-validation:$springVersion")
    implementation("org.springframework.boot:spring-boot-starter-web:$springVersion")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:$springdocVersion")

    testAnnotationProcessor("org.projectlombok:lombok:$lombokVersion")
    testCompileOnly("org.projectlombok:lombok:$lombokVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testImplementation("org.mockito:mockito-junit-jupiter:$mockitoJUnitVersion")
    testImplementation("org.springframework.boot:spring-boot-starter-test:$springVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
    testLogging {
        events          = setOf(
            org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED,
            org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED,
            org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED
        )
        showCauses      = true
        showExceptions  = true
        showStackTraces = true
    }
}

java {
    withSourcesJar()
    withJavadocJar()
}

nexusPublishing {
    repositories {
        sonatype()
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = project.name
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
                url.set("https://akif.dev")
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
                    }
                }
                scm {
                    url.set("https://github.com/makiftutuncu/${project.name}")
                }
            }
        }
    }
}

signing {
    val signingKey = properties["signingKey"] as String?
    val signingPassword = properties["signingPassword"] as String?
    useInMemoryPgpKeys(signingKey, signingPassword)
    sign(publishing.publications["mavenJava"])
}

tasks.javadoc {
    (options as StandardJavadocDocletOptions).addBooleanOption("html5", true)
}

tasks.getByName<Sign>("signMavenJavaPublication").enabled = !project.version.toString().endsWith("-SNAPSHOT")
