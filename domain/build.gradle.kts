plugins {
    id("com.android.library")
    kotlin("android")
    id("com.google.devtools.ksp") version Versions.KSP
    id("kotlin-parcelize")
}

android {
    compileSdk = Configuration.compileSdk

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles += file("consumer-rules.pro")
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
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
}

dependencies {
    // Koin
    implementation(Dependencies.Koin.CORE)
    ksp(Dependencies.Koin.KSP_COMPILER)

    // Test
    testImplementation(Dependencies.Test.JUNIT)
    androidTestImplementation(Dependencies.Test.JUNIT_EXT)
    androidTestImplementation(Dependencies.Test.ESPRESSO)

    // Other
    implementation(Dependencies.AndroidX.APPCOMPAT)
    implementation(Dependencies.TIMBER)
}