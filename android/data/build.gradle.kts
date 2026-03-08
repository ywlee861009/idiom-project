plugins {
    id("com.android.library")
    alias(libs.plugins.kotlin.android)
    id("com.google.dagger.hilt.android")
    id("org.jetbrains.kotlin.plugin.serialization") version "2.1.10"
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.kero.idiom.data"
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
    implementation(project(":domain"))
    implementation(libs.hilt.android)
    implementation(libs.kotlinx.serialization.json)
    ksp(libs.hilt.compiler)
}
