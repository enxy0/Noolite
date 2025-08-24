package com.enxy.noolite.presentation.ui.settings

import android.content.res.Configuration
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.enxy.noolite.BuildConfig
import com.enxy.noolite.R
import com.enxy.noolite.domain.features.common.Event
import com.enxy.noolite.domain.features.settings.model.AppSettings
import com.enxy.noolite.presentation.ui.common.AppTextField
import com.enxy.noolite.presentation.ui.common.ShapeIcon
import com.enxy.noolite.presentation.ui.common.TopAppBar
import com.enxy.noolite.presentation.ui.settings.model.SettingsAction
import com.enxy.noolite.presentation.utils.ThemedPreview
import com.enxy.noolite.presentation.utils.intent.IntentActionsProvider
import org.koin.compose.koinInject

private class SettingsState {
    companion object {
        val ICON_SIZE = 56.dp
    }

    var onDarkThemeChange: (isChecked: Boolean) -> Unit = {}
    var onNotifyWifiChange: (isChecked: Boolean) -> Unit = {}
    var onApiUrlChange: (apiUrl: String) -> Unit = {}
    var onGitHubClick: () -> Unit = {}
    var onTestDataClick: () -> Unit = {}
    var onBackClick: () -> Unit = {}
}

@Composable
fun SettingsScreen(
    component: SettingsComponent,
    navigateUp: () -> Unit,
) {
    val state = rememberSettingsState(navigateUp, component)
    val event by component.eventsFlow.collectAsState()
    val settings by component.settingsFlow.collectAsState()
    SettingsContent(settings, event, state)
}

@Composable
private fun rememberSettingsState(
    navigateUp: () -> Unit,
    component: SettingsComponent,
    intentActionsProvider: IntentActionsProvider = koinInject()
): SettingsState = remember {
    SettingsState().apply {
        onDarkThemeChange = component::setDarkTheme
        onNotifyWifiChange = component::setNotifyWifiChange
        onApiUrlChange = component::setApiUrl
        onGitHubClick = { intentActionsProvider.openGithubProject() }
        onTestDataClick = component::setTestData
        onBackClick = navigateUp
    }
}

@Composable
private fun SettingsContent(
    settings: AppSettings, event: Event<SettingsAction>?, state: SettingsState
) {
    Scaffold(
        topBar = { TopAppBar(stringResource(R.string.settings_title), state.onBackClick) },
        modifier = Modifier.fillMaxSize(),
        content = { contentPadding ->
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                SettingsItems(settings, state, contentPadding)
                SettingsEvent(event) // TODO: Move to Scaffolds parameter
            }
        }
    )
}

@Composable
private fun SettingsEvent(event: Event<SettingsAction>?) {
    val snackbarHostState by remember { mutableStateOf(SnackbarHostState()) }
    LaunchedEffect(event) {
        event?.getValueIfNotHandled()?.let { action ->
            if (action is SettingsAction.ShowSnackbar) {
                snackbarHostState.showSnackbar(
                    message = action.message, duration = SnackbarDuration.Short
                )
            }
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun SettingsItems(
    settings: AppSettings,
    state: SettingsState,
    contentPadding: PaddingValues,
) {
    LazyColumn(
        contentPadding = contentPadding,
        modifier = Modifier.fillMaxSize()
    ) {
        item { Section(R.string.settings_section_appearance) }
        item {
            SwitchSettingsItem(
                icon = painterResource(R.drawable.ic_on),
                title = stringResource(R.string.settings_theme_title),
                isChecked = settings.isDarkTheme,
                description = stringResource(R.string.settings_theme_description),
                onCheckedChange = state.onDarkThemeChange
            )
        }
        item { Section(R.string.settings_section_server) }
        item {
            ServerSettingsItem(
                icon = painterResource(R.drawable.ic_server),
                title = stringResource(R.string.settings_server_title),
                apiUrl = settings.apiUrl,
                onApiUrlChange = state.onApiUrlChange
            )
        }
        item { Section(R.string.settings_section_other) }
        /*
        item {
            SwitchSettingsItem(
                icon = painterResource(R.drawable.ic_wifi),
                title = stringResource(R.string.settings_wifi_title),
                description = stringResource(R.string.settings_wifi_description),
                isChecked = settings.notifyWifiChange,
                onCheckedChange = state.onNotifyWifiChange
            )
        }*/
        item {
            SettingsItem(
                icon = painterResource(R.drawable.ic_github),
                title = stringResource(R.string.settings_github_title),
                description = stringResource(R.string.settings_github_description),
                onClick = state.onGitHubClick
            )
        }
        item {
            SettingsItem(
                icon = painterResource(R.drawable.ic_bug),
                title = stringResource(R.string.settings_test_title),
                description = stringResource(R.string.settings_test_description),
                onClick = state.onTestDataClick
            )
        }
        item {
            SettingsItem(
                icon = painterResource(R.drawable.ic_info),
                title = stringResource(R.string.settings_app_version),
                description = "${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})"
            )
        }
    }
}

@Composable
private fun ServerSettingsItem(
    icon: Painter,
    title: String,
    apiUrl: String,
    onApiUrlChange: (apiUrl: String) -> Unit
) {
    val focusManager = LocalFocusManager.current
    var text by remember { mutableStateOf(apiUrl) }
    LaunchedEffect(apiUrl) { text = apiUrl }
    Row(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ShapeIcon(icon)
                AppTextField(
                    text = text,
                    label = title,
                    onTextChange = { value -> text = value },
                    modifier = Modifier.weight(1f)
                )
            }
            Button(
                onClick = {
                    focusManager.clearFocus()
                    onApiUrlChange(text)
                },
                elevation = null,
                modifier = Modifier
                    .padding(start = SettingsState.ICON_SIZE + 16.dp)
                    .fillMaxWidth()
            ) {
                Text(stringResource(R.string.settings_server_update))
            }
        }
    }
}

@Composable
private fun SwitchSettingsItem(
    title: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    description: String = "",
    icon: Painter? = null
) {
    Row(
        modifier = Modifier
            .clickable { onCheckedChange(!isChecked) }
            .padding(16.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        if (icon != null) {
            ShapeIcon(icon)
        } else {
            Spacer(modifier = Modifier.width(SettingsState.ICON_SIZE))
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.bodyMedium)
            Text(description, style = MaterialTheme.typography.bodySmall)
        }
        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Composable
private fun SettingsItem(
    title: String,
    onClick: (() -> Unit)? = null,
    description: String = "",
    icon: Painter? = null
) {
    Row(
        modifier = Modifier
            .clickable(
                enabled = onClick != null,
                onClick = { onClick?.invoke() }
            )
            .padding(16.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (icon != null) {
            ShapeIcon(icon)
        } else {
            Spacer(modifier = Modifier.width(SettingsState.ICON_SIZE))
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.bodyMedium)
            Text(description, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
private fun Section(@StringRes titleResId: Int) {
    Text(
        text = stringResource(titleResId),
        style = MaterialTheme.typography.headlineSmall,
        modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp)
    )
}

@Preview("Settings screen")
@Preview("Settings screen (dark)", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewDetailsScreen() {
    ThemedPreview {
        SettingsContent(AppSettings.default(), null, SettingsState())
    }
}
