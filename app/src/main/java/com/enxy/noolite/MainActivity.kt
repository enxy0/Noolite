package com.enxy.noolite // do not move to another package

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.arkivanov.decompose.retainedComponent
import com.enxy.noolite.presentation.ui.home.HomeComponent
import com.enxy.noolite.presentation.ui.home.HomeContent
import com.enxy.noolite.ui.theme.NooliteTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        val component = retainedComponent { context ->
            HomeComponent(
                componentContext = context,
            )
        }
        setContent {
            NooliteTheme {
                HomeContent(
                    component = component,
                )
            }
        }
    }
}
