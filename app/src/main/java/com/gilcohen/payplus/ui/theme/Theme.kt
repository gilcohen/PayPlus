package com.gilcohen.payplus.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// The design is light-only, so there is no dark or dynamic (Material You) scheme.
private val LightColorScheme = lightColorScheme(
    primary = PayPlusGreen,
    onPrimary = Color.White,
    secondary = DarkGray,
    onSecondary = Color.White,
    error = RejectedRed,
    background = Color.White,
    onBackground = DarkGray,
    surface = Color.White,
    onSurface = DarkGray,
    surfaceContainerHigh = Color.White,
)

@Composable
fun PayPlusTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content,
    )
}
