package com.enxy.noolite // do not move to another package

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.arkivanov.decompose.retainedComponent
import com.enxy.noolite.presentation.ui.MainFlowComponent
import com.enxy.noolite.presentation.ui.MainFlowComponentImpl
import com.enxy.noolite.presentation.ui.MainFlowContent
import com.enxy.noolite.ui.theme.NooliteTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
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
