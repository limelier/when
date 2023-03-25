import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jooq.meta.jaxb.Logging

plugins {
    kotlin("jvm") version "1.8.0"
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
    implementation(kotlin("stdlib-jdk8"))
}

jooq {
    version.set("3.18.0")

    configurations {
        create("main") {
            jooqConfiguration.apply {
                logging = Logging.WARN
                jdbc.apply {
                    driver = "org.postgresql.Driver"
                    url = "jdbc:postgresql://localhost:5432/when"
                    user = "postgres"
                    password = "postgres"
                }
                generator.apply {
                    name = "org.jooq.codegen.KotlinGenerator"
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

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("MainKt")
}
val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}