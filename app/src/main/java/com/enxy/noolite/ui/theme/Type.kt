package com.enxy.noolite.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.enxy.noolite.R

private val DefaultTypography = Typography()

internal val openSansFamily = FontFamily(
    Font(R.font.opensans_regular, FontWeight.Normal),
    Font(R.font.opensans_medium, FontWeight.Medium),
    Font(R.font.opensans_semibold, FontWeight.SemiBold),
    Font(R.font.opensans_bold, FontWeight.Bold),
)

val Typography = Typography(
    displayLarge = DefaultTypography.displayLarge.copy(
        fontFamily = openSansFamily,
    ),
    displayMedium = DefaultTypography.displayMedium.copy(
        fontFamily = openSansFamily,
    ),
    displaySmall = DefaultTypography.displaySmall.copy(
        fontFamily = openSansFamily,
    ),
    headlineLarge = DefaultTypography.headlineLarge.copy(
        fontFamily = openSansFamily,
    ),
    headlineMedium = DefaultTypography.headlineMedium.copy(
        fontFamily = openSansFamily,
    ),
    headlineSmall = DefaultTypography.headlineSmall.copy(
        fontFamily = openSansFamily,
    ),
    titleLarge = DefaultTypography.titleLarge.copy(
        fontFamily = openSansFamily,
    ),
    titleMedium = DefaultTypography.titleMedium.copy(
        fontFamily = openSansFamily,
    ),
    titleSmall = DefaultTypography.titleSmall.copy(
        fontFamily = openSansFamily,
    ),
    bodyLarge = DefaultTypography.bodyLarge.copy(
        fontFamily = openSansFamily,
    ),
    bodyMedium = DefaultTypography.bodyMedium.copy(
        fontFamily = openSansFamily,
    ),
    bodySmall = DefaultTypography.bodySmall.copy(
        fontFamily = openSansFamily,
    ),
    labelLarge = DefaultTypography.labelLarge.copy(
        fontFamily = openSansFamily,
    ),
    labelMedium = DefaultTypography.labelMedium.copy(
        fontFamily = openSansFamily,
    ),
    labelSmall = DefaultTypography.labelSmall.copy(
        fontFamily = openSansFamily,
    ),
)
