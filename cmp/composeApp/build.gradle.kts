plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.serialization)
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
            implementation(project(":data"))
            implementation(project(":core"))
            implementation(project(":feature:quiz"))
            implementation(project(":feature:result"))
            
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.androidx.navigation.compose)
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(libs.compottie)
            implementation(libs.compottie.resources)
            implementation(libs.compottie.dot)
        }
        androidMain.dependencies {
            implementation(libs.androidx.core.ktx)
            implementation(libs.androidx.activity.compose)
            implementation(libs.koin.android)
        }
    }
}

android {
    namespace = "com.kero.idiom"
    compileSdk = 35
    defaultConfig {
        applicationId = "com.kero.idiom"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0.0-CMP"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // 안드로이드 전용 디버그 의존성은 여기서 관리!
    debugImplementation(libs.androidx.ui.tooling)
}
