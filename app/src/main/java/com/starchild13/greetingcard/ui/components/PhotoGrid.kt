/**
 * Photo grid component for Card Studio.
 *
 * Renders photos and animated GIFs in a symmetrical 2x2 layout with rounded borders.
 */
package com.starchild13.greetingcard.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.starchild13.greetingcard.model.CardTemplate

/**
 * Grid Photo Layout inspired by Pinterest mood boards and Instagram explore grids.
 *
 * Renders up to 4 photos or animated GIFs in a balanced 2x2 symmetrical grid.
 *
 * @param images The list of images and GIFs to display.
 * @param template Active visual template for accent borders.
 * @param modifier Optional modifier for outer layout styling.
 */
@Composable
fun PhotoGrid(
    images: List<Any>,
    template: CardTemplate,
    modifier: Modifier = Modifier
) {
    // Resolve up to 4 picture slots with fallback reuse if fewer images are provided
    val pic1 = images.getOrNull(0)
    val pic2 = images.getOrNull(1) ?: pic1
    val pic3 = images.getOrNull(2) ?: pic1
    val pic4 = images.getOrNull(3) ?: pic2

    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .padding(4.dp)
    ) {
        // 2x2 Grid Arrangement using Column and nested Rows
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // Top Row (2 photos: pic1 and pic2)
            Row(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                        .clip(RoundedCornerShape(8.dp))
                        .border(1.dp, template.accentColor.copy(alpha = 0.25f), RoundedCornerShape(8.dp))
                ) {
                    CardImageItem(imageModel = pic1, index = 0, modifier = Modifier.fillMaxSize())
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                        .clip(RoundedCornerShape(8.dp))
                        .border(1.dp, template.accentColor.copy(alpha = 0.25f), RoundedCornerShape(8.dp))
                ) {
                    CardImageItem(imageModel = pic2, index = 1, modifier = Modifier.fillMaxSize())
                }
            }

            // Bottom Row (2 photos: pic3 and pic4)
            Row(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                        .clip(RoundedCornerShape(8.dp))
                        .border(1.dp, template.accentColor.copy(alpha = 0.25f), RoundedCornerShape(8.dp))
                ) {
                    CardImageItem(imageModel = pic3, index = 2, modifier = Modifier.fillMaxSize())
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                        .clip(RoundedCornerShape(8.dp))
                        .border(1.dp, template.accentColor.copy(alpha = 0.25f), RoundedCornerShape(8.dp))
                ) {
                    CardImageItem(imageModel = pic4, index = 3, modifier = Modifier.fillMaxSize())
                }
            }
        }
    }
}
