package com.enxy.noolite.feature.detail

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import com.enxy.noolite.core.ui.NooliteIcons
import com.enxy.noolite.core.ui.compose.Channel
import com.enxy.noolite.core.ui.compose.ThemedPreview
import com.enxy.noolite.core.ui.compose.TopAppBar
import com.enxy.noolite.core.ui.extensions.plus
import com.enxy.noolite.core.ui.icons.ArrowBack
import com.enxy.noolite.core.ui.icons.Favorite
import com.enxy.noolite.core.ui.icons.FavoriteBorder
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
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = contentPadding + PaddingValues(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .hazeSource(state = hazeState),
        ) {
            itemsIndexed(
                items = state.group.channels,
                key = { _, channel -> channel.id },
                contentType = { _, channel -> channel.type },
            ) { index, channel ->
                Channel(
                    channel = channel,
                    onChannelActionClick = onChannelActionClick,
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
                    imageVector = NooliteIcons.ArrowBack,
                    contentDescription = null
                )
            }
        },
        actions = {
            IconButton(onClick = { onFavoriteClick(!isFavorite) }) {
                if (isFavorite) {
                    Icon(
                        imageVector = NooliteIcons.Favorite,
                        tint = MaterialTheme.colorScheme.primary,
                        contentDescription = null
                    )
                } else {
                    Icon(
                        imageVector = NooliteIcons.FavoriteBorder,
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
