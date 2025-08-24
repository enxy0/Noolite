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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.enxy.noolite.R
import com.enxy.noolite.domain.features.actions.model.GroupAction
import com.enxy.noolite.domain.features.common.Group
import com.enxy.noolite.presentation.ui.common.IconActionButton
import com.enxy.noolite.presentation.utils.FakeUiDataProvider
import com.enxy.noolite.presentation.utils.ThemedPreview

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Groups(
    groups: List<Group>,
    onGroupClick: (group: Group) -> Unit,
    onGroupAction: (action: GroupAction) -> Unit,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(16.dp),
    contentPadding: PaddingValues = PaddingValues(16.dp)
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
                onGroupClick = { onGroupClick(group) },
                onGroupAction = onGroupAction,
                modifier = Modifier.animateItem()
            )
        }
    }
}

@Composable
private fun Group(
    group: Group,
    onGroupClick: () -> Unit,
    onGroupAction: (action: GroupAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier then Modifier
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .clickable(onClick = onGroupClick)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .size(height = 125.dp, width = 145.dp),
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                IconActionButton(
                    painter = painterResource(R.drawable.ic_on),
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    onClick = { onGroupAction(GroupAction.TurnOn(group)) }
                )
                IconActionButton(
                    painter = painterResource(R.drawable.ic_off),
                    onClick = { onGroupAction(GroupAction.TurnOff(group)) }
                )
            }
            Spacer(Modifier.height(8.dp))
            Text(
                text = group.name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = group.channelNames,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Preview("Group")
@Preview("Group (dark)", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewGroup() {
    ThemedPreview {
        Group(
            group = FakeUiDataProvider.getFavoriteGroup(),
            onGroupClick = {},
            onGroupAction = {},
        )
    }
}

@Preview("Groups")
@Preview("Groups (dark)", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewGroups() {
    ThemedPreview {
        Groups(
            groups = FakeUiDataProvider.getGroups(),
            onGroupClick = {},
            onGroupAction = {},
        )
    }
}
