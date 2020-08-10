import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "com.kakao.ifkakao"
version = "1.0.0-SNAPSHOT"

plugins {
    kotlin("jvm") version "1.3.72"
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

val kotestVersion = "4.1.2"
val mockkVersion = "1.10.0"

dependencies {
    implementation(kotlin("stdlib"))
    testImplementation("io.kotest:kotest-runner-junit5-jvm:$kotestVersion")
    testImplementation("io.kotest:kotest-assertions-core-jvm:$kotestVersion")
    testImplementation("io.kotest:kotest-property-jvm:$kotestVersion")
    testImplementation("io.mockk:mockk:$mockkVersion")

}


