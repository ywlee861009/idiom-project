plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
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
            implementation(libs.koin.compose.viewmodel) // CMP 공통 ViewModel 지원 필수
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
            implementation(libs.google.admob) // AdMob SDK
            implementation(libs.koin.android)
            
            // Ktor Engine
            implementation(libs.ktor.client.okhttp)

            // Firebase
            implementation(platform(libs.firebase.bom.get().toString()))
            implementation(libs.firebase.analytics)
            implementation(libs.firebase.crashlytics)

            // WorkManager (Local Notification)
            implementation(libs.androidx.work.runtime.ktx)
        }
    }
}

android {
    namespace = "com.kero.idiom"
    compileSdk = 36
    defaultConfig {
        applicationId = "com.kero.idiom"
        minSdk = 24
        targetSdk = 36
        versionCode = 2
        versionName = "1.0.0"
    }

    buildFeatures {
        buildConfig = true
    }

    buildTypes {
        getByName("debug") {
            manifestPlaceholders["admobAppId"] = "ca-app-pub-3940256099942544~3347511713" // Test App ID
            buildConfigField("String", "ADMOB_INTERSTITIAL_ID", "\"ca-app-pub-3940256099942544/1033173712\"") // Test Interstitial ID
            buildConfigField("String", "ADMOB_REWARDED_ID", "\"ca-app-pub-3940256099942544/5224354917\"") // Test Reward ID
        }
        getByName("release") {
            isMinifyEnabled = true // 💡 R8/Proguard 활성화 (앱 크기 축소 및 난독화)
            isShrinkResources = true // 미사용 리소스 제거
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            
            // 💡 네이티브 디버그 기호 업로드 경고 해결
            ndk {
                debugSymbolLevel = "full"
            }

            manifestPlaceholders["admobAppId"] = "ca-app-pub-2103375309908918~7366320554" // Real App ID
            buildConfigField("String", "ADMOB_INTERSTITIAL_ID", "\"ca-app-pub-3940256099942544/1033173712\"") 
            buildConfigField("String", "ADMOB_REWARDED_ID", "\"ca-app-pub-2103375309908918/5518411052\"") // Real Reward ID
            signingConfig = signingConfigs.getByName("debug")
        }
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
