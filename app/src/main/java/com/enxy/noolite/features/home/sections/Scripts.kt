package com.enxy.noolite.features.home.sections

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.enxy.noolite.R
import com.enxy.noolite.domain.common.Script
import com.enxy.noolite.utils.FakeUiDataProvider
import com.enxy.noolite.utils.ThemedPreview
import com.enxy.noolite.utils.extensions.pluralResource

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Scripts(
    scripts: List<Script>,
    onScriptClick: (script: Script) -> Unit,
    onScriptRemove: (script: Script) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(8.dp),
) {
    LazyRow(
        horizontalArrangement = horizontalArrangement,
        contentPadding = contentPadding,
        modifier = modifier.fillMaxWidth()
    ) {
        items(
            items = scripts,
            key = { script -> script.id },
        ) { script ->
            Script(
                script = script,
                onScriptClick = { onScriptClick(script) },
                onScriptRemove = { onScriptRemove(script) },
                modifier = Modifier.animateItem(),
            )
        }
    }
}

@Composable
private fun Script(
    script: Script,
    onScriptClick: () -> Unit,
    onScriptRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier then Modifier
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .clickable(onClick = onScriptClick)
    ) {
        Row(
            modifier = Modifier.padding(
                start = 20.dp,
                top = 16.dp,
                bottom = 16.dp,
                end = 4.dp
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = script.name,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                    text = pluralResource(
                        R.plurals.script_actions,
                        script.actions.size,
                        script.actions.size
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            IconButton(onClick = onScriptRemove) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null,
                )
            }
        }
    }
}

@Preview("Scripts")
@Preview("Scripts (dark)", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewScripts() {
    ThemedPreview {
        Scripts(
            scripts = FakeUiDataProvider.getScripts(),
            onScriptClick = {},
            onScriptRemove = {},
            contentPadding = PaddingValues(16.dp),
        )
    }
}
