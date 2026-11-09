/**
 * Carousel layout component for Card Studio.
 *
 * Implements swipeable horizontal pagination for photos and animated GIFs
 * with dynamic slide badges and animated indicator dots.
 */
package com.starchild13.greetingcard.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.starchild13.greetingcard.model.CardTemplate

/**
 * Swipeable Carousel Layout inspired by multi-image Instagram posts.
 *
 * Renders photos and animated GIFs in a full-bleed horizontal pager with:
 * - Slide counter badge (e.g., "1/3") in the top right.
 * - Dynamic pagination pill indicators at the bottom center.
 *
 * @param images The list of image or GIF models (Uris, URLs).
 * @param template Active visual template for accent colors.
 * @param modifier Optional modifier for outer layout styling.
 */
@Composable
fun PhotoCarousel(
    images: List<Any>,
    template: CardTemplate,
    modifier: Modifier = Modifier
) {
    // 1. Initialize HorizontalPager state with page count
    val count = images.size.coerceAtLeast(1)
    val pagerState = rememberPagerState(pageCount = { count })

    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(RoundedCornerShape(16.dp))
    ) {
        // 2. Horizontal Pager for swiping through photos & GIFs
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            val imageModel = images.getOrNull(page)
            CardImageItem(
                imageModel = imageModel,
                index = page,
                modifier = Modifier.fillMaxSize()
            )
        }

        // 3. Instagram-style photo index badge (e.g. "1/3")
        if (count > 1) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.Black.copy(alpha = 0.65f))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "${pagerState.currentPage + 1}/$count",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            // 4. Pagination dots indicator with animated width expansion for active page
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(count) { index ->
                    val isSelected = pagerState.currentPage == index
                    val dotWidth by animateDpAsState(
                        targetValue = if (isSelected) 18.dp else 6.dp,
                        label = "dotWidth"
                    )
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 3.dp)
                            .height(6.dp)
                            .width(dotWidth)
                            .clip(CircleShape)
                            .background(
                                if (isSelected) template.accentColor else Color.White.copy(alpha = 0.5f)
                            )
                    )
                }
            }
        }
    }
}
