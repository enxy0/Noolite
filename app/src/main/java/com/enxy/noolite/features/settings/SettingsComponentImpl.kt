package com.enxy.noolite.features.settings

import android.content.Context
import com.arkivanov.decompose.ComponentContext
import com.enxy.noolite.R
import com.enxy.noolite.domain.features.demo.SetDemoInfoUseCase
import com.enxy.noolite.domain.features.settings.GetAppSettingsUseCase
import com.enxy.noolite.domain.features.settings.GetNooliteGroupsUseCase
import com.enxy.noolite.domain.features.settings.UpdateAppSettingsUseCase
import com.enxy.noolite.domain.features.settings.model.AppSettings
import com.enxy.noolite.domain.features.settings.model.NooliteSettingsPayload
import com.enxy.noolite.utils.extensions.componentScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container
import timber.log.Timber

interface SettingsComponent : ContainerHost<AppSettings, SettingsSideEffect> {
    fun onChangeAppTheme(darkTheme: Boolean)
    fun onChangeApiUrlClick(apiUrl: String)
    fun onSetTestDataClick()
    fun onBackClick()
}

class SettingsComponentImpl(
    componentContext: ComponentContext,
    private val onBackClicked: () -> Unit,
) : ComponentContext by componentContext,
    SettingsComponent,
    KoinComponent {

    private val scope = componentScope()
    private val context: Context by inject()
    private val getAppSettingsUseCase: GetAppSettingsUseCase by inject()
    private val updateAppSettingsUseCase: UpdateAppSettingsUseCase by inject()
    private val getNooliteGroupsUseCase: GetNooliteGroupsUseCase by inject()
    private val setDemoInfoUseCase: SetDemoInfoUseCase by inject()

    override val container = scope.container<AppSettings, SettingsSideEffect>(AppSettings.default())

    init {
        loadSettings()
    }

    override fun onChangeAppTheme(darkTheme: Boolean) {
        intent {
            val theme = AppSettings.Theme.from(darkTheme)
            val settings = container.stateFlow.value.copy(darkTheme = theme)
            updateAppSettingsUseCase(settings).collect()
        }
    }

    override fun onChangeApiUrlClick(apiUrl: String) {
        intent {
            getNooliteGroupsUseCase(NooliteSettingsPayload(apiUrl)).collectLatest { result ->
                result
                    .onSuccess {
                        val message = context.getString(R.string.settings_update_groups_success)
                        postSideEffect(SettingsSideEffect.Message(message))
                        val settings = container.stateFlow.value.copy(apiUrl = apiUrl)
                        updateAppSettingsUseCase(settings).collect()
                    }
                    .onFailure {
                        Timber.e(it)
                        val message = context.getString(R.string.settings_update_groups_failure)
                        postSideEffect(SettingsSideEffect.Message(message))
                    }
            }
        }
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

    private fun loadSettings() = intent {
        getAppSettingsUseCase(Unit)
            .collectLatest { result ->
                result
                    .onSuccess { settings ->
                        reduce { settings }
                    }
                    .onFailure {
                        Timber.e(it)
                    }
            }
    }
}
