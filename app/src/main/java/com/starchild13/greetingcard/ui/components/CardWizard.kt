package com.starchild13.greetingcard.ui.components

import android.net.Uri
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ViewCarousel
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.starchild13.greetingcard.model.CardCategory
import com.starchild13.greetingcard.model.CardColorPalette
import com.starchild13.greetingcard.model.CardColorPaletteCatalog
import com.starchild13.greetingcard.model.CardLayoutMode
import com.starchild13.greetingcard.model.CardTemplate
import com.starchild13.greetingcard.model.GifSticker
import com.starchild13.greetingcard.model.SampleGifStickers
import com.starchild13.greetingcard.model.TemplateCatalog

// ─────────────────────────────────────────────────────────────────────────────
// Step definitions
// ─────────────────────────────────────────────────────────────────────────────

private data class WizardStep(val index: Int, val emoji: String, val label: String)

private val WIZARD_STEPS = listOf(
    WizardStep(0, "🎨", "Colour"),
    WizardStep(1, "🖼️", "Photos"),
    WizardStep(2, "✍️", "Text"),
    WizardStep(3, "💾", "Save")
)

private val birthdayMessageSuggestions = listOf(
    "🎂 Wishing you a year ahead filled with laughter, love, and endless happiness!",
    "🌟 May all your biggest dreams and wishes turn into reality this year!",
    "🎉 Another year wiser, bolder, and more wonderful! Keep shining bright!",
    "💖 Celebrating the incredible person you are today and always!"
)

private val congratsMessageSuggestions = listOf(
    "🎓 Cap tossed, degree earned! So proud of your hard work and brilliance!",
    "🚀 Huge congratulations on your career level up! The sky is your only limit!",
    "🥂 Hard work pays off and excellence gets noticed! Celebrating your triumph!",
    "🌟 What an extraordinary achievement! Wishing you continued success!"
)

// ─────────────────────────────────────────────────────────────────────────────
// Main Wizard Composable
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Stepped Card Builder Wizard.
 *
 * Guides the user through four focused stages:
 *   Step 1 – Colour & Theme
 *   Step 2 – Photos & GIF Stickers
 *   Step 3 – Text & Message
 *   Step 4 – Save / Download / Share
 */
