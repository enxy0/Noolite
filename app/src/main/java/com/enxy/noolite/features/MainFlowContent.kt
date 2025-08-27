package com.enxy.noolite.features

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.experimental.stack.ChildStack
import com.arkivanov.decompose.extensions.compose.experimental.stack.animation.PredictiveBackParams
import com.arkivanov.decompose.extensions.compose.experimental.stack.animation.stackAnimation
import com.enxy.noolite.features.detail.DetailsContent
import com.enxy.noolite.features.home.HomeContent
import com.enxy.noolite.features.script.ScriptContent
import com.enxy.noolite.features.settings.SettingsContent
import com.enxy.noolite.utils.slideInFastOutSlowExperimental
import com.enxy.noolite.utils.slidePredictiveBackAnimatable

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
