import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.jvm)
    application
}

group = "com.desiderantes"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.jooq)
    implementation(libs.config4k)
    implementation(libs.clikt)

    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation(libs.junit.jupiter.engine)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}
application {
    mainClass.set("com.desiderantes.sqlformatter.MainKt")
    applicationDefaultJvmArgs = listOf("-Dorg.jooq.no-tips=true", "-Dorg.jooq.no-logo=true")
}