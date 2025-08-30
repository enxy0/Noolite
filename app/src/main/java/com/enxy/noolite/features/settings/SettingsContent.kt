package com.enxy.noolite.features.settings

import android.content.res.Configuration
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.enxy.noolite.BuildConfig
import com.enxy.noolite.R
import com.enxy.noolite.features.common.AppTextField
import com.enxy.noolite.features.common.TopAppBar
import com.enxy.noolite.features.settings.model.SettingsState
import com.enxy.noolite.features.settings.theme.ChangeThemeBottomSheetContent
import com.enxy.noolite.utils.ThemedPreview
import com.enxy.noolite.utils.intent.IntentActionsProvider
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState
import org.koin.compose.koinInject
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

private val SettingIconSize = 56.dp
private val SettingActionIconSize = 24.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsContent(component: SettingsComponent) {
    val intentActionsProvider = koinInject<IntentActionsProvider>()
    val state by component.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    component.collectSideEffect { event ->
        when (event) {
            is SettingsSideEffect.Message -> {
                snackbarHostState.showSnackbar(event.message)
            }
        }
    }
    Surface(color = MaterialTheme.colorScheme.background) {
        SettingsScaffold(
            state = state,
            snackbarHostState = snackbarHostState,
            onChangeApiUrlClick = component::onChangeApiUrlClick,
            onGitHubClick = { intentActionsProvider.openGithubProject() },
            onSetTestDataClick = component::onSetTestDataClick,
            onBackClick = component::onBackClick,
            onChangeThemeClick = component::onChangeThemeClick,
        )
        val dialogSlot = component.dialogSlot.subscribeAsState()
        when (val child = dialogSlot.value.child?.instance) {
            is SettingsComponent.DialogConfig.ChangeTheme -> {
                ModalBottomSheet(
                    onDismissRequest = child.component::onDismiss,
                ) {
                    ChangeThemeBottomSheetContent(child.component)
                }
            }
            null -> Unit
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsScaffold(
    state: SettingsState,
    snackbarHostState: SnackbarHostState,
    onChangeApiUrlClick: (apiUrl: String) -> Unit,
    onGitHubClick: () -> Unit,
    onSetTestDataClick: () -> Unit,
    onBackClick: () -> Unit,
    onChangeThemeClick: () -> Unit,
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
            Spacer(Modifier.height(16.dp))
            ServerSettingsItem(
                icon = painterResource(R.drawable.ic_server),
                title = stringResource(R.string.settings_server_title),
                apiUrl = state.apiUrl,
                apiUrlChanging = state.apiUrlChanging,
                onChangeApiUrlClick = onChangeApiUrlClick,
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.padding(horizontal = 16.dp),
            )
            Spacer(Modifier.height(16.dp))
            SettingsItem(
                icon = painterResource(R.drawable.ic_on),
                title = stringResource(R.string.settings_theme_title),
                description = state.theme.asString(),
                onClick = onChangeThemeClick,
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                actionIcon = rememberVectorPainter(Icons.AutoMirrored.Rounded.KeyboardArrowRight),
                modifier = Modifier.padding(horizontal = 16.dp),
            )
            Spacer(Modifier.height(4.dp))
            SettingsItem(
                icon = painterResource(R.drawable.ic_github),
                title = stringResource(R.string.settings_github_title),
                description = stringResource(R.string.settings_github_description),
                onClick = onGitHubClick,
                actionIcon = rememberVectorPainter(Icons.AutoMirrored.Rounded.KeyboardArrowRight),
                modifier = Modifier.padding(horizontal = 16.dp),
            )
            Spacer(Modifier.height(4.dp))
            SettingsItem(
                icon = painterResource(R.drawable.ic_bug),
                title = stringResource(R.string.settings_test_title),
                description = stringResource(R.string.settings_test_description),
                onClick = onSetTestDataClick,
                actionIcon = rememberVectorPainter(Icons.AutoMirrored.Rounded.KeyboardArrowRight),
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(Modifier.height(4.dp))
            SettingsItem(
                icon = painterResource(R.drawable.ic_info),
                title = stringResource(R.string.settings_app_version),
                description = "${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})",
                shape = RoundedCornerShape(bottomEnd = 24.dp, bottomStart = 24.dp),
                modifier = Modifier.padding(horizontal = 16.dp),
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
    apiUrlChanging: Boolean,
    onChangeApiUrlClick: (apiUrl: String) -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = RectangleShape,
) {
    val focusManager = LocalFocusManager.current
    var text by remember { mutableStateOf(apiUrl) }
    LaunchedEffect(apiUrl) { text = apiUrl }
    SettingsItem(
        icon = icon,
        shape = shape,
        modifier = modifier,
    ) {
        Column {
            AppTextField(
                text = text,
                label = title,
                onTextChange = { value -> text = value },
                modifier = Modifier.fillMaxWidth(),
            )
            FilledTonalButton(
                onClick = {
                    focusManager.clearFocus()
                    onChangeApiUrlClick(text)
                },
                enabled = !apiUrlChanging,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                AnimatedContent(apiUrlChanging) { loading ->
                    if (loading) {
                        CircularProgressIndicator(
                            strokeWidth = 3.dp,
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        Text(stringResource(R.string.settings_server_update))
                    }
                }
            }
        }
    }
}

@Composable
private fun SettingsItem(
    icon: Painter,
    actionIcon: Painter? = null,
    title: String,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    shape: Shape = RectangleShape,
    description: String = "",
) {
    SettingsItem(
        icon = icon,
        shape = shape,
        modifier = modifier,
        onClick = onClick,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
            if (description.isNotBlank()) {
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        if (actionIcon != null) {
            Icon(
                painter = actionIcon,
                contentDescription = null,
                modifier = Modifier.size(SettingActionIconSize)
            )
        }
    }
}

@Composable
private fun SettingsItem(
    icon: Painter,
    modifier: Modifier = Modifier,
    shape: Shape = RectangleShape,
    onClick: (() -> Unit)? = null,
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        modifier = modifier
            .clip(shape)
            .background(MaterialTheme.colorScheme.surfaceContainer, shape)
            .clickable(
                enabled = onClick != null,
                onClick = { onClick?.invoke() }
            )
            .padding(16.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            painter = icon,
            contentDescription = null,
            modifier = Modifier
                .size(SettingIconSize)
                .padding(12.dp)
        )
        content()
    }
}

@Preview("Settings screen")
@Preview("Settings screen (dark)", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewDetailsScreen() {
    ThemedPreview {
        SettingsScaffold(
            state = SettingsState.empty(),
            snackbarHostState = remember { SnackbarHostState() },
            onChangeApiUrlClick = {},
            onGitHubClick = {},
            onSetTestDataClick = {},
            onBackClick = {},
            onChangeThemeClick = {},
        )
    }
}
