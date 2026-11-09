/**
 * Header component for Card Studio greeting cards.
 *
 * Displays occasion sticker tags, festive headlines, and recipient greeting titles,
 * supporting custom color palettes and template styling.
 */
package com.starchild13.greetingcard.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.starchild13.greetingcard.model.CardColorPalette
import com.starchild13.greetingcard.model.CardTemplate

/**
 * Clean, modern, and spacious Greeting Card Header.
 *
 * Designed with elegant typography hierarchy, occasion tag pill badge,
 * and decorative accent lines around recipient title.
 *
 * @param recipientName Name of the person receiving the card.
 * @param template Active [CardTemplate] defining font family, weight, and fallback styling.
 * @param headline Festive banner title (defaults to template headline).
 * @param palette Optional active [CardColorPalette] providing custom color overrides.
 * @param modifier Optional layout modifier.
 */
@Composable
fun CardHeader(
    recipientName: String,
    template: CardTemplate,
    headline: String = template.headline,
    palette: CardColorPalette? = null,
    modifier: Modifier = Modifier
) {
    // Resolve effective gradient, accent color, and text color based on palette or template
    val gradient = palette?.gradientBrush ?: template.gradientBrush
    val accent = palette?.accentColor ?: template.accentColor
    val textCol = palette?.textColor ?: template.textColor

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 18.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Section 1: Occasion Tag Pill Badge
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .background(gradient)
                .padding(horizontal = 16.dp, vertical = 6.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = template.tagText,
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.0.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Section 2: Prominent Festive Headline
        Text(
            text = headline.ifBlank { template.headline },
            color = accent,
            fontSize = 26.sp,
            fontFamily = template.fontFamily,
            fontWeight = template.fontWeight,
            letterSpacing = 0.5.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Section 3: Recipient Greeting Title with side accent lines
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 12.dp)
        ) {
            Box(
                modifier = Modifier
                    .width(28.dp)
                    .height(1.dp)
                    .background(accent.copy(alpha = 0.4f))
            )
            Text(
                text = if (recipientName.isNotBlank()) "Dearest $recipientName" else "Celebrating You!",
                color = textCol.copy(alpha = 0.95f),
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(horizontal = 10.dp)
            )
            Box(
                modifier = Modifier
                    .width(28.dp)
                    .height(1.dp)
                    .background(accent.copy(alpha = 0.4f))
            )
        }
    }
}
