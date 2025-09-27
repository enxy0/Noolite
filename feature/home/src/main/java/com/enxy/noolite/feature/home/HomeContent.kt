package com.enxy.noolite.feature.home

import androidx.annotation.StringRes
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.enxy.noolite.core.model.ChannelAction
import com.enxy.noolite.core.model.Group
import com.enxy.noolite.core.model.GroupAction
import com.enxy.noolite.core.model.Script
import com.enxy.noolite.core.ui.FakeUiDataProvider
import com.enxy.noolite.core.ui.NooliteIcons
import com.enxy.noolite.core.ui.compose.Channel
import com.enxy.noolite.core.ui.compose.IconTextTooltip
import com.enxy.noolite.core.ui.compose.ThemedPreview
import com.enxy.noolite.core.ui.compose.TopAppBar
import com.enxy.noolite.core.ui.extensions.plus
import com.enxy.noolite.core.ui.icons.Add
import com.enxy.noolite.core.ui.icons.ArrowForward
import com.enxy.noolite.core.ui.icons.Description
import com.enxy.noolite.core.ui.icons.List
import com.enxy.noolite.core.ui.icons.Router
import com.enxy.noolite.core.ui.icons.Settings
import com.enxy.noolite.domain.home.model.HomeData
import com.enxy.noolite.feature.home.sections.GroupCardWidth
import com.enxy.noolite.feature.home.sections.HorizontalGroups
import com.enxy.noolite.feature.home.sections.Scripts
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import com.enxy.noolite.core.ui.R as CoreUiR

