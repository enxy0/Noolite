package com.enxy.noolite.presentation.ui

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackHandlerOwner
import com.enxy.noolite.domain.features.common.Group
import com.enxy.noolite.presentation.ui.detail.DetailsComponent
import com.enxy.noolite.presentation.ui.detail.DetailsComponentImpl
import com.enxy.noolite.presentation.ui.home.HomeComponent
import com.enxy.noolite.presentation.ui.home.HomeComponentImpl
import com.enxy.noolite.presentation.ui.script.ScriptComponent
import com.enxy.noolite.presentation.ui.script.ScriptComponentImpl
import com.enxy.noolite.presentation.ui.settings.SettingsComponent
import com.enxy.noolite.presentation.ui.settings.SettingsComponentImpl
import kotlinx.serialization.Serializable

interface MainFlowComponent : BackHandlerOwner {
    val childStack: Value<ChildStack<*, Child>>

    fun onBackClick()

    sealed interface Child {
        class Home(val component: HomeComponent) : Child
        class Settings(val component: SettingsComponent) : Child
        class Script(val component: ScriptComponent) : Child
        class Details(val component: DetailsComponent) : Child
    }
}

class MainFlowComponentImpl(
    componentContext: ComponentContext,
) : MainFlowComponent,
    ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()
    override val childStack: Value<ChildStack<*, MainFlowComponent.Child>> = childStack(
        source = navigation,
        serializer = Config.serializer(),
        initialConfiguration = Config.Home,
        handleBackButton = true,
        childFactory = ::createChild,
    )

    override fun onBackClick() {
        navigation.pop()
    }

    private fun createChild(
        config: Config,
        componentContext: ComponentContext,
    ): MainFlowComponent.Child = when (config) {
        is Config.Details -> {
            MainFlowComponent.Child.Details(
                component = detailsComponent(
                    componentContext = componentContext,
                    config = config,
                )
            )
        }
        is Config.Home -> {
            MainFlowComponent.Child.Home(
                component = homeComponent(
                    componentContext = componentContext,
                )
            )
        }
        is Config.Script -> {
            MainFlowComponent.Child.Script(
                component = scriptComponent(
                    componentContext = componentContext,
                )
            )
        }
        is Config.Settings -> {
            MainFlowComponent.Child.Settings(
                component = settingsComponent(
                    componentContext = componentContext,
                )
            )
        }
    }

    private fun detailsComponent(
        componentContext: ComponentContext,
        config: Config.Details
    ): DetailsComponent = DetailsComponentImpl(
        componentContext = componentContext,
        group = config.group,
        onBackClicked = ::onBackClick,
    )

    private fun homeComponent(
        componentContext: ComponentContext,
    ): HomeComponent = HomeComponentImpl(
        componentContext = componentContext,
        onSettingsClicked = { navigation.pushNew(Config.Settings) },
        onGroupClicked = { group -> navigation.pushNew(Config.Details(group)) },
        onAddScriptClicked = { navigation.pushNew(Config.Script) }
    )

    private fun scriptComponent(
        componentContext: ComponentContext,
    ): ScriptComponent = ScriptComponentImpl(
        componentContext = componentContext,
        onBackClicked = ::onBackClick,
    )

    private fun settingsComponent(
        componentContext: ComponentContext,
    ): SettingsComponent = SettingsComponentImpl(
        componentContext = componentContext,
        onBackClicked = ::onBackClick,
    )

    @Serializable
    sealed interface Config {
        @Serializable
        data object Home : Config

        @Serializable
        data object Settings : Config

        @Serializable
        data object Script : Config

        @Serializable
        data class Details(val group: Group) : Config
    }
}
