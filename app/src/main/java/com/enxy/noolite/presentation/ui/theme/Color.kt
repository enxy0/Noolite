package com.enxy.noolite.presentation.ui.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.runtime.structuralEqualityPolicy
import androidx.compose.ui.graphics.Color

internal val LocalColors = staticCompositionLocalOf { lightColors() }

internal val SurfaceLight = Color(0xFFF0F0F0)
internal val SurfaceDark = Color(0xFF1B1B1B)
internal val SurfaceVariantLight = Color(0xFFDFDFDF)
internal val SurfaceVariantDark = Color(0xFF2F2F2F)
internal val AmberA700 = Color(0xFFFFAB00)
internal val Amber500 = Color(0xFFFFC107)

internal fun lightColors(
    primary: Color = Amber500,
    primaryVariant: Color = Amber500,
    secondary: Color = Amber500,
    secondaryVariant: Color = secondary,
    background: Color = Color.White,
    surface: Color = SurfaceLight,
    surfaceVariant: Color = SurfaceVariantLight,
    error: Color = Color(0xFFB00020),
    onPrimary: Color = Color.White,
    onSecondary: Color = Color.Black,
    onBackground: Color = Color.Black,
    onSurface: Color = Color.Black,
    onError: Color = Color.White
): AppColors = AppColors(
    primary,
    primaryVariant,
    secondary,
    secondaryVariant,
    background,
    surface,
    surfaceVariant,
    error,
    onPrimary,
    onSecondary,
    onBackground,
    onSurface,
    onError,
    true
)

internal fun darkColors(
    primary: Color = AmberA700,
    primaryVariant: Color = AmberA700,
    secondary: Color = AmberA700,
    secondaryVariant: Color = secondary,
    background: Color = Color.Black,
    surface: Color = SurfaceDark,
    surfaceVariant: Color = SurfaceVariantDark,
    error: Color = Color(0xFFCF6679),
    onPrimary: Color = Color.Black,
    onSecondary: Color = Color.Black,
    onBackground: Color = Color.White,
    onSurface: Color = Color.White,
    onError: Color = Color.Black
): AppColors = AppColors(
    primary,
    primaryVariant,
    secondary,
    secondaryVariant,
    background,
    surface,
    surfaceVariant,
    error,
    onPrimary,
    onSecondary,
    onBackground,
    onSurface,
    onError,
    false
)

class AppColors(
    primary: Color,
    primaryVariant: Color,
    secondary: Color,
    secondaryVariant: Color,
    background: Color,
    surface: Color,
    surfaceVariant: Color,
    error: Color,
    onPrimary: Color,
    onSecondary: Color,
    onBackground: Color,
    onSurface: Color,
    onError: Color,
    isLight: Boolean
) {
    var primary by mutableStateOf(primary, structuralEqualityPolicy())
        internal set
    var primaryVariant by mutableStateOf(primaryVariant, structuralEqualityPolicy())
        internal set
    var secondary by mutableStateOf(secondary, structuralEqualityPolicy())
        internal set
    var secondaryVariant by mutableStateOf(secondaryVariant, structuralEqualityPolicy())
        internal set
    var background by mutableStateOf(background, structuralEqualityPolicy())
        internal set
    var surface by mutableStateOf(surface, structuralEqualityPolicy())
        internal set
    var surfaceVariant by mutableStateOf(surfaceVariant, structuralEqualityPolicy())
        internal set
    var error by mutableStateOf(error, structuralEqualityPolicy())
        internal set
    var onPrimary by mutableStateOf(onPrimary, structuralEqualityPolicy())
        internal set
    var onSecondary by mutableStateOf(onSecondary, structuralEqualityPolicy())
        internal set
    var onBackground by mutableStateOf(onBackground, structuralEqualityPolicy())
        internal set
    var onSurface by mutableStateOf(onSurface, structuralEqualityPolicy())
        internal set
    var onError by mutableStateOf(onError, structuralEqualityPolicy())
        internal set
    var isLight by mutableStateOf(isLight, structuralEqualityPolicy())
        internal set

    /**
     * Returns a copy of this Colors, optionally overriding some of the values.
     */
    fun copy(
        primary: Color = this.primary,
        primaryVariant: Color = this.primaryVariant,
        secondary: Color = this.secondary,
        secondaryVariant: Color = this.secondaryVariant,
        background: Color = this.background,
        surface: Color = this.surface,
        surfaceVariant: Color = this.surfaceVariant,
        error: Color = this.error,
        onPrimary: Color = this.onPrimary,
        onSecondary: Color = this.onSecondary,
        onBackground: Color = this.onBackground,
        onSurface: Color = this.onSurface,
        onError: Color = this.onError,
        isLight: Boolean = this.isLight
    ): AppColors = AppColors(
        primary,
        primaryVariant,
        secondary,
        secondaryVariant,
        background,
        surface,
        surfaceVariant,
        error,
        onPrimary,
        onSecondary,
        onBackground,
        onSurface,
        onError,
        isLight
    )

    fun updateColorsFrom(other: AppColors) {
        primary = other.primary
        primaryVariant = other.primaryVariant
        secondary = other.secondary
        secondaryVariant = other.secondaryVariant
        background = other.background
        surface = other.surface
        surfaceVariant = other.surfaceVariant
        error = other.error
        onPrimary = other.onPrimary
        onSecondary = other.onSecondary
        onBackground = other.onBackground
        onSurface = other.onSurface
        onError = other.onError
        isLight = other.isLight
    }

    override fun toString(): String {
        return "Colors(" +
                "primary=$primary, " +
                "primaryVariant=$primaryVariant, " +
                "secondary=$secondary, " +
                "secondaryVariant=$secondaryVariant, " +
                "background=$background, " +
                "surface=$surface, " +
                "surface=$surfaceVariant, " +
                "error=$error, " +
                "onPrimary=$onPrimary, " +
                "onSecondary=$onSecondary, " +
                "onBackground=$onBackground, " +
                "onSurface=$onSurface, " +
                "onError=$onError, " +
                "isLight=$isLight" +
                ")"
    }
}
