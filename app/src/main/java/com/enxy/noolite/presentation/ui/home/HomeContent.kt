package com.enxy.noolite.presentation.ui.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.enxy.noolite.R
import com.enxy.noolite.domain.features.actions.model.ChannelAction
import com.enxy.noolite.domain.features.actions.model.GroupAction
import com.enxy.noolite.domain.features.common.Group
import com.enxy.noolite.domain.features.common.Script
import com.enxy.noolite.domain.features.home.model.HomeData
import com.enxy.noolite.presentation.ui.common.AppTextField
import com.enxy.noolite.presentation.ui.common.IconTextTooltip
import com.enxy.noolite.presentation.ui.detail.Channel
import com.enxy.noolite.presentation.ui.home.sections.Groups
import com.enxy.noolite.presentation.ui.home.sections.Scripts
import com.enxy.noolite.presentation.utils.FakeUiDataProvider
import com.enxy.noolite.presentation.utils.ThemedPreview
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
    Scaffold(
        topBar = { HomeTopAppBar(onSettingsClick) },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = modifier,
        contentWindowInsets = WindowInsets.systemBars.add(WindowInsets.displayCutout)
    ) { contentPadding ->
        val transition = updateTransition(state)
        transition.Crossfade(
            contentKey = { it.toContentKey() },
        ) { state ->
            when (state) {
                is HomeState.Initial -> Unit
                is HomeState.Empty -> {
                    HomeEmptyState(
                        state = state,
                        onConnectClick = onConnectClick,
                        contentPadding = contentPadding,
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
                    color = Color.White,
                    modifier = Modifier.size(26.dp)
                )
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(stringResource(R.string.home_onboarding_action))
                    Spacer(Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Outlined.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
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
) {
    LazyColumn(
        contentPadding = contentPadding,
    ) {
        item(
            contentType = ContentType.Groups,
            key = ContentType.Groups.name,
        ) {
            GroupsSection(
                groups = data.groups,
                onGroupClick = onGroupClick,
                onGroupAction = onGroupAction,
            )
        }
        item(
            contentType = ContentType.Scripts,
            key = ContentType.Scripts.name,
        ) {
            ScriptsSection(
                scripts = data.scripts,
                onAddScriptClick = onAddScriptClick,
                onScriptClick = onScriptClick,
                onScriptRemove = onScriptRemove,
            )
        }
        item(
            contentType = ContentType.FavoriteGroup,
            key = ContentType.FavoriteGroup.name,
        ) {
            FavoriteGroupSection(
                group = data.favoriteGroup,
                onChannelActionClick = onChannelActionClick,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeTopAppBar(
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        title = { Text(stringResource(R.string.app_name)) },
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
        Text(
            text = stringResource(R.string.home_groups),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        if (groups.isNotEmpty()) {
            Groups(
                groups = groups,
                onGroupClick = onGroupClick,
                onGroupAction = onGroupAction,
            )
        } else {
            IconTextTooltip(
                painter = rememberVectorPainter(Icons.Outlined.List),
                text = stringResource(R.string.home_groups_empty),
                modifier = Modifier.padding(16.dp)
            )
        }
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
        Spacer(Modifier.height(16.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.home_scripts),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Text(
                text = "+",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(end = 4.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .size(28.dp)
                    .align(Alignment.CenterVertically)
                    .clickable(onClick = onAddScriptClick)
            )
        }
        AnimatedContent(scripts.isNotEmpty()) { hasScripts ->
            if (hasScripts) {
                Scripts(
                    scripts = scripts,
                    onScriptClick = onScriptClick,
                    onScriptRemove = onScriptRemove,
                    modifier = Modifier.fillMaxWidth(),
                )
            } else {
                IconTextTooltip(
                    painter = painterResource(R.drawable.ic_script),
                    text = stringResource(R.string.home_scripts_empty),
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
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
        Text(
            text = stringResource(R.string.home_favorite),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        if (group != null) {
            Spacer(Modifier.height(4.dp))
            for (channel in group.channels) {
                Channel(
                    channel = channel,
                    onChannelActionClick = onChannelActionClick,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )
            }
        } else {
            IconTextTooltip(
                painter = rememberVectorPainter(Icons.Outlined.FavoriteBorder),
                text = stringResource(R.string.home_favorite_empty),
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

private enum class ContentType {
    Groups,
    Scripts,
    FavoriteGroup,
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
