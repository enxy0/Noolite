plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.enxy.noolite"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.enxy.noolite"
        minSdk = 23
        targetSdk = 36
        versionCode = 194
        versionName = "1.0.4"
    }

    signingConfigs {
        // TODO: Add key to .gitignore and properties from local.properties
        getByName("debug") {
            storeFile = file("keys/test-key")
            storePassword = "123456"
            keyPassword = "123456"
            keyAlias = "upload"
        }
        // TODO: Add key to .gitignore and properties from local.properties
        create("release") {
            storeFile = file("keys/test-key")
            storePassword = "123456"
            keyPassword = "123456"
            keyAlias = "upload"
        }
    }

    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        compileOptions {
            jvmToolchain(17)
        }
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
}

dependencies {
    implementation(project(":core:database"))
    implementation(project(":core:model"))
    implementation(project(":core:network"))
    implementation(project(":core:ui"))
    implementation(project(":data:home"))
    implementation(project(":data:script"))
    implementation(project(":data:settings"))
    implementation(project(":domain:detail"))
    implementation(project(":domain:home"))
    implementation(project(":domain:script"))
    implementation(project(":domain:settings"))
    implementation(project(":feature:detail"))
    implementation(project(":feature:home"))
    implementation(project(":feature:script"))
    implementation(project(":feature:settings"))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.lifecycle.process)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.google.material)
    implementation(libs.koin.android)
    implementation(platform(libs.androidx.compose.bom))
}
