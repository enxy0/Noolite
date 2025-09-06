plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.enxy.noolite.core.ui"
    compileSdk = 36

    defaultConfig {
        minSdk = 23
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
        compose = true
    }
}

dependencies {
    api(project(":core:model"))
    api(project(":core:common"))
    api(platform(libs.androidx.compose.bom))
    api(libs.orbit.mvi.core)
    api(libs.orbit.mvi.compose)
    api(project.dependencies.platform(libs.koin.bom))
    api(libs.koin.compose)
    api(libs.decompose)
    api(libs.decompose.compose)
    api(libs.decompose.compose.experimental)
    api(libs.androidx.material3)
    api(libs.androidx.ui)
    api(libs.androidx.ui.graphics)
    api(libs.androidx.ui.tooling.preview)
    api(libs.timber)
    debugApi(libs.androidx.ui.tooling)
    debugApi(libs.androidx.ui.test.manifest)
    androidTestApi(platform(libs.androidx.compose.bom))
    api(libs.haze)
    api(libs.haze.materials)
}
