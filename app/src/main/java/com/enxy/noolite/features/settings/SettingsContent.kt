package com.enxy.noolite.features.settings

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.enxy.noolite.domain.features.settings.model.AppSettings
import com.enxy.noolite.features.common.AppTextField
import com.enxy.noolite.features.common.ShapeIcon
import com.enxy.noolite.features.common.TopAppBar
import com.enxy.noolite.utils.ThemedPreview
import com.enxy.noolite.utils.intent.IntentActionsProvider
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState
import org.koin.compose.koinInject
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

private val ICON_SIZE = 56.dp

@Composable
fun SettingsContent(component: SettingsComponent) {
    val intentActionsProvider = koinInject<IntentActionsProvider>()
    val settings by component.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    component.collectSideEffect { event ->
        when (event) {
            is SettingsSideEffect.Message -> {
                snackbarHostState.showSnackbar(event.message)
            }
        }
    }
    SettingsScaffold(
        settings = settings,
        snackbarHostState = snackbarHostState,
        onChangeAppTheme = component::onChangeAppTheme,
        onChangeApiUrlClick = component::onChangeApiUrlClick,
        onGitHubClick = { intentActionsProvider.openGithubProject() },
        onSetTestDataClick = component::onSetTestDataClick,
        onBackClick = component::onBackClick,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsScaffold(
    settings: AppSettings,
    snackbarHostState: SnackbarHostState,
    onChangeAppTheme: (isChecked: Boolean) -> Unit,
    onChangeApiUrlClick: (apiUrl: String) -> Unit,
    onGitHubClick: () -> Unit,
    onSetTestDataClick: () -> Unit,
    onBackClick: () -> Unit,
) {
    val hazeState = rememberHazeState()
    Scaffold(
        topBar = {
            SettingsTopAppBar(
                hazeState = hazeState,
                onBackClick = onBackClick,
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        contentWindowInsets = WindowInsets.systemBars.add(WindowInsets.displayCutout),
        modifier = Modifier.fillMaxSize(),
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .hazeSource(state = hazeState)
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(contentPadding)
        ) {
            SectionTitle(
                text = stringResource(R.string.settings_section_appearance),
                modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp),
            )
            SwitchSettingsItem(
                icon = painterResource(R.drawable.ic_on),
                title = stringResource(R.string.settings_theme_title),
                isChecked = settings.isDarkTheme,
                description = stringResource(R.string.settings_theme_description),
                onCheckedChange = onChangeAppTheme,
            )
            SectionTitle(
                text = stringResource(R.string.settings_section_server),
                modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
            )
            ServerSettingsItem(
                icon = painterResource(R.drawable.ic_server),
                title = stringResource(R.string.settings_server_title),
                apiUrl = settings.apiUrl,
                onChangeApiUrlClick = onChangeApiUrlClick,
            )
            SectionTitle(
                text = stringResource(R.string.settings_section_other),
                modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
            )
            SettingsItem(
                icon = painterResource(R.drawable.ic_github),
                title = stringResource(R.string.settings_github_title),
                description = stringResource(R.string.settings_github_description),
                onClick = onGitHubClick
            )
            SettingsItem(
                icon = painterResource(R.drawable.ic_bug),
                title = stringResource(R.string.settings_test_title),
                description = stringResource(R.string.settings_test_description),
                onClick = onSetTestDataClick
            )
            SettingsItem(
                icon = painterResource(R.drawable.ic_info),
                title = stringResource(R.string.settings_app_version),
                description = "${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})"
            )
            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun SettingsTopAppBar(
    hazeState: HazeState,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        title = { Text(stringResource(R.string.settings_title)) },
        hazeState = hazeState,
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = null,
                )
            }
        },
        modifier = modifier,
    )
}

@Composable
private fun ServerSettingsItem(
    icon: Painter,
    title: String,
    apiUrl: String,
    onChangeApiUrlClick: (apiUrl: String) -> Unit
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
            FilledTonalButton(
                onClick = {
                    focusManager.clearFocus()
                    onChangeApiUrlClick(text)
                },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .padding(start = ICON_SIZE + 16.dp)
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
    icon: Painter? = null,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .clickable { onCheckedChange(!isChecked) }
            .padding(16.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        if (icon != null) {
            ShapeIcon(icon)
        } else {
            Spacer(modifier = Modifier.width(ICON_SIZE))
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
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
            Spacer(modifier = Modifier.width(ICON_SIZE))
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun SectionTitle(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleLarge,
        modifier = modifier,
    )
}

@Preview("Settings screen")
@Preview("Settings screen (dark)", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewDetailsScreen() {
    ThemedPreview {
        SettingsScaffold(
            settings = AppSettings.default(),
            snackbarHostState = remember { SnackbarHostState() },
            onChangeAppTheme = {},
            onChangeApiUrlClick = {},
            onGitHubClick = {},
            onSetTestDataClick = {},
            onBackClick = {},
        )
    }
}
