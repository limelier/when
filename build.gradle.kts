import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jooq.meta.jaxb.Logging

plugins {
    kotlin("jvm") version "1.8.10"
    id("nu.studer.jooq") version "8.1"
    application
}

group = "dev.limelier"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // discord
    implementation("dev.kord:kord-core:0.8.0-M16")

    // db
    jooqGenerator("org.postgresql:postgresql:42.5.4")
    implementation("org.postgresql:postgresql:42.5.4")

    // logging
    implementation("io.github.microutils:kotlin-logging-jvm:3.0.5")
    implementation("org.slf4j:slf4j-simple:2.0.5")

    // kotlin
    testImplementation(kotlin("test"))
}

jooq {
    version.set("3.18.0")

    configurations {
        create("main") {
            jooqConfiguration.apply {
                logging = Logging.WARN
                jdbc.apply {
                    driver = "org.postgresql.Driver"
                    url = "jdbc:postgresql://db:5432/when"
                    user = "postgres"
                    password = "postgres"
                }
                generator.apply {
                    name = "org.jooq.codegen.KotlinGenerator"
                    generateSchemaSourceOnCompilation.set(false)
                    database.apply {
                        name = "org.jooq.meta.postgres.PostgresDatabase"
                        inputSchema = "public"
                    }
                    target.apply {
                        packageName = "dev.limelier"
                        directory = "src/generated/jooq"
                    }
                    strategy.name = "org.jooq.codegen.DefaultGeneratorStrategy"
                }
            }
        }
    }
}

tasks {
    test {
        useJUnitPlatform()
    }
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "19"
    }
    withType<JavaCompile> {
        targetCompatibility = "19"
    }
    withType<Jar> {
        manifest {
            attributes["Main-Class"] = "MainKt"
        }

        duplicatesStrategy = DuplicatesStrategy.EXCLUDE

        configurations.compileClasspath.get().forEach {
            from(if (it.isDirectory) it else zipTree(it))
        }
    }
}

application {
    mainClass.set("MainKt")
}
