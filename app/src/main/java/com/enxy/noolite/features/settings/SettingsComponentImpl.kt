package com.enxy.noolite.features.settings

import android.content.Context
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.slot.dismiss
import com.arkivanov.decompose.value.Value
import com.enxy.noolite.R
import com.enxy.noolite.domain.features.demo.SetDemoInfoUseCase
import com.enxy.noolite.domain.features.settings.GetAppSettingsUseCase
import com.enxy.noolite.domain.features.settings.GetNooliteGroupsUseCase
import com.enxy.noolite.domain.features.settings.UpdateAppSettingsUseCase
import com.enxy.noolite.domain.features.settings.model.NooliteSettingsPayload
import com.enxy.noolite.features.settings.model.SettingsState
import com.enxy.noolite.features.settings.model.toAppSettings
import com.enxy.noolite.features.settings.theme.ChangeThemeComponent
import com.enxy.noolite.features.settings.theme.ChangeThemeComponentImpl
import com.enxy.noolite.features.settings.url.ChangeApiUrlComponent
import com.enxy.noolite.features.settings.url.ChangeApiUrlComponentImpl
import com.enxy.noolite.utils.extensions.componentScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container
import timber.log.Timber
import kotlin.time.Duration.Companion.milliseconds

interface SettingsComponent : ContainerHost<SettingsState, SettingsSideEffect> {
    val dialogSlot: Value<ChildSlot<*, DialogConfig>>
    fun onSetTestDataClick()
    fun onBackClick()
    fun onChangeThemeClick()
    fun onChangeApiUrlClick()

    sealed class DialogConfig {
        internal class ChangeTheme(val component: ChangeThemeComponent) : DialogConfig()
        internal class ChangeApiUrl(val component: ChangeApiUrlComponent) : DialogConfig()
    }
}

class SettingsComponentImpl(
    componentContext: ComponentContext,
    private val onBackClicked: () -> Unit,
) : ComponentContext by componentContext,
    SettingsComponent,
    KoinComponent {

    companion object {
        private val SettingsThemeChangeDuration = 200.milliseconds
    }

    private val scope = componentScope()
    private val context: Context by inject()
    private val getAppSettingsUseCase: GetAppSettingsUseCase by inject()
    private val updateAppSettingsUseCase: UpdateAppSettingsUseCase by inject()
    private val getNooliteGroupsUseCase: GetNooliteGroupsUseCase by inject()
    private val setDemoInfoUseCase: SetDemoInfoUseCase by inject()

    private val dialogNavigation = SlotNavigation<DialogConfig>()
    override val dialogSlot: Value<ChildSlot<*, SettingsComponent.DialogConfig>> =
        childSlot(
            source = dialogNavigation,
            serializer = DialogConfig.serializer(),
            childFactory = ::createDialogChild,
        )

    override val container =
        scope.container<SettingsState, SettingsSideEffect>(SettingsState.empty())

    init {
        loadSettings()
    }

    override fun onSetTestDataClick() {
        intent {
            setDemoInfoUseCase(Unit).collectLatest { result ->
                result
                    .onSuccess {
                        val message = context.getString(R.string.settings_demo_info_success)
                        postSideEffect(SettingsSideEffect.Message(message))
                    }
                    .onFailure {
                        Timber.e(it)
                        val message = context.getString(R.string.settings_demo_info_failure)
                        postSideEffect(SettingsSideEffect.Message(message))
                    }
            }
        }
    }

    override fun onBackClick() {
        onBackClicked()
    }

    override fun onChangeThemeClick() {
        dialogNavigation.activate(DialogConfig.ChangeTheme)
    }

    override fun onChangeApiUrlClick() {
        dialogNavigation.activate(DialogConfig.ChangeApiUrl(container.stateFlow.value.apiUrl))
    }

    private fun loadSettings() = intent {
        getAppSettingsUseCase(Unit)
            .collectLatest { result ->
                result
                    .onSuccess { settings ->
                        reduce { state.copy(settings) }
                    }
                    .onFailure {
                        Timber.e(it)
                    }
            }
    }

    private fun onChangeApiUrlClick(apiUrl: String) {
        intent {
            reduce { state.copy(apiUrlChanging = true) }
            getNooliteGroupsUseCase(NooliteSettingsPayload(apiUrl)).collectLatest { result ->
                result
                    .onSuccess {
                        val message = context.getString(R.string.settings_update_groups_success)
                        postSideEffect(SettingsSideEffect.Message(message))
                        val settings = container.stateFlow.value.copy(apiUrl = apiUrl)
                        updateAppSettingsUseCase(settings.toAppSettings()).collect()
                    }
                    .onFailure {
                        Timber.e(it)
                        val message = context.getString(R.string.settings_update_groups_failure)
                        postSideEffect(SettingsSideEffect.Message(message))
                    }
                reduce { state.copy(apiUrlChanging = false) }
            }
        }
    }

    private fun createDialogChild(
        config: DialogConfig,
        componentContext: ComponentContext
    ): SettingsComponent.DialogConfig = when (config) {
        is DialogConfig.ChangeTheme -> {
            SettingsComponent.DialogConfig.ChangeTheme(
                component = changeThemeComponent(
                    componentContext = componentContext,
                )
            )
        }
        is DialogConfig.ChangeApiUrl -> {
            SettingsComponent.DialogConfig.ChangeApiUrl(
                component = changeApiUrlComponent(
                    componentContext = componentContext,
                    apiUrl = config.apiUrl,
                )
            )
        }
    }

    private fun changeApiUrlComponent(
        componentContext: ComponentContext,
        apiUrl: String
    ): ChangeApiUrlComponent = ChangeApiUrlComponentImpl(
        componentContext = componentContext,
        currentApiUrl = apiUrl,
        onDismissed = dialogNavigation::dismiss,
        onUrlChanged = { apiUrl ->
            dialogNavigation.dismiss {
                onChangeApiUrlClick(apiUrl)
            }
        }
    )

    private fun changeThemeComponent(
        componentContext: ComponentContext,
    ): ChangeThemeComponent = ChangeThemeComponentImpl(
        componentContext = componentContext,
        onThemeChanged = { theme ->
            dialogNavigation.dismiss {
                intent {
                    delay(SettingsThemeChangeDuration)
                    val settings = container.stateFlow.value.toAppSettings()
                    updateAppSettingsUseCase(settings.copy(theme = theme)).collect()
                }
            }
        },
        onDismissed = dialogNavigation::dismiss,
    )

    @Serializable
    private sealed interface DialogConfig {
        @Serializable
        data object ChangeTheme : DialogConfig

        @Serializable
        data class ChangeApiUrl(val apiUrl: String) : DialogConfig
    }
}
