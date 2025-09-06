package com.enxy.noolite

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.experimental.stack.ChildStack
import com.arkivanov.decompose.extensions.compose.experimental.stack.animation.PredictiveBackParams
import com.arkivanov.decompose.extensions.compose.experimental.stack.animation.stackAnimation
import com.enxy.noolite.core.ui.compose.slideInFastOutSlowExperimental
import com.enxy.noolite.core.ui.compose.slidePredictiveBackAnimatable
import com.enxy.noolite.feature.detail.DetailsContent
import com.enxy.noolite.feature.home.HomeContent
import com.enxy.noolite.feature.script.ScriptContent
import com.enxy.noolite.feature.settings.SettingsContent

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
