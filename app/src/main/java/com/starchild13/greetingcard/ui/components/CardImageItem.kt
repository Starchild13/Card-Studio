/**
 * Image and animated GIF loading components for Card Studio.
 *
 * Provides Coil-backed image rendering with automatic animated GIF decoder selection
 * based on Android SDK level, fallback gradient generation for placeholder images,
 * and standalone animated GIF sticker badges.
 */
package com.starchild13.greetingcard.ui.components

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest

/**
 * Card Image Item Composable.
 *
 * Renders a photo or animated GIF asset using Coil's [AsyncImage].
 * Automatically configures SDK-appropriate GIF decoders ([ImageDecoderDecoder] for API 28+, [GifDecoder] for older SDKs).
 * If [imageModel] is null, displays a fallback linear gradient background with an occasion icon placeholder.
 *
 * @param imageModel Image target object (Uri, URL, or drawable resource ID).
 * @param index Position index used for selecting a deterministic fallback gradient.
 * @param modifier Optional modifier for sizing and alignment.
 * @param contentScale Scaling behavior for fitting image within bounds.
 */
@Composable
fun CardImageItem(
    imageModel: Any?,
    index: Int,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop
) {
    val context = LocalContext.current

    // Initialize Coil ImageLoader with SDK-level specific GIF decoding support
    val imageLoader = remember(context) {
        ImageLoader.Builder(context)
            .components {
                // Use native ImageDecoderDecoder on Android P (API 28) and above, else legacy GifDecoder
                if (Build.VERSION.SDK_INT >= 28) {
                    add(ImageDecoderDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
            }
            .crossfade(true)
            .build()
    }

    // Curated fallback linear gradients when image model is absent
    val fallbackGradients = listOf(
        Brush.linearGradient(listOf(Color(0xFFFF512F), Color(0xFFDD2476))),
        Brush.linearGradient(listOf(Color(0xFF8A2387), Color(0xFFE94057), Color(0xFFF27121))),
        Brush.linearGradient(listOf(Color(0xFF11998E), Color(0xFF38EF7D))),
        Brush.linearGradient(listOf(Color(0xFF4A00E0), Color(0xFF8E2DE2)))
    )
    val gradient = fallbackGradients[index % fallbackGradients.size]

    Box(
        modifier = modifier.background(gradient),
        contentAlignment = Alignment.Center
    ) {
        if (imageModel != null) {
            // Load photo or GIF via Coil AsyncImage
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(imageModel)
                    .crossfade(true)
                    .build(),
                imageLoader = imageLoader,
                contentDescription = "Birthday photo or GIF ${index + 1}",
                contentScale = contentScale,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            // Placeholder rendering with icon and photo number tag
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.25f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (index % 2 == 0) Icons.Default.Cake else Icons.Default.PhotoCamera,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }
            Text(
                text = "Photo/GIF #${index + 1}",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 12.sp,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 12.dp)
            )
        }
    }
}

/**
 * Animated GIF Badge Composable.
 *
 * Specialized component for rendering standalone animated GIF stickers overlaid on card layouts.
 * Uses a Coil [ImageLoader] configured for animated GIF playback.
 *
 * @param gifModel Source model for GIF asset (Uri string, web URL, or resource ID).
 * @param modifier Layout modifier for sizing and placement.
 */
@Composable
fun AnimatedGifBadge(
    gifModel: Any?,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // Initialize GIF-compatible Coil ImageLoader
    val imageLoader = remember(context) {
        ImageLoader.Builder(context)
            .components {
                if (Build.VERSION.SDK_INT >= 28) {
                    add(ImageDecoderDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
            }
            .crossfade(true)
            .build()
    }

    if (gifModel != null) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(gifModel)
                .crossfade(true)
                .build(),
            imageLoader = imageLoader,
            contentDescription = "Animated Birthday Sticker",
            contentScale = ContentScale.Fit,
            modifier = modifier
        )
    }
}
