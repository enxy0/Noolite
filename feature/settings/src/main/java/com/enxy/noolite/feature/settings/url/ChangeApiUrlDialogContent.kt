package com.enxy.noolite.feature.settings.url

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.enxy.noolite.core.model.AppSettings
import com.enxy.noolite.core.ui.compose.ThemedPreview
import com.enxy.noolite.feature.settings.R

@Composable
internal fun ChangeApiUrlDialogContent(
    component: ChangeApiUrlComponent,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    val apiUrlFieldState = rememberTextFieldState(component.currentApiUrl)
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.surfaceContainer,
        shape = MaterialTheme.shapes.large,
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            Spacer(Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.settings_change_api_url_title),
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleLarge,
            )
            Spacer(Modifier.height(12.dp))
            Text(
                text = stringResource(R.string.settings_change_api_url_description),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
            )
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                state = apiUrlFieldState,
                onKeyboardAction = {
                    focusManager.clearFocus()
                    val apiUrl = apiUrlFieldState.text
                    if (apiUrl != component.currentApiUrl) {
                        component.onApplyClick(apiUrl.toString())
                    }
                    ImeAction.Done
                },
                keyboardOptions = KeyboardOptions(
                    autoCorrectEnabled = false,
                ),
                lineLimits = TextFieldLineLimits.SingleLine,
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
            )
            Spacer(Modifier.height(24.dp))
            Row(
                modifier = Modifier.align(Alignment.End),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                TextButton(
                    onClick = {
                        focusManager.clearFocus()
                        component.onDismiss()
                    },
                    shape = MaterialTheme.shapes.medium,
                ) {
                    Text(stringResource(R.string.settings_change_api_url_action_dismiss))
                }
                TextButton(
                    onClick = {
                        focusManager.clearFocus()
                        component.onApplyClick(apiUrlFieldState.text.toString())
                    },
                    enabled = apiUrlFieldState.text != component.currentApiUrl,
                    shape = MaterialTheme.shapes.medium,
                ) {
                    Text(stringResource(R.string.settings_change_api_url_action_apply))
                }
            }
        }
    }
}

@Preview("ApiUrlDialog")
@Preview("ApiUrlDialog (dark)", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewChangeApiUrlDialogContent() {
    ThemedPreview {
        ChangeApiUrlDialogContent(
            object : ChangeApiUrlComponent {
                override val currentApiUrl: String = AppSettings.default().apiUrl
                override fun onDismiss() = Unit
                override fun onApplyClick(apiUrl: String) = Unit
            }
        )
    }
}
