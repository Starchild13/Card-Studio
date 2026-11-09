/**
 * Material design shape specifications for Card Studio.
 *
 * Defines corner rounding for UI components such as buttons, cards,
 * text fields, and dialog containers.
 */
package com.starchild13.greetingcard.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Shapes
import androidx.compose.ui.unit.dp

/**
 * Default Material Shapes configuration.
 *
 * - [Shapes.small]: 4.dp rounded corners for small UI elements like chips or badges.
 * - [Shapes.medium]: 4.dp rounded corners for medium UI elements like buttons and text fields.
 * - [Shapes.large]: 0.dp sharp corners for full-bleed containers.
 */
val Shapes = Shapes(
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(4.dp),
    large = RoundedCornerShape(0.dp)
)
