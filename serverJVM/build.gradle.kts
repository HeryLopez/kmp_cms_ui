plugins {
    kotlin("jvm")
    kotlin("plugin.serialization") version "1.9.0"
    application
}

group = "com.example"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    // Ktor server
    implementation("io.ktor:ktor-server-core:2.3.0")
    implementation("io.ktor:ktor-server-netty:2.3.0")
    implementation("io.ktor:ktor-server-content-negotiation:2.3.0")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.0")

    // Exposed + H2 (o tu DB preferida)
    implementation("org.jetbrains.exposed:exposed-core:0.42.0")
    implementation("org.jetbrains.exposed:exposed-dao:0.42.0")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.42.0")
    implementation("com.h2database:h2:2.1.214")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
}

application {
    mainClass.set("com.example.serverjvm.MainKt")
}

kotlin {
    jvmToolchain(11)
}
