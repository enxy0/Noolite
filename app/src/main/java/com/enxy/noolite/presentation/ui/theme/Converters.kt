package com.enxy.noolite.presentation.ui.theme

import androidx.compose.material.Colors
import androidx.compose.material.Shapes
import androidx.compose.material.Typography

internal fun AppColors.toMaterialColors() = Colors(
    primary = primary,
    primaryVariant = primaryVariant,
    secondary = secondary,
    secondaryVariant = secondaryVariant,
    background = background,
    surface = surface,
    error = error,
    onPrimary = onPrimary,
    onSecondary = onSecondary,
    onBackground = onBackground,
    onSurface = onSurface,
    onError = onError,
    isLight = isLight
)

internal fun AppTypography.toMaterialTypography() = Typography(
    defaultFontFamily = openSansFamily,
    h6 = h6,
    body1 = body1,
    body2 = body2,
    button = button,
    caption = caption
)

internal fun AppShapes.toMaterialShapes() = Shapes(
    small = small,
    medium = medium,
    large = large
)
