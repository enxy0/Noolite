package com.enxy.noolite.features.settings.theme

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.enxy.noolite.domain.features.settings.model.AppSettings
import com.enxy.noolite.features.settings.asString
import com.enxy.noolite.utils.ThemedPreview

@Composable
internal fun ChangeThemeBottomSheetContent(
    component: ChangeThemeComponent,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        for (option in component.options) {
            Text(
                text = option.asString(),
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier
                    .clickable(onClick = { component.onThemeChange(option) })
                    .padding(16.dp)
                    .fillMaxWidth(),
            )
        }
        Spacer(Modifier.height(24.dp))
    }
}

@Preview("ChooseTheme")
@Preview("ChooseTheme (dark)", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewChooseThemeBottomSheet() {
    ThemedPreview {
        ChangeThemeBottomSheetContent(
            component = object : ChangeThemeComponent {
                override val options: List<AppSettings.Theme> = AppSettings.Theme.entries
                override fun onThemeChange(theme: AppSettings.Theme) = Unit
                override fun onDismiss() = Unit
            }
        )
    }
}
