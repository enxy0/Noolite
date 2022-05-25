object Dependencies {
    const val TIMBER = "com.jakewharton.timber:timber:${Versions.TIMBER}"
    const val MATERIAL = "com.google.android.material:material:${Versions.MATERIAL}"
    const val SERIALIZATION_CONVERTER = "com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:${Versions.SERIALIZATION_CONVERTER}"

    object Koin {
        const val CORE = "io.insert-koin:koin-android:${Versions.KOIN}"
        const val COMPOSE = "io.insert-koin:koin-androidx-compose:${Versions.KOIN}"
        const val KSP_COMPILER = "io.insert-koin:koin-ksp-compiler:${Versions.KOIN_KSP}" // ksp
    }

    object AndroidX {
        const val APPCOMPAT = "androidx.appcompat:appcompat:${Versions.APPCOMPAT}"
        const val LIFECYCLE_PROCESS = "androidx.lifecycle:lifecycle-process:${Versions.LIFECYCLE}"

        object Compose {
            const val UI = "androidx.compose.ui:ui:${Versions.COMPOSE}"
            const val MATERIAL = "androidx.compose.material:material:${Versions.COMPOSE}"
            const val UI_TOOLING = "androidx.compose.ui:ui-tooling:${Versions.COMPOSE}"
            const val UI_TOOLING_PREVIEW = "androidx.compose.ui:ui-tooling-preview:${Versions.COMPOSE}"
            const val UI_TEST_JUNIT = "androidx.compose.ui:ui-test-junit4:${Versions.COMPOSE}"
            const val ACTIVITY = "androidx.activity:activity-compose:${Versions.COMPOSE_ACTIVITY}"
        }

        object Ktx {
            const val CORE = "androidx.core:core-ktx:${Versions.KTX_CORE}"
            const val UI = "androidx.navigation:navigation-ui-ktx:${Versions.NAVIGATION}"
            const val LIFECYCLE_RUNTIME = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.LIFECYCLE}"
            const val FRAGMENT = "androidx.navigation:navigation-fragment-ktx:${Versions.NAVIGATION}"
            const val ROOM = "androidx.room:room-ktx:${Versions.ROOM}"
            const val LIVEDATA = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.LIFECYCLE}"
        }

        object Room {
            const val RUNTIME = "androidx.room:room-runtime:${Versions.ROOM}"
            const val COMPILER = "androidx.room:room-compiler:${Versions.ROOM}"
        }
    }

    object Squareup {
        const val RETROFIT = "com.squareup.retrofit2:retrofit:${Versions.RETROFIT}"
        const val OKHTTP = "com.squareup.okhttp3:okhttp:${Versions.OKHTTP}"
        const val OKHTTP_LOGGING = "com.squareup.okhttp3:logging-interceptor:${Versions.OKHTTP}"
    }

    object Test {
        const val JUNIT = "junit:junit:${Versions.JUNIT}"
        const val JUNIT_EXT = "androidx.test.ext:junit:${Versions.JUNIT_EXT}"
        const val ESPRESSO = "androidx.test.espresso:espresso-core:${Versions.ESPRESSO}"
    }

    object Kotlin {
        const val SERIALIZATION = "org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.SERIALIZATION_JSON}"
    }
}
