package com.enxy.noolite.presentation.ui.common

import android.content.res.Configuration
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.enxy.noolite.R
import com.enxy.noolite.presentation.ui.theme.AppTheme
import com.enxy.noolite.presentation.utils.ThemedPreview
import com.enxy.noolite.presentation.utils.extensions.bottomDivider

@Composable
fun AppBar(
    title: String,
    onBackClick: (() -> Unit)? = null,
    actions: (@Composable RowScope.() -> Unit)? = null,
) {
    TopAppBar(
        backgroundColor = Color.Transparent,
        elevation = 0.dp,
        modifier = Modifier
            .height(56.dp)
            .bottomDivider(AppTheme.colors.surface)
    ) {
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.high) {
            if (onBackClick != null) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = null
                    )
                }
            }
            Text(
                text = title,
                style = AppTheme.typography.h6,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = AppTheme.dimensions.small)
            )
            if (actions != null) {
                actions()
            }
        }
    }
}

@Preview("AppBar")
@Preview("AppBar (dark)", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewAppBar() {
    ThemedPreview {
        AppBar(
            title = stringResource(R.string.settings_title),
            onBackClick = {}
        )
    }
}

@Preview("AppBar with actions")
@Preview("AppBar with actions (dark)", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewActionsAppBar() {
    ThemedPreview {
        AppBar(
            title = stringResource(R.string.app_name),
            actions = {
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Outlined.Search,
                        contentDescription = null
                    )
                }
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Outlined.Settings,
                        contentDescription = null
                    )
                }
            }
        )
    }
}
