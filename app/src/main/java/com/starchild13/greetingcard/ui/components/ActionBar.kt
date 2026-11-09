/**
 * Footer and Action Bar components for Card Studio greeting cards.
 *
 * Renders personalized greeting message containers, occasion sign-offs,
 * and animated heart reaction toggle buttons.
 */
package com.starchild13.greetingcard.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.starchild13.greetingcard.model.CardCategory
import com.starchild13.greetingcard.model.CardTemplate

/**
 * Clean Greeting Card Message and Footer Section.
 *
 * Designed with a structured presentation:
 * - Thoughtfully formatted message container with rounded corners and subtle border tint.
 * - Quote marks accent for a personalized greeting feel.
 * - Warm celebratory sign-off based on occasion.
 * - Interactive heart reaction pill button.
 *
 * @param customMessage The personalized greeting message.
 * @param template The active [CardTemplate] defining typography and colors.
 * @param isLiked State of the celebratory like reaction.
 * @param onLikeToggle Callback when the like heart is toggled.
 * @param signOff Optional custom sign-off message (e.g. "With love, Theo").
 * @param modifier Optional layout modifier.
 */
@Composable
fun CardFooter(
    customMessage: String,
    template: CardTemplate,
    isLiked: Boolean,
    onLikeToggle: () -> Unit,
    signOff: String? = null,
    modifier: Modifier = Modifier
) {
    // Step 1: Animate heart scale popping effect when liked
    val heartScale by animateFloatAsState(
        targetValue = if (isLiked) 1.25f else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "heartScale"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Step 2: Message container with clean rounded card frame & subtle border
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(template.surfaceColor.copy(alpha = 0.85f))
                .border(
                    width = 1.dp,
                    color = template.accentColor.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(horizontal = 18.dp, vertical = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "“",
                    color = template.accentColor.copy(alpha = 0.6f),
                    fontSize = 22.sp,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 14.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = customMessage,
                    color = template.textColor,
                    fontSize = 14.sp,
                    lineHeight = 22.sp,
                    textAlign = TextAlign.Center,
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.Normal
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Step 3: Sign-off and Heart Pill Button Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val effectiveSignOff = signOff?.takeIf { it.isNotBlank() } ?: when (template.category) {
                CardCategory.BIRTHDAY -> "With love & warmest wishes ❤️"
                CardCategory.CONGRATULATIONS -> "Here's to your next chapter! 🥂✨"
            }

            Text(
                text = effectiveSignOff,
                color = template.textColor.copy(alpha = 0.8f),
                fontSize = 13.sp,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Medium
            )

            // Step 4: Interactive heart pill action button
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        if (isLiked) Color(0xFFED4956).copy(alpha = 0.15f)
                        else template.textColor.copy(alpha = 0.08f)
                    )
                    .clickable { onLikeToggle() }
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = if (isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Celebrate with heart",
                        tint = if (isLiked) Color(0xFFED4956) else template.textColor.copy(alpha = 0.6f),
                        modifier = Modifier
                            .size(16.dp)
                            .scale(heartScale)
                    )
                    Text(
                        text = if (isLiked) "Loved" else "Love",
                        color = if (isLiked) Color(0xFFED4956) else template.textColor.copy(alpha = 0.7f),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}
