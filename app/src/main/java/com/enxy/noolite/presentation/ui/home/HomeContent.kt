package com.enxy.noolite.presentation.ui.home

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.enxy.noolite.R
import com.enxy.noolite.domain.features.common.Group
import com.enxy.noolite.domain.features.common.Script
import com.enxy.noolite.domain.features.home.model.HomeData
import com.enxy.noolite.presentation.ui.common.AppTextField
import com.enxy.noolite.presentation.ui.common.IconPlaceholder
import com.enxy.noolite.presentation.ui.common.TopAppBar
import com.enxy.noolite.presentation.ui.detail.Channel
import com.enxy.noolite.presentation.ui.detail.ChannelState
import com.enxy.noolite.presentation.ui.home.model.HomeAction
import com.enxy.noolite.presentation.ui.home.model.HomeUiState
import com.enxy.noolite.presentation.ui.home.sections.GroupState
import com.enxy.noolite.presentation.ui.home.sections.Groups
import com.enxy.noolite.presentation.ui.home.sections.ScriptState
import com.enxy.noolite.presentation.ui.home.sections.Scripts
import com.enxy.noolite.presentation.utils.FakeUiDataProvider
import com.enxy.noolite.presentation.utils.ThemedPreview

data class HomScaffoldState(
    val groupState: GroupState = GroupState(),
    val scriptState: ScriptState = ScriptState(),
    val channelState: ChannelState = ChannelState(),
    val navigateToSettings: () -> Unit = {},
    val navigateToScript: () -> Unit = {},
    val onApiUrlChange: (apiUrl: String) -> Unit = {}
)

@Composable
fun HomeContent(
    navigateToSettings: () -> Unit = {},
    navigateToGroup: (group: Group) -> Unit = {},
    navigateToScript: () -> Unit = {},
    component: HomeComponent,
) {
    val scaffoldState = rememberHomeScaffoldState(
        component,
        navigateToSettings = navigateToSettings,
        navigateToGroup = navigateToGroup,
        navigateToScript = navigateToScript
    )
    val uiState by component.uiState.collectAsState()
    val action by component.actionsFlow.collectAsState(HomeAction.None)
    HomeScaffold(uiState, scaffoldState, action)
}

@Composable
private fun rememberHomeScaffoldState(
    viewModel: HomeComponent,
    navigateToSettings: () -> Unit,
    navigateToGroup: (group: Group) -> Unit,
    navigateToScript: () -> Unit,
) = remember {
    HomScaffoldState(
        groupState = GroupState(navigateToGroup, viewModel::onGroupAction),
        scriptState = ScriptState(
            viewModel::onScriptClick,
            viewModel::onScriptRemove
        ),
        channelState = ChannelState(viewModel::onChannelAction),
        navigateToSettings = navigateToSettings,
        navigateToScript = navigateToScript,
        onApiUrlChange = viewModel::onApiUrlChange
    )
}

@Composable
private fun HomeScaffold(
    uiState: HomeUiState,
    scaffoldState: HomScaffoldState,
    action: HomeAction
) {
    Scaffold(
        topBar = { AppBarContent(scaffoldState.navigateToSettings) },
        modifier = Modifier
            .fillMaxSize()
            .scrollable(rememberScrollState(), Orientation.Vertical)
    ) { contentPadding ->
        // TODO: Use contentPadding
        when (uiState) {
            HomeUiState.Initial -> Unit
            is HomeUiState.Empty -> HomeEmptyState(
                apiUrl = uiState.apiUrl,
                isLoading = uiState.isLoading,
                onApiUrlChange = scaffoldState.onApiUrlChange
            )

            is HomeUiState.Content -> HomeContentState(
                state = scaffoldState,
                data = uiState.data
            )
        }
        when (action) {
            HomeAction.None -> Unit
            is HomeAction.ShowSnackbar -> HomeSnackbar(action)
        }
    }
}

