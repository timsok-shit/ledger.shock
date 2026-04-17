package com.accountbook.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = Yellow,
    onPrimary = Black,
    secondary = Peacock,
    onSecondary = White,
    tertiary = PeacockLight,
    background = Black,
    onBackground = White,
    surface = SurfaceDark,
    onSurface = White,
    surfaceVariant = SurfaceMid,
    onSurfaceVariant = TextSecondary,
    error = Color(0xFFFF4444),
    onError = White,
)

@Composable
fun AccountBookTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = AppTypography,
        content = content
    )
}
