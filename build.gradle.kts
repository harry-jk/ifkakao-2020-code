import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "com.kakao.ifkakao"
version = "1.0.0-SNAPSHOT"

plugins {
    kotlin("jvm") version "1.3.72"
    id("org.springframework.boot") version "2.2.2.RELEASE"
    id("io.spring.dependency-management") version "1.0.8.RELEASE"
}


repositories {
    mavenCentral()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = JavaVersion.VERSION_1_8.toString()
}

tasks.withType<Test> {
    useJUnitPlatform()
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
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")


    testImplementation("io.kotest:kotest-runner-junit5-jvm:$kotestVersion")
    testImplementation("io.kotest:kotest-assertions-core-jvm:$kotestVersion")
    testImplementation("io.kotest:kotest-property-jvm:$kotestVersion")
    testImplementation("io.mockk:mockk:$mockkVersion")
    testImplementation("org.mock-server:mockserver-netty:$mockServerVersion")
}


