package com.enxy.noolite.presentation.ui.detail

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.enxy.domain.features.common.Group
import com.enxy.noolite.presentation.ui.common.AppBar
import com.enxy.noolite.presentation.ui.theme.Amber500
import com.enxy.noolite.presentation.ui.theme.AppTheme
import com.enxy.noolite.presentation.utils.FakeUiDataProvider
import com.enxy.noolite.presentation.utils.ThemedPreview
import org.koin.androidx.compose.get

data class DetailsState(
    val onBackClick: () -> Unit = {},
    val onFavoriteClick: (isFavorite: Boolean) -> Unit = {},
    val channelState: ChannelState = ChannelState()
)

@Composable
fun DetailsScreen(
    onBackClick: () -> Unit,
    viewModel: DetailsViewModel = get()
) {
    val state = remember {
        DetailsState(
            onBackClick = onBackClick,
            onFavoriteClick = { isFavorite -> viewModel.onFavoriteClick(isFavorite) },
            channelState = ChannelState { action -> viewModel.onChannelAction(action) }
        )
    }
    val group = viewModel.group
    val isFavorite by viewModel.isFavoriteFlow.collectAsState(false)
    DetailsContent(group = group, state = state, isFavorite = isFavorite)
}

@Composable
private fun DetailsContent(
    group: Group,
    state: DetailsState,
    isFavorite: Boolean
) {
    Scaffold(topBar = { AppBarContent(group.name, state, isFavorite) }) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(group.channels) { channel ->
                Spacer(Modifier.height(AppTheme.dimensions.normal))
                Channel(
                    channel = channel,
                    state = state.channelState,
                    modifier = Modifier.padding(horizontal = AppTheme.dimensions.normal)
                )
            }
        }
    }
}

@Composable
private fun AppBarContent(
    title: String,
    state: DetailsState,
    isFavorite: Boolean
) {
    AppBar(
        title = title,
        onBackClick = state.onBackClick,
        actions = {
            IconButton(onClick = { state.onFavoriteClick(!isFavorite) }) {
                if (isFavorite) {
                    Icon(
                        imageVector = Icons.Outlined.Favorite,
                        tint = Amber500,
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
            group = FakeUiDataProvider.getFavoriteGroup(),
            state = DetailsState(),
            isFavorite = true
        )
    }
}
