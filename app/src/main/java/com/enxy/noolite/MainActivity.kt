package com.enxy.noolite

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.arkivanov.decompose.retainedComponent
import com.enxy.noolite.core.model.AppSettings
import com.enxy.noolite.core.ui.theme.NooliteTheme
import com.enxy.noolite.domain.common.GetAppSettingsUseCase
import com.enxy.noolite.utils.darkScrim
import com.enxy.noolite.utils.isSystemInDarkTheme
import com.enxy.noolite.utils.lightScrim
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private var isDarkTheme by mutableStateOf<Boolean?>(null)
    private val getAppSettingsUseCase by inject<GetAppSettingsUseCase>()

    override fun onCreate(savedInstanceState: Bundle?) {
        setupSplashScreen()
        enableEdgeToEdge()
        setupAppTheme()
        super.onCreate(savedInstanceState)
        val component: MainFlowComponent = retainedComponent { context ->
            MainFlowComponentImpl(
                componentContext = context,
            )
        }
        setContent {
            val isDarkTheme = isDarkTheme ?: return@setContent
            NooliteTheme(
                darkTheme = isDarkTheme
            ) {
                MainFlowContent(
                    component = component,
                )
            }
        }
    }

    private fun setupAppTheme() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                combine(
                    isSystemInDarkTheme(),
                    getAppSettingsUseCase(Unit)
                        .map { it.getOrNull()?.theme }
                        .distinctUntilChanged()
                ) { systemDark, theme ->
                    shouldUseDarkTheme(theme, systemDark).also { isDarkTheme = it }
                }.collect { darkTheme ->
                    enableEdgeToEdge(
                        statusBarStyle = SystemBarStyle.auto(
                            lightScrim = android.graphics.Color.TRANSPARENT,
                            darkScrim = android.graphics.Color.TRANSPARENT,
                        ) { darkTheme },
                        navigationBarStyle = SystemBarStyle.auto(
                            lightScrim = lightScrim,
                            darkScrim = darkScrim,
                        ) { darkTheme },
                    )
                }
            }
        }
    }

    private fun shouldUseDarkTheme(
        theme: AppSettings.Theme?,
        systemDark: Boolean
    ): Boolean = when (theme) {
        AppSettings.Theme.LIGHT -> false
        AppSettings.Theme.DARK -> true
        else -> systemDark
    }

    private fun setupSplashScreen() {
        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition { theme == null }
    }
}
