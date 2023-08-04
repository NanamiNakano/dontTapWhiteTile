plugins {
    id("idea")
    kotlin("jvm") version "1.9.0"
    java
}

group = "com.thynanami.plugin.dtwt"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://oss.sonatype.org/content/groups/public/")
}

dependencies {
    implementation(kotlin("stdlib"))
    compileOnly("org.spigotmc:spigot-api:1.20.1-R0.1-SNAPSHOT")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

tasks {
    wrapper {
        gradleVersion = "8.2"
        distributionType = Wrapper.DistributionType.ALL
    }

    processResources {
        val placeholders = mapOf(
            "version" to version
        )

        filesMatching("plugin.yml") {
            expand(placeholders)
        }
    }
}

tasks.create<Copy>("copyToPlugins") {
    dependsOn("jar")
    this.from("./build/libs/dtwt-1.0-SNAPSHOT.jar")
    this.into("./server/plugins")
}