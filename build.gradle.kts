plugins {
    kotlin("jvm") version "1.9.25"
    id("io.github.gradle-nexus.publish-plugin") version "1.3.0"
    id("org.jetbrains.dokka") version "1.8.10"
}

allprojects {
    group = "dev.akif"
    version = "0.5.1"

    apply(plugin = "org.jetbrains.dokka")
}

kotlin {
    jvmToolchain(17)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

nexusPublishing {
    repositories {
        sonatype()
    }
}

repositories {
    mavenCentral()
}
