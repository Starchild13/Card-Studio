/**
 * Card Controls and Studio Customizer components for Card Studio.
 *
 * Provides tabbed editor controls (Fill Fields, Designs & Styles, Photos & GIFs),
 * occasion category selection, color palette pickers, template themes, layout modes,
 * image/GIF upload triggers, and card persistence actions.
 */
package com.starchild13.greetingcard.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Palette
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
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

/**
 * Studio Customizer Tabs enum representing available editor sections.
 *
 * @property label User-facing tab label text.
 * @property icon Tab indicator icon vector.
 */
enum class StudioCustomizerTab(val label: String, val icon: ImageVector) {
    FIELDS("Fill Fields", Icons.Default.Edit),
    DESIGNS("Designs & Styles", Icons.Default.Palette),
    PHOTOS("Photos", Icons.Default.AddPhotoAlternate)
}

/**
 * Inspiration message suggestion chips for quick autofill on Birthday cards.
 */
private val birthdayMessageSuggestions = listOf(
    "🎂 Wishing you a year ahead filled with laughter, love, and endless happiness!",
    "🌟 May all your biggest dreams and wishes turn into reality this year!",
    "🎉 Another year wiser, bolder, and more wonderful! Keep shining bright!",
    "💖 Celebrating the incredible person you are today and always! Have the best birthday!"
)

/**
 * Inspiration message suggestion chips for quick autofill on Congratulations cards.
 */
private val congratsMessageSuggestions = listOf(
    "🎓 Cap tossed, degree earned! So tremendously proud of your hard work and brilliance!",
    "🚀 Huge congratulations on your career level up! The sky is your only limit!",
    "🥂 Hard work pays off and excellence gets noticed! Celebrating your major triumph!",
    "🌟 What an extraordinary achievement! Wishing you continued success on your next chapter!"
)

/**
 * Card Studio Customizer Panel.
 *
 * Allows users to fill in card fields, select curated visual designs and color palettes,
 * add photos, and save the customized card to Room SQLite database.
 */
