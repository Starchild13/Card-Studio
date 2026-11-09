/**
 * Material Theme configuration for Card Studio.
 *
 * Configures dark and light color palettes, typography, and shapes for the application.
 */
package com.starchild13.greetingcard.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * Dark color palette configuration.
 *
 * Uses ElectricPurple as primary, BlueNeon as secondary, and SlateDark/SlateSurface
 * for background and surface tones.
 */
private val DarkColorPalette = darkColors(
    primary = ElectricPurple,
    primaryVariant = DeepViolet,
    secondary = BlueNeon,
    background = SlateDark,
    surface = SlateSurface,
    onPrimary = Color.White,
    onBackground = SilverLight,
    onSurface = SilverLight,
)

/**
 * Light color palette configuration.
 *
 * Uses Purple500 as primary, BluePrimary as secondary, and SilverLight/White
 * for clean light surfaces.
 */
private val LightColorPalette = lightColors(
    primary = Purple500,
    primaryVariant = Purple700,
    secondary = BluePrimary,
    background = SilverLight,
    surface = Color.White,
    onPrimary = Color.White,
    onBackground = SlateDark,
    onSurface = SlateDark,
)

/**
 * Root Composable wrapper that applies [MaterialTheme] styling to the app.
 *
 * @param darkTheme Whether to force dark theme colors or follow system preferences.
 * @param content The Composable UI content hierarchy to wrap with theme styling.
 */
@Composable
fun GreetingCardTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
