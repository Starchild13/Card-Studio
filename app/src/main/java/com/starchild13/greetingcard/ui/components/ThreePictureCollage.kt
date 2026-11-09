/**
 * Three-picture collage layout component for Card Studio.
 *
 * Implements an editorial pin board aesthetic featuring a prominent hero photo
 * paired with vertically stacked secondary photo slots.
 */
package com.starchild13.greetingcard.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.starchild13.greetingcard.model.CardTemplate

/**
 * Editorial Three-Picture Layout inspired by Pinterest pin collages and Instagram story grids.
 *
 * Layout structure:
 * - Left column (60% width): Prominent tall hero photograph or GIF with a featured badge.
 * - Right column (40% width): Two vertically stacked secondary photos with matching borders.
 *
 * @param images List of photo and GIF items to arrange.
 * @param template Active visual template for accent borders and badge styling.
 * @param modifier Optional modifier for outer layout styling.
 */
@Composable
fun ThreePictureCollage(
    images: List<Any>,
    template: CardTemplate,
    modifier: Modifier = Modifier
) {
    // Resolve hero image and 2 secondary images with safe fallback indexing
    val pic1 = images.getOrNull(0)
    val pic2 = images.getOrNull(1) ?: pic1
    val pic3 = images.getOrNull(2) ?: pic1

    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .padding(4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // Main Hero Photo Column (Left: ~60% width, weight 1.2f)
            Box(
                modifier = Modifier
                    .weight(1.2f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(8.dp))
                    .border(1.dp, template.accentColor.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
            ) {
                CardImageItem(
                    imageModel = pic1,
                    index = 0,
                    modifier = Modifier.fillMaxSize()
                )

                // Editorial pin badge overlay on hero photo
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(template.accentColor.copy(alpha = 0.9f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "FEATURED ★",
                        color = Color.White,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Secondary Stacked Photos Column (Right: ~40% width, weight 0.8f)
            Column(
                modifier = Modifier
                    .weight(0.8f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Secondary Photo 1 (Top right pin)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .border(1.dp, template.accentColor.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                ) {
                    CardImageItem(
                        imageModel = pic2,
                        index = 1,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                // Secondary Photo 2 (Bottom right pin)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .border(1.dp, template.accentColor.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                ) {
                    CardImageItem(
                        imageModel = pic3,
                        index = 2,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}
