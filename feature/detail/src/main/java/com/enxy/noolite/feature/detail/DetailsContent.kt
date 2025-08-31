package com.enxy.noolite.feature.detail

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.enxy.noolite.core.model.ChannelAction
import com.enxy.noolite.core.ui.FakeUiDataProvider
import com.enxy.noolite.core.ui.compose.Channel
import com.enxy.noolite.core.ui.compose.ThemedPreview
import com.enxy.noolite.core.ui.compose.TopAppBar
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState
import org.orbitmvi.orbit.compose.collectAsState

@Composable
fun DetailsContent(
    component: DetailsComponent,
    modifier: Modifier = Modifier,
) {
    val state by component.collectAsState()
    DetailsContent(
        state = state,
        onBackClick = component::onBackClick,
        onFavoriteClick = component::onFavoriteClick,
        onChannelActionClick = component::onChannelActionClick,
        modifier = modifier,
    )
}

@Composable
private fun DetailsContent(
    state: DetailsState,
    onBackClick: () -> Unit,
    onChannelActionClick: (action: ChannelAction) -> Unit,
    onFavoriteClick: (favorite: Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val hazeState = rememberHazeState()
    Scaffold(
        topBar = {
            DetailsTopAppBar(
                title = state.group.name,
                hazeState = hazeState,
                onBackClick = onBackClick,
                onFavoriteClick = onFavoriteClick,
                isFavorite = state.isFavorite,
            )
        },
        contentWindowInsets = WindowInsets.systemBars.add(WindowInsets.displayCutout),
        modifier = modifier,
    ) { contentPadding ->
        LazyColumn(
            contentPadding = contentPadding,
            modifier = Modifier
                .fillMaxSize()
                .hazeSource(state = hazeState),
        ) {
            items(
                items = state.group.channels,
                key = { channel -> channel.id },
                contentType = { channel -> channel.type },
            ) { channel ->
                Spacer(Modifier.height(16.dp))
                Channel(
                    channel = channel,
                    onChannelActionClick = onChannelActionClick,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
    }
}

@Composable
private fun DetailsTopAppBar(
    title: String,
    hazeState: HazeState,
    onBackClick: () -> Unit,
    onFavoriteClick: (favorite: Boolean) -> Unit,
    isFavorite: Boolean
) {
    TopAppBar(
        title = { Text(title) },
        hazeState = hazeState,
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = null
                )
            }
        },
        actions = {
            IconButton(onClick = { onFavoriteClick(!isFavorite) }) {
                if (isFavorite) {
                    Icon(
                        imageVector = Icons.Outlined.Favorite,
                        tint = MaterialTheme.colorScheme.primary,
                        contentDescription = null
                    )
                } else {
                    Icon(
                        imageVector = Icons.Outlined.FavoriteBorder,
                        contentDescription = null
                    )
                }
            }
        }
    )
}

@Preview("Details screen")
@Preview("Details screen (dark)", uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun PreviewDetailsScreen() {
    ThemedPreview {
        DetailsContent(
            state = DetailsState(
                group = FakeUiDataProvider.getFavoriteGroup(),
            ),
            onBackClick = {},
            onChannelActionClick = {},
            onFavoriteClick = {},
        )
    }
}
