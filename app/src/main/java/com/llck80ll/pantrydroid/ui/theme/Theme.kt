package com.llck80ll.pantrydroid.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val PantryGreen = Color(0xFF2E6E4F)
val PantryOrange = Color(0xFFE87832)
val PantryCream = Color(0xFFF7F3E8)

private val LightColors = lightColorScheme(
    primary = PantryGreen,
    secondary = PantryOrange,
    background = PantryCream,
    surface = Color(0xFFFFFBF4),
    surfaceVariant = Color(0xFFEDE7DA)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF79D3A3),
    secondary = Color(0xFFFFB17D),
    background = Color(0xFF151A17),
    surface = Color(0xFF1D2420),
    surfaceVariant = Color(0xFF29312C)
)

@Composable
fun PantryDroidTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) DarkColors else LightColors,
        content = content
    )
}

