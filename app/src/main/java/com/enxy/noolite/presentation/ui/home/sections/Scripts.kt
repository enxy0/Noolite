package com.enxy.noolite.presentation.ui.home.sections

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.enxy.domain.features.common.Script
import com.enxy.noolite.R
import com.enxy.noolite.presentation.ui.theme.AppTheme
import com.enxy.noolite.presentation.utils.FakeUiDataProvider
import com.enxy.noolite.presentation.utils.ThemedPreview
import com.enxy.noolite.presentation.utils.extensions.pluralResource

data class ScriptState(
    val onScriptClick: (script: Script) -> Unit = {},
    val onScriptRemove: (script: Script) -> Unit = {}
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Scripts(
    scripts: List<Script>,
    modifier: Modifier = Modifier,
    state: ScriptState = ScriptState(),
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(AppTheme.dimensions.normal),
    contentPadding: PaddingValues = PaddingValues(AppTheme.dimensions.normal)
) {
    LazyRow(
        horizontalArrangement = horizontalArrangement,
        contentPadding = contentPadding,
        modifier = modifier
    ) {
        items(
            items = scripts,
            key = { script -> script.id }
        ) { script ->
            Script(
                script = script,
                state = state,
                modifier = Modifier.animateItemPlacement()
            )
        }
    }
}

@Composable
private fun Script(
    script: Script,
    state: ScriptState,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier then Modifier
            .clip(AppTheme.shapes.large)
            .background(AppTheme.colors.surface)
            .clickable { state.onScriptClick(script) }
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
                Text(text = script.name)
                Text(
                    text = pluralResource(
                        R.plurals.script_actions,
                        script.actions.size,
                        script.actions.size
                    ),
                    style = AppTheme.typography.body2
                )
            }
            IconButton(
                onClick = { state.onScriptRemove(script) }
            ) {
                Icon(imageVector = Icons.Default.Close, contentDescription = null)
            }
        }
    }
}

@Preview("Scripts")
@Preview("Scripts (dark)", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewScripts() {
    ThemedPreview {
        Scripts(FakeUiDataProvider.getScripts())
    }
}
