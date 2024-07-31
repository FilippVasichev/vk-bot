plugins {
    id("org.springframework.boot") version "3.1.4"
    id("io.spring.dependency-management") version "1.1.3"
    id("org.jlleitschuh.gradle.ktlint") version "12.0.3"

    kotlin("jvm") version "2.0.0"
    kotlin("plugin.spring") version "2.0.0"
}

group = "zionweeds"
version = "1.0"
java.sourceCompatibility = JavaVersion.VERSION_21

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-jackson:2.9.0")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    testImplementation("org.testcontainers:junit-jupiter:1.15.0")
    testImplementation("org.mockserver:mockserver-client-java:5.11.2")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    testImplementation("io.rest-assured:rest-assured:5.3.2")
    testImplementation("io.rest-assured:json-path:5.3.2")
    testImplementation("io.rest-assured:xml-path:5.3.2")
}

tasks.test {
    useJUnitPlatform()
}

ktlint {
    version.set("1.0.1")
    verbose.set(true)
    outputColorName.set("RED")
    reporters {
        reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.JSON)
    }
}

kotlin {
    jvmToolchain(21)
}
