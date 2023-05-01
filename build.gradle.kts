plugins {
    kotlin("jvm") version "1.8.20"
    id("io.github.gradle-nexus.publish-plugin") version "1.1.0"
    id("org.jetbrains.dokka") version "1.7.20"
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