@Composable
fun HomeSnackbar(action: HomeAction.ShowSnackbar) {
    val snackbarHostState by remember { mutableStateOf(SnackbarHostState()) }
    LaunchedEffect(action) {
        snackbarHostState.showSnackbar(
            message = action.message,
            duration = SnackbarDuration.Short
        )
    }
    Box(modifier = Modifier.fillMaxSize()) {
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun HomeEmptyState(
    apiUrl: String,
    isLoading: Boolean,
    onApiUrlChange: (apiUrl: String) -> Unit
) {
    val focusManager = LocalFocusManager.current
    var text by remember { mutableStateOf(apiUrl) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.img_gateway),
            contentDescription = null,
            modifier = Modifier
                .align(CenterHorizontally)
                .padding(vertical = 24.dp)
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
            text = text,
            label = stringResource(R.string.settings_server_title),
            focusManager = focusManager,
            onTextChange = { value -> text = value },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = {
                focusManager.clearFocus()
                onApiUrlChange(text)
            },
            shape = RoundedCornerShape(16.dp),
            elevation = null,
            enabled = !isLoading,
            modifier = Modifier
                .height(56.dp)
                .fillMaxWidth()
        ) {
            AnimatedVisibility(isLoading) {
                CircularProgressIndicator(
                    strokeWidth = 3.dp,
                    color = Color.White,
                    modifier = Modifier.size(26.dp)
                )
            }
            AnimatedVisibility(!isLoading) {
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
    state: HomScaffoldState,
    data: HomeData
) {
    LazyColumn {
        item { GroupsContent(data.groups, state.groupState) }
        item { ScriptsContent(data.scripts, state.scriptState, state.navigateToScript) }
        item { FavoriteContent(data.favoriteGroup, state.channelState) }
    }
}

@Composable
private fun AppBarContent(onNavigateSettings: () -> Unit) {
    TopAppBar(title = stringResource(R.string.app_name)) {
        IconButton(onClick = onNavigateSettings) {
            Icon(
                imageVector = Icons.Outlined.Settings,
                contentDescription = null
            )
        }
    }
}

@Composable
private fun GroupsContent(groups: List<Group>, state: GroupState) {
    Spacer(Modifier.height(16.dp))
    Text(
        text = stringResource(R.string.home_groups),
        style = MaterialTheme.typography.headlineSmall,
        modifier = Modifier.padding(horizontal = 16.dp)
    )
    if (groups.isNotEmpty()) {
        Groups(groups = groups, state = state)
    } else {
        IconPlaceholder(
            imageVector = Icons.Outlined.List,
            text = stringResource(R.string.home_groups_empty),
            modifier = Modifier.padding(16.dp)
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun ScriptsContent(
    scripts: List<Script>,
    state: ScriptState,
    navigateToScript: () -> Unit
) {
    Spacer(Modifier.height(16.dp))
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.home_scripts),
            style = MaterialTheme.typography.headlineSmall,
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
                .clickable { navigateToScript() }
        )
    }
    AnimatedContent(scripts.isNotEmpty()) { hasScripts ->
        if (hasScripts) {
            Scripts(scripts = scripts, state = state, modifier = Modifier.fillMaxWidth())
        } else {
            IconPlaceholder(
                painter = painterResource(R.drawable.ic_script),
                text = stringResource(R.string.home_scripts_empty),
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
private fun FavoriteContent(favoriteGroup: Group?, state: ChannelState) {
    Spacer(Modifier.height(16.dp))
    Text(
        text = stringResource(R.string.home_favorite),
        style = MaterialTheme.typography.headlineSmall,
        modifier = Modifier.padding(horizontal = 16.dp)
    )
    if (favoriteGroup != null) {
        Spacer(Modifier.height(4.dp))
        for (channel in favoriteGroup.channels) {
            Channel(
                channel = channel,
                state = state,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )
        }
    } else {
        IconPlaceholder(
            imageVector = Icons.Outlined.FavoriteBorder,
            text = stringResource(R.string.home_favorite_empty),
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview("Initial Home Screen")
@Preview("Initial Home Screen (dark)", uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun PreviewContentHomeScreen() {
    ThemedPreview {
        HomeScaffold(
            scaffoldState = HomScaffoldState(),
            uiState = HomeUiState.Content(FakeUiDataProvider.getHomeData()),
            action = HomeAction.None
        )
    }
}

@Preview("Content Home Screen")
@Preview("Content Home Screen (dark)", uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun PreviewInitialHomeScreen() {
    ThemedPreview {
        HomeScaffold(
            scaffoldState = HomScaffoldState(),
            uiState = HomeUiState.Empty(FakeUiDataProvider.getSettings().apiUrl, false),
            action = HomeAction.None
        )
    }
}
