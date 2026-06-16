package com.llck80ll.pantrydroid.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val PantryIndigo = Color(0xFF4F46E5)
val PantryPurple = Color(0xFF6D28D9)
val PantryAmber = Color(0xFFF59E0B)
val PantryCream = Color(0xFFFAF8F5)
val PantryInk = Color(0xFF15141D)

private val LightColors = lightColorScheme(
    primary = PantryIndigo,
    secondary = PantryAmber,
    tertiary = PantryPurple,
    background = PantryCream,
    surface = Color.White,
    surfaceVariant = Color(0xFFF4F4F5),
    outline = Color(0xFFE4E4E7),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onSurface = PantryInk,
    onBackground = PantryInk
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFA5B4FC),
    secondary = Color(0xFFFBBF24),
    tertiary = Color(0xFFC4B5FD),
    background = Color(0xFF09090B),
    surface = Color(0xFF121118),
    surfaceVariant = Color(0xFF27272A),
    outline = Color(0xFF3F3F46),
    onSurface = Color(0xFFFAFAFA),
    onBackground = Color(0xFFFAFAFA)
)

@Composable
fun PantryDroidTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) DarkColors else LightColors,
        content = content
    )
}