@Composable
fun HomeContent(
    component: HomeComponent,
    modifier: Modifier = Modifier,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    component.collectSideEffect { effect ->
        when (effect) {
            is HomeSideEffect.Message -> {
                snackbarHostState.showSnackbar(effect.message)
            }
        }
    }
    val state by component.collectAsState()
    HomeScaffold(
        state = state,
        snackbarHostState = snackbarHostState,
        onConnectClick = component::onConnectClick,
        onSettingsClick = component::onSettingsClick,
        onGroupClick = component::onGroupClick,
        onGroupAction = component::onGroupAction,
        onAddScriptClick = component::onAddScriptClick,
        onScriptClick = component::onScriptClick,
        onScriptRemove = component::onScriptRemove,
        onChannelActionClick = component::onChannelActionClick,
        modifier = modifier,
    )
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun HomeScaffold(
    state: HomeState,
    snackbarHostState: SnackbarHostState,
    onConnectClick: (apiUrl: String) -> Unit,
    onSettingsClick: () -> Unit,
    onGroupClick: (group: Group) -> Unit,
    onGroupAction: (action: GroupAction) -> Unit,
    onAddScriptClick: () -> Unit,
    onScriptClick: (script: Script) -> Unit,
    onScriptRemove: (script: Script) -> Unit,
    onChannelActionClick: (action: ChannelAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val hazeState = rememberHazeState()
    Scaffold(
        topBar = {
            HomeTopAppBar(
                hazeState = hazeState,
                onSettingsClick = onSettingsClick,
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        contentWindowInsets = WindowInsets.systemBars.add(WindowInsets.displayCutout),
        modifier = modifier,
    ) { contentPadding ->
        val transition = updateTransition(state)
        transition.Crossfade(
            contentKey = { it.toContentKey() },
            modifier = Modifier.hazeSource(state = hazeState),
        ) { state ->
            when (state) {
                is HomeState.Initial -> Unit
                is HomeState.Empty -> {
                    HomeEmptyState(
                        state = state,
                        onConnectClick = onConnectClick,
                        contentPadding = contentPadding,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                is HomeState.Content -> {
                    HomeContentState(
                        data = state.data,
                        onGroupClick = onGroupClick,
                        onGroupAction = onGroupAction,
                        contentPadding = contentPadding,
                        onAddScriptClick = onAddScriptClick,
                        onScriptClick = onScriptClick,
                        onScriptRemove = onScriptRemove,
                        onChannelActionClick = onChannelActionClick,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

@Composable
private fun HomeEmptyState(
    state: HomeState.Empty,
    contentPadding: PaddingValues,
    onConnectClick: (apiUrl: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current
    val apiUrlFieldState = rememberTextFieldState(state.apiUrl)
    Column(
        modifier = modifier
            .fillMaxSize()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(contentPadding)
            .padding(horizontal = 16.dp)
    ) {
        Spacer(Modifier.height(24.dp))
        Icon(
            imageVector = NooliteIcons.Router,
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
                .background(MaterialTheme.colorScheme.surfaceContainer, CircleShape)
                .padding(16.dp)
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.home_onboarding_title),
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(Modifier.height(8.dp))
        BulletText(text = stringResource(R.string.home_setup_description_1))
        Spacer(Modifier.height(4.dp))
        BulletText(text = stringResource(R.string.home_setup_description_2))
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            state = apiUrlFieldState,
            label = { Text(stringResource(R.string.home_api_url_title)) },
            keyboardOptions = KeyboardOptions(
                autoCorrectEnabled = false,
            ),
            onKeyboardAction = {
                focusManager.clearFocus()
                onConnectClick(apiUrlFieldState.text.toString())
                ImeAction.Done
            },
            enabled = !state.isLoading,
            shape = RoundedCornerShape(16.dp),
            lineLimits = TextFieldLineLimits.SingleLine,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        Button(
            onClick = {
                focusManager.clearFocus()
                onConnectClick(apiUrlFieldState.text.toString())
            },
            enabled = !state.isLoading,
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(text = stringResource(R.string.home_onboarding_action))
            Spacer(Modifier.width(4.dp))
            if (state.isLoading) {
                CircularProgressIndicator(
                    strokeWidth = 3.dp,
                    modifier = Modifier.size(20.dp)
                )
            } else {
                Icon(
                    imageVector = NooliteIcons.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun BulletText(
    text: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Box(
            modifier = Modifier
                .padding(top = 6.dp)
                .size(8.dp)
                .background(color = MaterialTheme.colorScheme.primary, CircleShape)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

private const val KEY_GROUPS = "groups"
private const val KEY_SCRIPTS = "scripts"
private const val KEY_FAVORITE = "favorite"

@Composable
private fun HomeContentState(
    data: HomeData,
    onGroupClick: (group: Group) -> Unit,
    onGroupAction: (action: GroupAction) -> Unit,
    onAddScriptClick: () -> Unit,
    onScriptClick: (script: Script) -> Unit,
    onScriptRemove: (script: Script) -> Unit,
    onChannelActionClick: (action: ChannelAction) -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        contentPadding = contentPadding + PaddingValues(vertical = 16.dp),
        columns = GridCells.Adaptive(GroupCardWidth),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier,
    ) {
        title(
            text = {
                SectionTitle(
                    text = stringResource(R.string.home_groups),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    modifier = Modifier.animateItem()
                )
            },
            key = KEY_GROUPS,
        )
        if (data.groups.isNotEmpty()) {
            groups(
                groups = data.groups,
                contentPadding = PaddingValues(horizontal = 16.dp),
                onGroupClick = onGroupClick,
                onGroupAction = onGroupAction
            )
        } else {
            iconTooltip(
                icon = NooliteIcons.List,
                textResId = R.string.home_groups_empty,
                contentPadding = PaddingValues(horizontal = 16.dp),
                key = KEY_GROUPS,
            )
        }
        title(
            text = {
                ScriptSectionTitle(
                    onAddScriptClick = onAddScriptClick,
                    contentPadding = PaddingValues(start = 16.dp, top = 24.dp, end = 16.dp),
                    modifier = Modifier.animateItem(),
                )
            },
            key = KEY_SCRIPTS,
        )
        if (data.scripts.isNotEmpty()) {
            scripts(
                scripts = data.scripts,
                contentPadding = PaddingValues(horizontal = 16.dp),
                onScriptClick = onScriptClick,
                onScriptRemove = onScriptRemove,
            )
        } else {
            iconTooltip(
                icon = NooliteIcons.Description,
                textResId = R.string.home_scripts_empty,
                contentPadding = PaddingValues(horizontal = 16.dp),
                key = KEY_SCRIPTS,
            )
        }
        title(
            text = {
                SectionTitle(
                    text = stringResource(R.string.home_favorite),
                    contentPadding = PaddingValues(start = 16.dp, top = 24.dp, end = 16.dp),
                    modifier = Modifier.animateItem(),
                )
            },
            key = KEY_FAVORITE,
        )
        val favoriteGroup = data.favoriteGroup
        if (favoriteGroup != null) {
            favoriteGroup(
                group = favoriteGroup,
                contentPadding = PaddingValues(horizontal = 16.dp),
                onChannelActionClick = onChannelActionClick,
            )
        } else {
            iconTooltip(
                icon = NooliteIcons.List,
                textResId = R.string.home_groups_empty,
                contentPadding = PaddingValues(horizontal = 16.dp),
                key = KEY_FAVORITE,
            )
        }
    }
}

@Composable
private fun ScriptSectionTitle(
    onAddScriptClick: () -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.padding(contentPadding),
    ) {
        SectionTitle(
            text = stringResource(R.string.home_scripts),
            contentPadding = PaddingValues(),
            modifier = Modifier.weight(1f),
        )
        AddCircleButton(onClick = onAddScriptClick)
    }
}

private fun LazyGridScope.scripts(
    scripts: List<Script>,
    contentPadding: PaddingValues,
    onScriptClick: (script: Script) -> Unit,
    onScriptRemove: (script: Script) -> Unit,
) {
    item(
        contentType = HomeContentType.Scripts,
        key = HomeContentType.Scripts.name,
        span = { GridItemSpan(maxLineSpan) }
    ) {
        Scripts(
            scripts = scripts,
            onScriptClick = onScriptClick,
            onScriptRemove = onScriptRemove,
            contentPadding = contentPadding,
            modifier = Modifier.animateItem()
        )
    }
}

private fun LazyGridScope.title(
    text: @Composable LazyGridItemScope.() -> Unit,
    key: String,
) {
    item(
        contentType = HomeContentType.Title,
        key = "${HomeContentType.Title.name}-$key",
        span = { GridItemSpan(maxLineSpan) },
    ) {
        text()
    }
}

private fun LazyGridScope.iconTooltip(
    icon: ImageVector,
    @StringRes textResId: Int,
    key: String,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
) {
    item(
        contentType = HomeContentType.IconTooltip,
        key = "${HomeContentType.IconTooltip.name}-$key",
        span = { GridItemSpan(maxLineSpan) },
    ) {
        IconTextTooltip(
            painter = rememberVectorPainter(icon),
            text = stringResource(textResId),
            modifier = modifier
                .padding(contentPadding)
                .animateItem(),
        )
    }
}

@Composable
private fun SectionTitle(
    text: String,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues,
) {
    Text(
        text = text,
        color = MaterialTheme.colorScheme.onBackground,
        style = MaterialTheme.typography.titleLarge,
        modifier = modifier.padding(contentPadding),
    )
}

@Composable
private fun HomeTopAppBar(
    hazeState: HazeState,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        title = {
            Column(verticalArrangement = Arrangement.spacedBy(0.dp)) {
                Text(
                    text = stringResource(CoreUiR.string.app_name),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = stringResource(R.string.home_second_row_title),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        },
        hazeState = hazeState,
        actions = {
            IconButton(onClick = onSettingsClick) {
                Icon(
                    imageVector = NooliteIcons.Settings,
                    contentDescription = null
                )
            }
        },
        modifier = modifier,
    )
}

private fun LazyGridScope.groups(
    groups: List<Group>,
    contentPadding: PaddingValues,
    onGroupClick: (group: Group) -> Unit,
    onGroupAction: (action: GroupAction) -> Unit,
    modifier: Modifier = Modifier
) {
    item(
        contentType = HomeContentType.Groups,
        key = HomeContentType.Groups.name,
        span = { GridItemSpan(maxLineSpan) },
    ) {
        HorizontalGroups(
            groups = groups,
            onGroupClick = onGroupClick,
            onGroupAction = onGroupAction,
            contentPadding = contentPadding,
            modifier = modifier.animateItem()
        )
    }
}

@Composable
private fun AddCircleButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    FilledTonalIconButton(
        onClick = onClick,
        modifier = modifier.size(30.dp)
    ) {
        Icon(
            imageVector = NooliteIcons.Add,
            contentDescription = null,
            modifier = Modifier.size(20.dp)
        )
    }
}

private fun LazyGridScope.favoriteGroup(
    group: Group,
    onChannelActionClick: (action: ChannelAction) -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
) {
    item(
        contentType = HomeContentType.FavoriteGroup,
        key = HomeContentType.FavoriteGroup.name,
        span = { GridItemSpan(maxLineSpan) },
    ) {
        Column(
            modifier = modifier
                .padding(contentPadding)
                .animateItem(),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            for (channel in group.channels) {
                Channel(
                    channel = channel,
                    onChannelActionClick = onChannelActionClick,
                )
            }
        }
    }
}

private enum class HomeContentType {
    Title,
    IconTooltip,
    Groups,
    Scripts,
    FavoriteGroup,
}

@Preview("Initial Home Screen")
@Composable
private fun PreviewHomeScreen(
    @PreviewParameter(PreviewHomeStateProvider::class)
    state: HomeState
) {
    ThemedPreview {
        HomeScaffold(
            state = state,
            snackbarHostState = remember { SnackbarHostState() },
            onConnectClick = {},
            onSettingsClick = {},
            onGroupAction = {},
            onGroupClick = {},
            onAddScriptClick = {},
            onScriptClick = {},
            onScriptRemove = {},
            onChannelActionClick = {}
        )
    }
}

private class PreviewHomeStateProvider : PreviewParameterProvider<HomeState> {
    override val values: Sequence<HomeState> = sequenceOf(
        HomeState.Empty(apiUrl = "192.168.1.10:8080", isLoading = false),
        HomeState.Content(getPreviewHomeData()),
    )
}

private fun getPreviewHomeData(
    groups: List<Group> = FakeUiDataProvider.getGroups(),
    scripts: List<Script> = FakeUiDataProvider.getScripts(),
    favoriteGroup: Group? = FakeUiDataProvider.getFavoriteGroup(),
): HomeData = HomeData(
    groups = groups,
    scripts = scripts,
    favoriteGroup = favoriteGroup,
)
