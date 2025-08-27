package com.enxy.noolite.features.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.automirrored.rounded.List
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.enxy.noolite.R
import com.enxy.noolite.domain.features.actions.model.ChannelAction
import com.enxy.noolite.domain.features.actions.model.GroupAction
import com.enxy.noolite.domain.features.common.Group
import com.enxy.noolite.domain.features.common.Script
import com.enxy.noolite.domain.features.home.model.HomeData
import com.enxy.noolite.features.common.AppTextField
import com.enxy.noolite.features.common.IconTextTooltip
import com.enxy.noolite.features.common.TopAppBar
import com.enxy.noolite.features.detail.Channel
import com.enxy.noolite.features.home.sections.Groups
import com.enxy.noolite.features.home.sections.Scripts
import com.enxy.noolite.utils.FakeUiDataProvider
import com.enxy.noolite.utils.ThemedPreview
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

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
    var apiUrl by rememberSaveable(state.apiUrl) { mutableStateOf(state.apiUrl) }
    Column(
        modifier = modifier
            .fillMaxSize()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(contentPadding)
            .padding(horizontal = 16.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.img_gateway),
            contentDescription = null,
            modifier = Modifier
                .padding(vertical = 24.dp)
                .align(CenterHorizontally)
        )
        Text(
            text = stringResource(R.string.home_onboarding_title),
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.home_empty),
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(Modifier.height(24.dp))
        AppTextField(
            text = apiUrl,
            label = stringResource(R.string.settings_server_title),
            focusManager = focusManager,
            onTextChange = { apiUrl = it },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = {
                focusManager.clearFocus()
                onConnectClick(apiUrl)
            },
            shape = RoundedCornerShape(16.dp),
            elevation = null,
            enabled = !state.isLoading,
            modifier = Modifier
                .height(56.dp)
                .fillMaxWidth()
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    strokeWidth = 3.dp,
                    modifier = Modifier.size(26.dp)
                )
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = stringResource(R.string.home_onboarding_action))
                    Spacer(Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
        Spacer(Modifier.height(16.dp))
    }
}

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
    LazyColumn(
        contentPadding = contentPadding,
        modifier = modifier,
    ) {
        item(
            contentType = HomeContentType.Groups,
            key = HomeContentType.Groups.name,
        ) {
            GroupsSection(
                groups = data.groups,
                onGroupClick = onGroupClick,
                onGroupAction = onGroupAction,
            )
        }
        item(
            contentType = HomeContentType.Scripts,
            key = HomeContentType.Scripts.name,
        ) {
            ScriptsSection(
                scripts = data.scripts,
                onAddScriptClick = onAddScriptClick,
                onScriptClick = onScriptClick,
                onScriptRemove = onScriptRemove,
            )
        }
        item(
            contentType = HomeContentType.FavoriteGroup,
            key = HomeContentType.FavoriteGroup.name,
        ) {
            FavoriteGroupSection(
                group = data.favoriteGroup,
                onChannelActionClick = onChannelActionClick,
            )
        }
        item(
            contentType = HomeContentType.Spacer,
            key = HomeContentType.Spacer.name,
        ) {
            Spacer(Modifier.height(16.dp))
        }
    }
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
                    text = stringResource(R.string.app_name),
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
                    imageVector = Icons.Outlined.Settings,
                    contentDescription = null
                )
            }
        },
        modifier = modifier,
    )
}

@Composable
private fun GroupsSection(
    groups: List<Group>,
    onGroupClick: (group: Group) -> Unit,
    onGroupAction: (action: GroupAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        Spacer(Modifier.height(16.dp))
        SectionTitle(
            text = stringResource(R.string.home_groups),
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        if (groups.isNotEmpty()) {
            Groups(
                groups = groups,
                onGroupClick = onGroupClick,
                onGroupAction = onGroupAction,
                contentPadding = PaddingValues(top = 8.dp, start = 16.dp, end = 16.dp)
            )
        } else {
            IconTextTooltip(
                painter = rememberVectorPainter(Icons.AutoMirrored.Rounded.List),
                text = stringResource(R.string.home_groups_empty),
                modifier = Modifier.padding(16.dp)
            )
        }
        Spacer(Modifier.height(16.dp))
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun ScriptsSection(
    scripts: List<Script>,
    onAddScriptClick: () -> Unit,
    onScriptClick: (script: Script) -> Unit,
    onScriptRemove: (script: Script) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            SectionTitle(
                text = stringResource(R.string.home_scripts),
                modifier = Modifier.weight(1f),
            )
            FilledTonalIconButton(
                onClick = onAddScriptClick,
                modifier = Modifier.size(30.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
        AnimatedContent(scripts.isNotEmpty()) { hasScripts ->
            if (hasScripts) {
                Scripts(
                    scripts = scripts,
                    onScriptClick = onScriptClick,
                    onScriptRemove = onScriptRemove,
                    contentPadding = PaddingValues(top = 8.dp, start = 16.dp, end = 16.dp),
                )
            } else {
                IconTextTooltip(
                    painter = painterResource(R.drawable.ic_script),
                    text = stringResource(R.string.home_scripts_empty),
                    modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp),
                )
            }
        }
        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun FavoriteGroupSection(
    group: Group?,
    onChannelActionClick: (action: ChannelAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        Spacer(Modifier.height(16.dp))
        SectionTitle(
            text = stringResource(R.string.home_favorite),
            modifier = Modifier.padding(horizontal = 16.dp),
        )
        if (group != null) {
            Spacer(Modifier.height(8.dp))
            for (channel in group.channels) {
                Channel(
                    channel = channel,
                    onChannelActionClick = onChannelActionClick,
                    modifier = Modifier.padding(vertical = 4.dp, horizontal = 16.dp)
                )
            }
        } else {
            IconTextTooltip(
                painter = rememberVectorPainter(Icons.Outlined.FavoriteBorder),
                text = stringResource(R.string.home_favorite_empty),
                modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp),
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
        color = MaterialTheme.colorScheme.onBackground,
        modifier = modifier,
    )
}

private enum class HomeContentType {
    Groups,
    Scripts,
    FavoriteGroup,
    Spacer,
}

@Preview("Initial Home Screen")
@Composable
private fun PreviewContentHomeScreen() {
    ThemedPreview {
        HomeScaffold(
            state = HomeState.Content(
                data = FakeUiDataProvider.getHomeData(),
            ),
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

@Preview("Content Home Screen")
@Composable
private fun PreviewInitialHomeScreen() {
    ThemedPreview {
        HomeScaffold(
            state = HomeState.Empty(
                apiUrl = FakeUiDataProvider.getSettings().apiUrl,
                isLoading = false,
            ),
            snackbarHostState = remember { SnackbarHostState() },
            onConnectClick = {},
            onSettingsClick = {},
            onGroupAction = {},
            onGroupClick = {},
            onAddScriptClick = {},
            onScriptClick = {},
            onScriptRemove = {},
            onChannelActionClick = {},
        )
    }
}
