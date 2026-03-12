plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.realm)
}

kotlin {
    jvmToolchain(17)
    androidTarget {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(project(":domain"))
            implementation(project(":core"))
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.androidx.datastore.preferences.core)
            
            // Ktor
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.ktor.client.logging)
            
            // Realm (KMP NoSQL)
            implementation(libs.realm.library.base)
            
            // Koin (Core)
            implementation(libs.koin.core)
            
            // Compose Resources & Runtime (Compiler Requirement)
            implementation(compose.runtime)
            implementation(compose.components.resources)
        }
        androidMain.dependencies {
            implementation(libs.androidx.datastore.preferences)
            implementation(libs.koin.android)
        }
    }
}

compose.resources {
    packageOfResClass = "com.kero.idiom.data.resources"
}

android {
    namespace = "com.kero.idiom.data"
    compileSdk = 35
}
