plugins {
    idea
    java
    `java-library`
    `maven-publish`
}

group = "dev.akif"
version = "0.1.0-SNAPSHOT"

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

dependencies {
    annotationProcessor("org.projectlombok:lombok:$lombokVersion")
    compileOnly("org.projectlombok:lombok:$lombokVersion")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:$springVersion")
    implementation("org.springframework.boot:spring-boot-starter-validation:$springVersion")
    implementation("org.springframework.boot:spring-boot-starter-web:$springVersion")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:$springdocVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

tasks.jar {
    manifest {
        attributes(
            mapOf(
                "Title" to project.name,
                "Version" to project.version,
                "Author" to "Mehmet Akif Tütüncü",
                "Email" to "m.akif.tutuncu@gmail.com",
                "Url" to "https://akif.dev",
                "License" to "MIT License"
            )
        )
    }
}

java {
    withSourcesJar()
    withJavadocJar()
}

publishing {
    publications {
        create<MavenPublication>("spring-boot-crud") {
            from(components["java"])
        }
    }

    repositories {
        maven {
            name = "spring-boot-crud"
            url = uri(layout.buildDirectory.dir("repo"))
        }
    }
}
