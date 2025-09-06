package com.enxy.noolite

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.arkivanov.decompose.retainedComponent
import com.enxy.noolite.core.ui.theme.NooliteTheme

class MainActivity : AppCompatActivity() {

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
