package com.enxy.noolite // do not move to another package

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.arkivanov.decompose.retainedComponent
import com.enxy.noolite.core.ui.theme.NooliteTheme
import com.enxy.noolite.features.MainFlowComponent
import com.enxy.noolite.features.MainFlowComponentImpl
import com.enxy.noolite.features.MainFlowContent

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        val component: MainFlowComponent = retainedComponent { context ->
            MainFlowComponentImpl(
                componentContext = context,
            )
        }

        setContent {
            NooliteTheme {
                MainFlowContent(
                    component = component,
                )
            }
        }
    }
}
