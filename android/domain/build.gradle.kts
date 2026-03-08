plugins {
    id("com.android.library")
    alias(libs.plugins.kotlin.android)
    id("org.jetbrains.kotlin.plugin.serialization") version "2.1.10"
}

android {
    namespace = "com.kero.idiom.domain"
    compileSdk = 35

    kotlin {
        jvmToolchain(17)
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
}