@Composable
fun CardWizard(
    // Colour & Theme
    selectedCategory: CardCategory,
    onCategoryChange: (CardCategory) -> Unit,
    selectedPalette: CardColorPalette,
    onPaletteChange: (CardColorPalette) -> Unit,
    selectedTemplate: CardTemplate,
    onTemplateChange: (CardTemplate) -> Unit,
    selectedLayout: CardLayoutMode,
    onLayoutChange: (CardLayoutMode) -> Unit,
    // Photos
    uploadedPhotoCount: Int,
    onUploadPhotosClick: () -> Unit,
    onUploadCustomGifClick: (() -> Unit)? = null,
    selectedGifSticker: GifSticker? = null,
    onGifStickerSelect: ((GifSticker?) -> Unit)? = null,
    photoUris: List<Uri> = emptyList(),
    onRemovePhoto: ((Uri) -> Unit)? = null,
    onClearPhotos: (() -> Unit)? = null,
    placedGifCount: Int = 0,
    onClearGifs: (() -> Unit)? = null,
    onBackClick: (() -> Unit)? = null,
    // Text
    recipientName: String,
    onNameChange: (String) -> Unit,
    headline: String,
    onHeadlineChange: (String) -> Unit,
    customMessage: String,
    onMessageChange: (String) -> Unit,
    signOff: String,
    onSignOffChange: (String) -> Unit,
    // Save actions
    onSaveCardClick: () -> Unit,
    onDownloadClick: () -> Unit,
    onShareClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var currentStep by remember { mutableIntStateOf(0) }
    val totalSteps = WIZARD_STEPS.size

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .background(Color(0xFF141419))
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        // ── Step Progress Bar ──────────────────────────────────────────────
        WizardProgressBar(currentStep = currentStep, steps = WIZARD_STEPS)

        Spacer(modifier = Modifier.height(20.dp))

        // ── Animated Step Content ──────────────────────────────────────────
        AnimatedContent(
            targetState = currentStep,
            transitionSpec = {
                if (targetState > initialState) {
                    slideInHorizontally { it } togetherWith slideOutHorizontally { -it }
                } else {
                    slideInHorizontally { -it } togetherWith slideOutHorizontally { it }
                }
            },
            label = "wizard_step"
        ) { step ->
            when (step) {
                0 -> StepColourTheme(
                    selectedCategory = selectedCategory,
                    onCategoryChange = onCategoryChange,
                    selectedPalette = selectedPalette,
                    onPaletteChange = onPaletteChange,
                    selectedTemplate = selectedTemplate,
                    onTemplateChange = onTemplateChange,
                    selectedLayout = selectedLayout,
                    onLayoutChange = onLayoutChange
                )
                1 -> StepPhotosGifs(
                    uploadedPhotoCount = uploadedPhotoCount,
                    onUploadPhotosClick = onUploadPhotosClick,
                    onUploadCustomGifClick = onUploadCustomGifClick,
                    selectedGifSticker = selectedGifSticker,
                    onGifStickerSelect = onGifStickerSelect,
                    photoUris = photoUris,
                    onRemovePhoto = onRemovePhoto,
                    onClearPhotos = onClearPhotos,
                    placedGifCount = placedGifCount,
                    onClearGifs = onClearGifs,
                    onPreviousStep = { currentStep = 0 },
                    onNextStep = { currentStep = 2 }
                )
                2 -> StepText(
                    selectedCategory = selectedCategory,
                    recipientName = recipientName,
                    onNameChange = onNameChange,
                    headline = headline,
                    onHeadlineChange = onHeadlineChange,
                    customMessage = customMessage,
                    onMessageChange = onMessageChange,
                    signOff = signOff,
                    onSignOffChange = onSignOffChange,
                    selectedTemplate = selectedTemplate
                )
                3 -> StepSave(
                    onSaveCardClick = onSaveCardClick,
                    onDownloadClick = onDownloadClick,
                    onShareClick = onShareClick,
                    onHomeClick = onBackClick
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // ── Navigation Buttons ─────────────────────────────────────────────
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (currentStep > 0) {
                Button(
                    onClick = { currentStep-- },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF1E293B)),
                    shape = RoundedCornerShape(14.dp),
                    elevation = ButtonDefaults.elevation(defaultElevation = 0.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Back",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            } else if (onBackClick != null) {
                Button(
                    onClick = { onBackClick() },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF1E293B)),
                    shape = RoundedCornerShape(14.dp),
                    elevation = ButtonDefaults.elevation(defaultElevation = 0.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back to Home",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Back to Home",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            if (currentStep < totalSteps - 1) {
                Button(
                    onClick = { currentStep++ },
                    modifier = Modifier
                        .weight(if (currentStep == 0 && onBackClick == null) 1f else 2f)
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF3897F0)),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(
                        text = "Next: ${WIZARD_STEPS[currentStep + 1].emoji} ${WIZARD_STEPS[currentStep + 1].label} →",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Progress Bar
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun WizardProgressBar(currentStep: Int, steps: List<WizardStep>) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        steps.forEachIndexed { index, step ->
            val isDone = index < currentStep
            val isActive = index == currentStep

            // Step bubble
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(
                        when {
                            isDone -> Color(0xFF10B981)
                            isActive -> Color(0xFF3897F0)
                            else -> Color(0xFF1E293B)
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isDone) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                } else {
                    Text(
                        text = step.emoji,
                        fontSize = 16.sp
                    )
                }
            }

            // Label
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = step.label,
                    color = when {
                        isDone -> Color(0xFF10B981)
                        isActive -> Color.White
                        else -> Color.White.copy(alpha = 0.35f)
                    },
                    fontSize = 11.sp,
                    fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal,
                    textAlign = TextAlign.Start
                )
                // Connector bar
                if (index < steps.lastIndex) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(2.dp)
                            .clip(RoundedCornerShape(1.dp))
                            .background(
                                if (isDone) Color(0xFF10B981) else Color(0xFF1E293B)
                            )
                    )
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Step 1 — Colour & Theme
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun StepColourTheme(
    selectedCategory: CardCategory,
    onCategoryChange: (CardCategory) -> Unit,
    selectedPalette: CardColorPalette,
    onPaletteChange: (CardColorPalette) -> Unit,
    selectedTemplate: CardTemplate,
    onTemplateChange: (CardTemplate) -> Unit,
    selectedLayout: CardLayoutMode,
    onLayoutChange: (CardLayoutMode) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {

        // Occasion category selector
        SectionLabel(text = "🎉 OCCASION")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            CardCategory.entries.forEach { category ->
                val isSelected = selectedCategory == category
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(14.dp))
                        .background(
                            if (isSelected) Color(0xFF3897F0).copy(alpha = 0.25f)
                            else Color(0xFF1F1F28)
                        )
                        .border(
                            width = if (isSelected) 1.5.dp else 1.dp,
                            color = if (isSelected) Color(0xFF3897F0) else Color.White.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(14.dp)
                        )
                        .clickable { onCategoryChange(category) }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = category.icon, fontSize = 18.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = category.displayName,
                            color = if (isSelected) Color.White else Color.White.copy(alpha = 0.7f),
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }

        // Colour palette picker
        SectionLabel(text = "🎨 COLOUR PALETTE")
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CardColorPaletteCatalog.ALL_PALETTES.forEach { palette ->
                val isSelected = selectedPalette.id == palette.id
                Box(
                    modifier = Modifier
                        .width(110.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(palette.cardBackground)
                        .border(
                            width = if (isSelected) 2.5.dp else 1.dp,
                            color = if (isSelected) Color(0xFF3897F0) else Color.White.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(14.dp)
                        )
                        .clickable { onPaletteChange(palette) }
                        .padding(10.dp)
                ) {
                    Column {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(32.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(palette.gradientBrush),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = palette.name,
                            color = palette.textColor,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1
                        )
                    }
                }
            }
        }

        // Design theme
        SectionLabel(text = "✨ DESIGN THEME")
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TemplateCatalog.getTemplatesForCategory(selectedCategory).forEach { template ->
                val isSelected = selectedTemplate.id == template.id
                Box(
                    modifier = Modifier
                        .width(130.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(selectedPalette.cardBackground)
                        .border(
                            width = if (isSelected) 2.5.dp else 1.dp,
                            color = if (isSelected) Color(0xFF3897F0) else Color.White.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(14.dp)
                        )
                        .clickable { onTemplateChange(template) }
                        .padding(10.dp)
                ) {
                    Column {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(28.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(selectedPalette.gradientBrush),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = template.title,
                            color = selectedPalette.textColor,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1
                        )
                        Text(
                            text = template.subtitle,
                            color = selectedPalette.textColor.copy(alpha = 0.7f),
                            fontSize = 10.sp,
                            maxLines = 1
                        )
                    }
                }
            }
        }

        // Photo layout
        SectionLabel(text = "📐 PHOTO LAYOUT")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            CardLayoutMode.entries.forEach { mode ->
                val isSelected = selectedLayout == mode
                val icon = when (mode) {
                    CardLayoutMode.CAROUSEL -> Icons.Default.ViewCarousel
                    CardLayoutMode.THREE_PICTURE -> Icons.Default.Dashboard
                    CardLayoutMode.GRID -> Icons.Default.GridView
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            if (isSelected) Color(0xFF3897F0).copy(alpha = 0.25f)
                            else Color(0xFF1E1E26)
                        )
                        .border(
                            width = if (isSelected) 1.5.dp else 1.dp,
                            color = if (isSelected) Color(0xFF3897F0) else Color.White.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clickable { onLayoutChange(mode) }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = icon,
                            contentDescription = mode.displayName,
                            tint = if (isSelected) Color(0xFF3897F0) else Color.White.copy(alpha = 0.6f),
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = mode.displayName,
                            color = if (isSelected) Color.White else Color.White.copy(alpha = 0.6f),
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Step 2 — Photos & GIFs
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun StepPhotosGifs(
    uploadedPhotoCount: Int,
    onUploadPhotosClick: () -> Unit,
    onUploadCustomGifClick: (() -> Unit)? = null,
    selectedGifSticker: GifSticker? = null,
    onGifStickerSelect: ((GifSticker?) -> Unit)? = null,
    photoUris: List<Uri> = emptyList(),
    onRemovePhoto: ((Uri) -> Unit)? = null,
    onClearPhotos: (() -> Unit)? = null,
    placedGifCount: Int = 0,
    onClearGifs: (() -> Unit)? = null,
    onPreviousStep: (() -> Unit)? = null,
    onNextStep: (() -> Unit)? = null
) {
    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {

        // Photo upload
        Column {
            SectionLabel(text = "🖼️ YOUR PHOTOS")
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (uploadedPhotoCount == 0) "Using default studio photos" else "$uploadedPhotoCount photo(s) selected",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Up to 10 photos supported",
                        color = Color.White.copy(alpha = 0.5f),
                        fontSize = 12.sp
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (uploadedPhotoCount > 0 && onClearPhotos != null) {
                        Button(
                            onClick = onClearPhotos,
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF334155)),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Reset to Default Photos",
                                tint = Color(0xFF94A3B8),
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Reset", color = Color(0xFFE2E8F0), fontSize = 12.sp)
                        }
                    }

                    Button(
                        onClick = onUploadPhotosClick,
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF3897F0)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddPhotoAlternate,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (uploadedPhotoCount > 0) "+ Add More" else "Add Photos",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Picked Photos Thumbnail Row with individual remove (✖) buttons
            if (photoUris.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    photoUris.forEachIndexed { index, uri ->
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .border(1.dp, Color(0xFF3897F0), RoundedCornerShape(12.dp))
                        ) {
                            CardImageItem(
                                imageModel = uri,
                                index = index,
                                modifier = Modifier.fillMaxSize()
                            )
                            // Delete/Remove photo button
                            if (onRemovePhoto != null) {
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(4.dp)
                                        .size(20.dp)
                                        .clip(CircleShape)
                                        .background(Color.Black.copy(alpha = 0.75f))
                                        .clickable { onRemovePhoto(uri) },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Remove Photo",
                                        tint = Color.White,
                                        modifier = Modifier.size(12.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Quick Navigation Row inside Step 1 after picking photos/stickers
        if (onPreviousStep != null && onNextStep != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = onPreviousStep,
                    modifier = Modifier
                        .weight(1f)
                        .height(42.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF1E293B)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back to Theme",
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("← Theme", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }

                Button(
                    onClick = onNextStep,
                    modifier = Modifier
                        .weight(1.5f)
                        .height(42.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF3897F0)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Next: ✍️ Text →", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Step 3 — Text & Message
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun StepText(
    selectedCategory: CardCategory,
    recipientName: String,
    onNameChange: (String) -> Unit,
    headline: String,
    onHeadlineChange: (String) -> Unit,
    customMessage: String,
    onMessageChange: (String) -> Unit,
    signOff: String,
    onSignOffChange: (String) -> Unit,
    selectedTemplate: CardTemplate
) {
    val suggestions = if (selectedCategory == CardCategory.BIRTHDAY) birthdayMessageSuggestions
    else congratsMessageSuggestions

    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {

        OutlinedTextField(
            value = recipientName,
            onValueChange = onNameChange,
            label = { Text("Recipient Name") },
            placeholder = { Text("e.g. Jessica") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = studioTextFieldColors(),
            shape = RoundedCornerShape(12.dp)
        )

        OutlinedTextField(
            value = headline,
            onValueChange = onHeadlineChange,
            label = { Text("Headline / Banner") },
            placeholder = { Text(selectedTemplate.headline) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = studioTextFieldColors(),
            shape = RoundedCornerShape(12.dp)
        )

        OutlinedTextField(
            value = customMessage,
            onValueChange = onMessageChange,
            label = { Text("Personal Message") },
            placeholder = { Text("Write your warm wishes here...") },
            maxLines = 4,
            modifier = Modifier.fillMaxWidth(),
            colors = studioTextFieldColors(),
            shape = RoundedCornerShape(12.dp)
        )

        // Quick inspiration chips
        Text(
            text = "Quick Inspiration (tap to apply):",
            color = Color.White.copy(alpha = 0.5f),
            fontSize = 11.sp
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            suggestions.forEach { chip ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFF22222E))
                        .border(0.8.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(14.dp))
                        .clickable { onMessageChange(chip) }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = chip.take(34) + "…",
                        color = Color(0xFF90CAF9),
                        fontSize = 11.sp,
                        maxLines = 1
                    )
                }
            }
        }

        OutlinedTextField(
            value = signOff,
            onValueChange = onSignOffChange,
            label = { Text("Sign-Off / From") },
            placeholder = { Text("e.g. With love, Sarah ❤️") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = studioTextFieldColors(),
            shape = RoundedCornerShape(12.dp)
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Step 4 — Save, Download & Share
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun StepSave(
    onSaveCardClick: () -> Unit,
    onDownloadClick: () -> Unit,
    onShareClick: () -> Unit,
    onHomeClick: (() -> Unit)? = null
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "🎉 Your card is ready!",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "Save it to your collection, download to your gallery, or share it directly.",
            color = Color.White.copy(alpha = 0.6f),
            fontSize = 13.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Save to collection
        Button(
            onClick = onSaveCardClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF10B981)),
            shape = RoundedCornerShape(14.dp)
        ) {
            Icon(
                imageVector = Icons.Default.BookmarkAdd,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "Save to My Collection",
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Download to gallery
        Button(
            onClick = onDownloadClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF3897F0)),
            shape = RoundedCornerShape(14.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Download,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "Download to Gallery",
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Share
        Button(
            onClick = onShareClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF8B5CF6)),
            shape = RoundedCornerShape(14.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Share,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "Share Card",
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }

        if (onHomeClick != null) {
            Spacer(modifier = Modifier.height(4.dp))
            Button(
                onClick = onHomeClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF1E293B)),
                shape = RoundedCornerShape(14.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Return to Home",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Helpers
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text,
        color = Color.White.copy(alpha = 0.5f),
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 0.8.sp
    )
}

@Composable
private fun studioTextFieldColors() = TextFieldDefaults.outlinedTextFieldColors(
    textColor = Color.White,
    focusedBorderColor = Color(0xFF3897F0),
    unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
    focusedLabelColor = Color(0xFF3897F0),
    unfocusedLabelColor = Color.White.copy(alpha = 0.6f),
    cursorColor = Color(0xFF3897F0)
)
