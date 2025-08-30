package com.enxy.noolite.features

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackHandlerOwner
import com.enxy.noolite.domain.common.Group
import com.enxy.noolite.domain.features.settings.GetAppSettingsUseCase
import com.enxy.noolite.domain.features.settings.model.AppSettings
import com.enxy.noolite.features.detail.DetailsComponent
import com.enxy.noolite.features.detail.DetailsComponentImpl
import com.enxy.noolite.features.home.HomeComponent
import com.enxy.noolite.features.home.HomeComponentImpl
import com.enxy.noolite.features.script.ScriptComponent
import com.enxy.noolite.features.script.ScriptComponentImpl
import com.enxy.noolite.features.settings.SettingsComponent
import com.enxy.noolite.features.settings.SettingsComponentImpl
import com.enxy.noolite.utils.extensions.componentScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface MainFlowComponent : BackHandlerOwner {
    val childStack: Value<ChildStack<*, Child>>
    val settings: StateFlow<AppSettings?>

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
    ComponentContext by componentContext,
    KoinComponent {

    private val getAppSettingsUseCase: GetAppSettingsUseCase by inject()
    private val scope = componentScope()

    private val navigation = StackNavigation<Config>()
    override val childStack: Value<ChildStack<*, MainFlowComponent.Child>> = childStack(
        source = navigation,
        serializer = Config.serializer(),
        initialConfiguration = Config.Home,
        handleBackButton = true,
        childFactory = ::createChild,
    )
    override val settings: StateFlow<AppSettings?> = getAppSettingsUseCase(Unit)
        .map { it.getOrNull() }
        .stateIn(scope, SharingStarted.Eagerly, null)

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
        onScripCreated = navigation::pop,
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