@Composable
fun CardControls(
    recipientName: String,
    onNameChange: (String) -> Unit,
    headline: String,
    onHeadlineChange: (String) -> Unit,
    customMessage: String,
    onMessageChange: (String) -> Unit,
    signOff: String,
    onSignOffChange: (String) -> Unit,
    selectedCategory: CardCategory,
    onCategoryChange: (CardCategory) -> Unit,
    selectedTemplate: CardTemplate,
    onTemplateChange: (CardTemplate) -> Unit,
    selectedPalette: CardColorPalette,
    onPaletteChange: (CardColorPalette) -> Unit,
    selectedLayout: CardLayoutMode,
    onLayoutChange: (CardLayoutMode) -> Unit,
    selectedGifSticker: GifSticker? = null,
    onGifStickerSelect: ((GifSticker?) -> Unit)? = null,
    uploadedPhotoCount: Int = 0,
    onUploadPhotosClick: () -> Unit = {},
    onUploadCustomGifClick: (() -> Unit)? = null,
    useSamplePhotos: Boolean = true,
    onToggleSamplePhotos: (Boolean) -> Unit = {},
    onSaveCardClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var activeTabIndex by remember { mutableIntStateOf(0) }
    val tabs = StudioCustomizerTab.entries

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .background(Color(0xFF141419))
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        // Section 1: Occasion Category Selector Pills (🎂 Birthday vs 🎉 Congratulations)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            CardCategory.entries.forEach { category ->
                val isSelected = selectedCategory == category
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            if (isSelected) Color(0xFF3897F0).copy(alpha = 0.25f)
                            else Color(0xFF1F1F28)
                        )
                        .border(
                            width = if (isSelected) 1.5.dp else 1.dp,
                            color = if (isSelected) Color(0xFF3897F0) else Color.White.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clickable { onCategoryChange(category) }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = category.icon, fontSize = 16.sp)
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

        Spacer(modifier = Modifier.height(14.dp))

        // Section 2: Customizer Tabs Header (Fill Fields, Designs & Styles, Photos & GIFs)
        ScrollableTabRow(
            selectedTabIndex = activeTabIndex,
            backgroundColor = Color.Transparent,
            contentColor = Color(0xFF3897F0),
            edgePadding = 0.dp,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[activeTabIndex]),
                    height = 3.dp,
                    color = Color(0xFF3897F0)
                )
            }
        ) {
            tabs.forEachIndexed { index, tab ->
                Tab(
                    selected = activeTabIndex == index,
                    onClick = { activeTabIndex = index },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = tab.icon,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = tab.label,
                                fontSize = 12.sp,
                                fontWeight = if (activeTabIndex == index) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    },
                    selectedContentColor = Color.White,
                    unselectedContentColor = Color.White.copy(alpha = 0.5f)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Section 3: Tab Content Switcher
        when (tabs[activeTabIndex]) {
            // TAB 0: FILL FIELDS (Recipient Name, Headline, Greeting Message Body, Inspiration Chips, Sign-Off)
            StudioCustomizerTab.FIELDS -> {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    // 1. Recipient Name Input Field
                    OutlinedTextField(
                        value = recipientName,
                        onValueChange = onNameChange,
                        label = { Text("Recipient Name") },
                        placeholder = { Text("e.g. Jessica") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            textColor = Color.White,
                            focusedBorderColor = Color(0xFF3897F0),
                            unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
                            focusedLabelColor = Color(0xFF3897F0),
                            unfocusedLabelColor = Color.White.copy(alpha = 0.6f),
                            cursorColor = Color(0xFF3897F0)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    // 2. Headline / Banner Greeting Title Field
                    OutlinedTextField(
                        value = headline,
                        onValueChange = onHeadlineChange,
                        label = { Text("Celebratory Headline / Title") },
                        placeholder = { Text(selectedTemplate.headline) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            textColor = Color.White,
                            focusedBorderColor = Color(0xFF3897F0),
                            unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
                            focusedLabelColor = Color(0xFF3897F0),
                            unfocusedLabelColor = Color.White.copy(alpha = 0.6f),
                            cursorColor = Color(0xFF3897F0)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    // 3. Heartfelt Message Body Field
                    OutlinedTextField(
                        value = customMessage,
                        onValueChange = onMessageChange,
                        label = { Text("Personal Greeting Message") },
                        placeholder = { Text("Write your warm wishes here...") },
                        maxLines = 4,
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            textColor = Color.White,
                            focusedBorderColor = Color(0xFF3897F0),
                            unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
                            focusedLabelColor = Color(0xFF3897F0),
                            unfocusedLabelColor = Color.White.copy(alpha = 0.6f),
                            cursorColor = Color(0xFF3897F0)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    // Quick Suggestion Chips for Message Body
                    Text(
                        text = "Quick Inspiration Chips (Tap to apply):",
                        color = Color.White.copy(alpha = 0.6f),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )

                    val suggestions = if (selectedCategory == CardCategory.BIRTHDAY) {
                        birthdayMessageSuggestions
                    } else {
                        congratsMessageSuggestions
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        suggestions.forEach { chipText ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(Color(0xFF22222E))
                                    .border(0.8.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(14.dp))
                                    .clickable { onMessageChange(chipText) }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = chipText.take(34) + "...",
                                    color = Color(0xFF90CAF9),
                                    fontSize = 11.sp,
                                    maxLines = 1
                                )
                            }
                        }
                    }

                    // 4. Closing Sign-Off Field
                    OutlinedTextField(
                        value = signOff,
                        onValueChange = onSignOffChange,
                        label = { Text("Closing Sign-Off / From") },
                        placeholder = { Text("e.g. With love, Sarah ❤️") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            textColor = Color.White,
                            focusedBorderColor = Color(0xFF3897F0),
                            unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
                            focusedLabelColor = Color(0xFF3897F0),
                            unfocusedLabelColor = Color.White.copy(alpha = 0.6f),
                            cursorColor = Color(0xFF3897F0)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }

            // TAB 1: DESIGNS & STYLES (Color Palette Picker, Design Theme Selector, Photo Layout Mode Selector)
            StudioCustomizerTab.DESIGNS -> {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    // 1. Color Palette Selector (Shades of blue, grey, purple, amethysts, etc.)
                    Column {
                        Text(
                            text = "CHOOSE COLOUR PALETTE (SHADES OF BLUE, GREY, PURPLE)",
                            color = Color.White.copy(alpha = 0.5f),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.8.sp
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        val palettes = CardColorPaletteCatalog.ALL_PALETTES
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            palettes.forEach { palette ->
                                val isSelected = selectedPalette.id == palette.id
                                Box(
                                    modifier = Modifier
                                        .width(120.dp)
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
                                                .height(28.dp)
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(palette.gradientBrush),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            if (isSelected) {
                                                Icon(
                                                    imageVector = Icons.Default.Check,
                                                    contentDescription = "Selected",
                                                    tint = Color.White,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = palette.name,
                                            color = palette.textColor,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            maxLines = 1
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // 2. Design Themes Selector
                    Column {
                        Text(
                            text = "CHOOSE DESIGN THEME & HEADLINE",
                            color = Color.White.copy(alpha = 0.5f),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.8.sp
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        val templates = TemplateCatalog.getTemplatesForCategory(selectedCategory)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            templates.forEach { template ->
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
                                        .clickable {
                                            onTemplateChange(template)
                                        }
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
                                                    contentDescription = "Selected",
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
                    }

                    // 3. Layout Mode Selector (Instagram Carousel, 3-Pic Collage, 2x2 Grid)
                    Column {
                        Text(
                            text = "PHOTO ARRANGEMENT (INSTAGRAM & PINTEREST STYLES)",
                            color = Color.White.copy(alpha = 0.5f),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.8.sp
                        )
                        Spacer(modifier = Modifier.height(10.dp))

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
                                            tint = if (isSelected) Color(0xFF3897F0) else Color.White.copy(alpha = 0.7f),
                                            modifier = Modifier.size(22.dp)
                                        )
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(
                                            text = mode.displayName,
                                            color = if (isSelected) Color.White else Color.White.copy(alpha = 0.7f),
                                            fontSize = 11.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // TAB 2: PHOTOS & GIF STICKERS (Photo Uploader & Animated GIF Picker)
            StudioCustomizerTab.PHOTOS -> {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    // Photo Upload Trigger Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "PHOTO COLLECTION (${uploadedPhotoCount} uploaded)",
                                color = Color.White.copy(alpha = 0.5f),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.8.sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = if (uploadedPhotoCount == 0) "Showing default studio photos" else "Using your personal photos",
                                color = Color.White.copy(alpha = 0.7f),
                                fontSize = 12.sp
                            )
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
                                text = "Add Photos",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Section 4: Primary Action Button - Save Card to Room Database
        Button(
            onClick = onSaveCardClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF10B981)),
            shape = RoundedCornerShape(14.dp),
            elevation = ButtonDefaults.elevation(defaultElevation = 4.dp)
        ) {
            Icon(
                imageVector = Icons.Default.BookmarkAdd,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Save Card to My Collection",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
