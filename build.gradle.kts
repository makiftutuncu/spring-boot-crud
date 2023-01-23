plugins {
    idea
    java
    `java-library`
    `maven-publish`
    signing
    id("io.github.gradle-nexus.publish-plugin") version "1.1.0"
}

allprojects {
    group = "dev.akif"
    version = "0.3.0-SNAPSHOT"
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

nexusPublishing {
    repositories {
        sonatype()
    }
}
