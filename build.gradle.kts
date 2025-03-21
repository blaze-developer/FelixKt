plugins {
    kotlin("jvm") version "2.1.10"
}

group = "com.blazedeveloper"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation(kotlin("reflect"))
    implementation("dev.kord:kord-core:0.15.0")
    implementation("dev.kord.x:emoji:0.5.0")
    implementation("org.slf4j:slf4j-simple:2.0.17")
    implementation("io.github.classgraph:classgraph:4.8.179")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(23)
}