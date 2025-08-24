package com.enxy.noolite.presentation.ui.common

import android.content.res.Configuration
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.enxy.noolite.R
import com.enxy.noolite.presentation.utils.ThemedPreview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
    title: String,
    onBackClick: (() -> Unit)? = null,
    actions: (@Composable RowScope.() -> Unit)? = null,
) {
    TopAppBar(
        navigationIcon = {
            if (onBackClick != null) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = null
                    )
                }
            }
        },
        title = {
            Text(
                text = title,
                modifier = Modifier
            )
        },
        actions = {
            // TODO: Remove Row?
            Row {
                actions?.invoke(this)
            }
        }
    )
}

@Preview("AppBar")
@Preview("AppBar (dark)", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewAppBar() {
    ThemedPreview {
        TopAppBar(
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
        TopAppBar(
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
