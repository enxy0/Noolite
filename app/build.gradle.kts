plugins {
    id("com.android.application")
    kotlin("android")
    id("com.google.devtools.ksp") version Versions.KSP
    id("androidx.navigation.safeargs.kotlin")
}

android {
    compileSdk = Configuration.compileSdk

    defaultConfig {
        applicationId = Configuration.applicationId
        minSdk = Configuration.minSdk
        targetSdk = Configuration.targetSdk
        versionCode = Configuration.versionCode
        versionName = Configuration.versionName
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        vectorDrawables {
            useSupportLibrary = true
        }

        signingConfigs {
            getByName("debug") {
                storeFile = file("keys/test-key")
                storePassword = "123456"
                keyPassword = "123456"
                keyAlias = "upload"
            }
            create("release") {
                storeFile = file("keys/test-key")
                storePassword = "123456"
                keyPassword = "123456"
                keyAlias = "upload"
            }
        }
    }

    buildTypes {
        getByName("debug") {
            signingConfig = signingConfigs.getByName("debug")
        }
        getByName("release") {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Versions.COMPOSE
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(project(path = ":data"))
    implementation(project(path = ":domain"))

    // Ktx
    implementation(Dependencies.AndroidX.Ktx.CORE)
    implementation(Dependencies.AndroidX.Ktx.LIFECYCLE_RUNTIME)
    implementation(Dependencies.AndroidX.Ktx.UI)
    implementation(Dependencies.AndroidX.Ktx.FRAGMENT)
    implementation(Dependencies.AndroidX.Ktx.LIVEDATA)

    // Compose
    implementation(Dependencies.AndroidX.Compose.UI)
    implementation(Dependencies.AndroidX.Compose.MATERIAL)
    implementation(Dependencies.AndroidX.Compose.UI_TOOLING_PREVIEW)
    implementation(Dependencies.AndroidX.Compose.ACTIVITY)
    debugImplementation(Dependencies.AndroidX.Compose.UI_TOOLING)

    // Koin
    implementation(Dependencies.Koin.CORE)
    implementation(Dependencies.Koin.COMPOSE)
    ksp(Dependencies.Koin.KSP_COMPILER)

    // Test
    testImplementation(Dependencies.Test.JUNIT)
    androidTestImplementation(Dependencies.Test.JUNIT_EXT)
    androidTestImplementation(Dependencies.Test.ESPRESSO)
    androidTestImplementation(Dependencies.AndroidX.Compose.UI_TEST_JUNIT)

    // Other
    implementation(Dependencies.AndroidX.APPCOMPAT)
    implementation(Dependencies.AndroidX.LIFECYCLE_PROCESS)
    implementation(Dependencies.MATERIAL)
    implementation(Dependencies.TIMBER)
}
