package com.enxy.noolite.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.experimental.stack.ChildStack
import com.arkivanov.decompose.extensions.compose.experimental.stack.animation.PredictiveBackParams
import com.arkivanov.decompose.extensions.compose.experimental.stack.animation.stackAnimation
import com.enxy.noolite.presentation.ui.detail.DetailsContent
import com.enxy.noolite.presentation.ui.home.HomeContent
import com.enxy.noolite.presentation.ui.script.ScriptContent
import com.enxy.noolite.presentation.ui.settings.SettingsContent
import com.enxy.noolite.presentation.utils.slideInFastOutSlowExperimental
import com.enxy.noolite.presentation.utils.slidePredictiveBackAnimatable

@OptIn(ExperimentalDecomposeApi::class)
@Composable
fun MainFlowContent(
    component: MainFlowComponent,
    modifier: Modifier = Modifier,
) {
    ChildStack(
        stack = component.childStack,
        animation = stackAnimation(
            animator = slideInFastOutSlowExperimental(),
            predictiveBackParams = {
                PredictiveBackParams(
                    backHandler = component.backHandler,
                    onBack = component::onBackClick,
                    animatable = ::slidePredictiveBackAnimatable,
                )
            }
        ),
        modifier = modifier,
    ) {
        when (val child = it.instance) {
            is MainFlowComponent.Child.Details -> {
                DetailsContent(child.component)
            }
            is MainFlowComponent.Child.Home -> {
                HomeContent(child.component)
            }
            is MainFlowComponent.Child.Script -> {
                ScriptContent(child.component)
            }
            is MainFlowComponent.Child.Settings -> {
                SettingsContent(child.component)
            }
        }
    }
}
