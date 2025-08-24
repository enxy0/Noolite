package com.enxy.noolite.presentation.ui.detail

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.enxy.noolite.domain.features.common.Group
import com.enxy.noolite.presentation.ui.common.TopAppBar
import com.enxy.noolite.presentation.utils.FakeUiDataProvider
import com.enxy.noolite.presentation.utils.ThemedPreview

data class DetailsState(
    val onBackClick: () -> Unit = {},
    val onFavoriteClick: (isFavorite: Boolean) -> Unit = {},
    val channelState: ChannelState = ChannelState()
)

@Composable
fun DetailsScreen(
    component: DetailsComponent,
    onBackClick: () -> Unit,
) {
    val state = remember {
        DetailsState(
            onBackClick = onBackClick,
            onFavoriteClick = { isFavorite -> component.onFavoriteClick(isFavorite) },
            channelState = ChannelState { action -> component.onChannelAction(action) }
        )
    }
    val group = component.group
    val isFavorite by component.isFavoriteGroup.collectAsState(false)
    DetailsContent(group = group, state = state, isFavorite = isFavorite)
}

@Composable
private fun DetailsContent(
    group: Group,
    state: DetailsState,
    isFavorite: Boolean
) {
    Scaffold(topBar = { AppBarContent(group.name, state, isFavorite) }) { contentPadding ->
        LazyColumn(
            contentPadding = contentPadding,
            modifier = Modifier.fillMaxSize()
        ) {
            items(group.channels) { channel ->
                Spacer(Modifier.height(16.dp))
                Channel(
                    channel = channel,
                    state = state.channelState,
                    modifier = Modifier.padding(horizontal = 16.dp)
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
    TopAppBar(
        title = title,
        onBackClick = state.onBackClick,
        actions = {
            IconButton(onClick = { state.onFavoriteClick(!isFavorite) }) {
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
            group = FakeUiDataProvider.getFavoriteGroup(),
            state = DetailsState(),
            isFavorite = true
        )
    }
}
