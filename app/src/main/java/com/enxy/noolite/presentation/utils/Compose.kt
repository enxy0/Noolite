package com.enxy.noolite.presentation.utils

import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import com.enxy.noolite.presentation.ui.theme.AppTheme

@Composable
fun ThemedPreview(content: @Composable () -> Unit) {
    AppTheme {
        Surface(color = AppTheme.colors.background) {
            content()
        }
    }
}
