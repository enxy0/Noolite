package com.enxy.noolite.presentation.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.enxy.noolite.R

internal val LocalTypography = staticCompositionLocalOf { AppTypography() }

internal val openSansFamily = FontFamily(
    Font(R.font.opensans_regular, FontWeight.Normal),
    Font(R.font.opensans_medium, FontWeight.Medium),
    Font(R.font.opensans_semibold, FontWeight.SemiBold),
    Font(R.font.opensans_bold, FontWeight.Bold),
)

data class AppTypography(
    val h6: TextStyle = TextStyle(
        fontFamily = openSansFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp
    ),
    val body1: TextStyle = TextStyle(
        fontFamily = openSansFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp
    ),
    val body2: TextStyle = TextStyle(
        fontFamily = openSansFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        color = Color(0xFF888888)
    ),
    val button: TextStyle = TextStyle(
        fontFamily = openSansFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp
    ),
    val button1: TextStyle = TextStyle(
        fontFamily = openSansFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp
    ),
    val caption: TextStyle = TextStyle(
        fontFamily = openSansFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        letterSpacing = 0.4.sp
    )
)
