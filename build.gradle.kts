import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED
import org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED
import org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED

group = "com.kakao.ifkakao"
version = "1.0.0-SNAPSHOT"

plugins {
    id("java")
    kotlin("jvm") version "1.3.72"
    id("org.springframework.boot") version "2.2.2.RELEASE"
    id("io.spring.dependency-management") version "1.0.8.RELEASE"
    id( "org.jetbrains.kotlin.plugin.spring") version "1.3.72"
    id("org.jetbrains.kotlin.plugin.jpa") version "1.3.72"
}


repositories {
    mavenCentral()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = JavaVersion.VERSION_1_8.toString()
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        events = setOf(FAILED, PASSED, SKIPPED)
    }
}

val jacksonVersion = "2.11.2"
val resultVersion = "2.2.0"
val fuelVersion = "2.2.3"

val kotestVersion = "4.1.2"
val mockkVersion = "1.10.0"
val mockServerVersion = "5.11.1"

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.kotest:kotest-runner-junit5-jvm:$kotestVersion")
    testImplementation("io.kotest:kotest-assertions-core-jvm:$kotestVersion")
    testImplementation("io.kotest:kotest-property-jvm:$kotestVersion")
    testImplementation("io.kotest:kotest-extensions-spring:$kotestVersion")
    testImplementation("io.mockk:mockk:$mockkVersion")
    testImplementation("org.mock-server:mockserver-netty:$mockServerVersion")

    testRuntimeOnly("com.h2database:h2")
}


