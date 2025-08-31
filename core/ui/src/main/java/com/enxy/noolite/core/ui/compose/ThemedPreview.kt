package com.enxy.noolite.core.ui.compose

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.enxy.noolite.core.ui.theme.NooliteTheme

@Composable
fun ThemedPreview(content: @Composable () -> Unit) {
    NooliteTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            content()
        }
    }
}
