package com.enxy.noolite.presentation.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

internal val LocalDimensions = staticCompositionLocalOf { AppDimensions() }

data class AppDimensions(
    val smallest: Dp = 8.dp,
    val small: Dp = 12.dp,
    val normal: Dp = 16.dp,
    val big: Dp = 24.dp,
    val bigger: Dp = 40.dp
)
