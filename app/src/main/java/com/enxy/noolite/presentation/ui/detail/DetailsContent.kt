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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.enxy.noolite.domain.features.actions.model.ChannelAction
import com.enxy.noolite.domain.features.common.Group
import com.enxy.noolite.presentation.ui.common.TopAppBar
import com.enxy.noolite.presentation.utils.FakeUiDataProvider
import com.enxy.noolite.ui.theme.NooliteTheme

@Composable
fun DetailsContent(
    component: DetailsComponent,
    modifier: Modifier = Modifier,
) {
    val isFavorite by component.isFavoriteGroup.collectAsState(false)
    DetailsContent(
        group = component.group,
        onBackClick = component::onBackClick,
        onFavoriteClick = component::onFavoriteClick,
        onChannelActionClick = component::onChannelActionClick,
        isFavorite = isFavorite,
        modifier = modifier,
    )
}

@Composable
private fun DetailsContent(
    group: Group,
    isFavorite: Boolean,
    onBackClick: () -> Unit,
    onChannelActionClick: (action: ChannelAction) -> Unit,
    onFavoriteClick: (favorite: Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            AppBarContent(
                title = group.name,
                onBackClick = onBackClick,
                onFavoriteClick = onFavoriteClick,
                isFavorite = isFavorite,
            )
        },
        modifier = modifier,
    ) { contentPadding ->
        LazyColumn(
            contentPadding = contentPadding,
            modifier = Modifier.fillMaxSize(),
        ) {
            items(
                items = group.channels,
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
private fun AppBarContent(
    title: String,
    onBackClick: () -> Unit,
    onFavoriteClick: (favorite: Boolean) -> Unit,
    isFavorite: Boolean
) {
    TopAppBar(
        title = title,
        onBackClick = onBackClick,
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
    NooliteTheme {
        DetailsContent(
            group = FakeUiDataProvider.getFavoriteGroup(),
            onBackClick = {},
            onChannelActionClick = {},
            onFavoriteClick = {},
            isFavorite = true
        )
    }
}
