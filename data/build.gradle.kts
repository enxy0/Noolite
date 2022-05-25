plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("plugin.serialization") version Versions.KOTLIN
    id("com.google.devtools.ksp") version Versions.KSP
}

android {
    compileSdk = Configuration.compileSdk

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles += file("consumer-rules.pro")

        javaCompileOptions {
            annotationProcessorOptions {
                arguments += mapOf(
                    "room.schemaLocation" to "$projectDir/schemas",
                    "room.incremental" to "true"
                )
            }
        }
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
    implementation(project(path = ":domain"))

    // Squareup
    implementation(Dependencies.Squareup.RETROFIT)
    implementation(Dependencies.Squareup.OKHTTP)
    implementation(Dependencies.Squareup.OKHTTP_LOGGING)

    // Koin
    implementation(Dependencies.Koin.CORE)
    ksp(Dependencies.Koin.KSP_COMPILER)

    // Serialization
    implementation(Dependencies.Kotlin.SERIALIZATION)
    implementation(Dependencies.SERIALIZATION_CONVERTER)

    // Room
    implementation(Dependencies.AndroidX.Room.RUNTIME)
    implementation(Dependencies.AndroidX.Ktx.ROOM)
    ksp(Dependencies.AndroidX.Room.COMPILER)

    // Test
    testImplementation(Dependencies.Test.JUNIT)
    androidTestImplementation(Dependencies.Test.JUNIT_EXT)
    androidTestImplementation(Dependencies.Test.ESPRESSO)

    // Other
    implementation(Dependencies.TIMBER)
}
