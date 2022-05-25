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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.enxy.domain.features.actions.model.GroupAction
import com.enxy.domain.features.common.Group
import com.enxy.noolite.R
import com.enxy.noolite.presentation.ui.common.ActionButton
import com.enxy.noolite.presentation.ui.theme.AppTheme
import com.enxy.noolite.presentation.utils.FakeUiDataProvider
import com.enxy.noolite.presentation.utils.ThemedPreview

data class GroupState(
    val onGroupClick: (group: Group) -> Unit = {},
    val onAction: (action: GroupAction) -> Unit = {}
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Groups(
    groups: List<Group>,
    state: GroupState = GroupState(),
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(AppTheme.dimensions.normal),
    contentPadding: PaddingValues = PaddingValues(AppTheme.dimensions.normal)
) {
    LazyRow(
        horizontalArrangement = horizontalArrangement,
        contentPadding = contentPadding
    ) {
        items(
            items = groups,
            key = { group -> group.id }
        ) { group ->
            Group(
                group = group,
                state = state,
                modifier = Modifier.animateItemPlacement()
            )
        }
    }
}

@Composable
private fun Group(
    group: Group,
    state: GroupState,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier then Modifier
            .clip(AppTheme.shapes.large)
            .background(AppTheme.colors.surface)
            .clickable { state.onGroupClick(group) }
    ) {
        Column(
            modifier = Modifier
                .padding(AppTheme.dimensions.normal)
                .size(height = 125.dp, width = 145.dp),
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ActionButton(
                    painter = painterResource(R.drawable.ic_on),
                    modifier = Modifier.background(AppTheme.colors.primary),
                    tint = Color.Black,
                    onClick = { state.onAction(GroupAction.TurnOn(group)) }
                )
                ActionButton(
                    painter = painterResource(R.drawable.ic_off),
                    onClick = { state.onAction(GroupAction.TurnOff(group)) }
                )
            }
            Spacer(Modifier.height(AppTheme.dimensions.smallest))
            Text(
                text = group.name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = group.channelNames,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = AppTheme.typography.body2
            )
        }
    }
}

@Preview("Group")
@Preview("Group (dark)", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewGroup() {
    ThemedPreview {
        Group(FakeUiDataProvider.getFavoriteGroup(), GroupState())
    }
}

@Preview("Groups")
@Preview("Groups (dark)", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewGroups() {
    ThemedPreview {
        Groups(FakeUiDataProvider.getGroups())
    }
}
