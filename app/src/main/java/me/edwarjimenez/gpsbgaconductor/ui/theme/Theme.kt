package me.edwarjimenez.gpsbgaconductor.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF0078FF),
    secondary = Color(0xFF00C6FF),
    tertiary = Color(0xFF00C66B),
    background = Color(0xFF0A0F1E),
    surface = Color(0xFF0D1830),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color(0xFFE0EEFF),
    onSurface = Color(0xFFE0EEFF)
)

@Composable
fun GpsBGAConductorTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        content = content
    )
}