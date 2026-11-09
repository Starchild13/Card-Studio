/**
 * Data model definitions and catalogs for Card Studio.
 *
 * Defines domain classes, template catalogs, color palette options, layout modes,
 * GIF sticker models, and state containers used across the app.
 */
package com.starchild13.greetingcard.model

import android.net.Uri
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.starchild13.greetingcard.R

/**
 * High-level category classification for greeting cards.
 *
 * Specifically supports Birthday and Congratulations categories,
 * each with tailored templates, icons, and themes.
 *
 * @property displayName Label shown in category selection chips.
 * @property subtitle Short description explaining the occasion category.
 * @property icon Emoji symbol visually representing the category.
 */
enum class CardCategory(
    val displayName: String,
    val subtitle: String,
    val icon: String
) {
    BIRTHDAY(
        displayName = "Birthday",
        subtitle = "Celebrate another fabulous trip around the sun",
        icon = "🎂"
    ),
    CONGRATULATIONS(
        displayName = "Congratulations",
        subtitle = "Celebrate graduations, promotions, and major milestones",
        icon = "🎉"
    )
}

/**
 * Layout arrangements inspired by Instagram posts and Pinterest boards.
 *
 * @property displayName Human-readable name for layout selector.
 * @property description Subtitle explaining the visual layout.
 */
enum class CardLayoutMode(
    val displayName: String,
    val description: String
) {
    CAROUSEL(
        displayName = "Carousel",
        description = "Swipeable Instagram-style post with pagination dots"
    ),
    THREE_PICTURE(
        displayName = "3-Picture",
        description = "Pinterest-inspired asymmetrical collage (hero + stacked pins)"
    ),
    GRID(
        displayName = "Grid",
        description = "Sleek photo grid with equal aspect ratios and rounded borders"
    )
}

/**
 * Custom color palette allowing users to choose dynamic shades of blue, grey, purple, and more for cards.
 *
 * @property id Unique string identifier for persistent storage.
 * @property name Human-readable title of the color palette.
 * @property cardBackground Outer card container background color.
 * @property surfaceColor Inner message container surface background color.
 * @property textColor Primary text color for body messages and titles.
 * @property accentColor High-contrast accent color for highlights and badges.
 * @property gradientBrush Linear gradient brush used for headers and highlights.
 * @property storyRingBrush Sweep gradient brush used for circular avatar rings.
 */
data class CardColorPalette(
    val id: String,
    val name: String,
    val cardBackground: Color,
    val surfaceColor: Color,
    val textColor: Color,
    val accentColor: Color,
    val gradientBrush: Brush,
    val storyRingBrush: Brush
)

/**
 * Catalog of curated custom color palettes featuring shades of blue, grey, and purple.
 */
object CardColorPaletteCatalog {
    /** Purple Nebula palette featuring deep violet and neon purple tones. */
    val PURPLE_NEBULA = CardColorPalette(
        id = "palette_purple_nebula",
        name = "Purple Nebula",
        cardBackground = Color(0xFF0F071E),
        surfaceColor = Color(0xFF1D1038),
        textColor = Color(0xFFF3E8FF),
        accentColor = Color(0xFFC084FC),
        gradientBrush = Brush.linearGradient(listOf(Color(0xFF4C1D95), Color(0xFF7C3AED), Color(0xFFC084FC), Color(0xFFEC4899))),
        storyRingBrush = Brush.sweepGradient(listOf(Color(0xFFC084FC), Color(0xFF7C3AED), Color(0xFFEC4899), Color(0xFFC084FC)))
    )

    /** Midnight Sapphire palette featuring ocean blue and cyan glow tones. */
    val MIDNIGHT_SAPPHIRE = CardColorPalette(
        id = "palette_midnight_sapphire",
        name = "Midnight Sapphire",
        cardBackground = Color(0xFF070D1A),
        surfaceColor = Color(0xFF0F1A30),
        textColor = Color(0xFFE0F2FE),
        accentColor = Color(0xFF38BDF8),
        gradientBrush = Brush.linearGradient(listOf(Color(0xFF1E3A8A), Color(0xFF0284C7), Color(0xFF38BDF8), Color(0xFF06B6D4))),
        storyRingBrush = Brush.sweepGradient(listOf(Color(0xFF38BDF8), Color(0xFF0284C7), Color(0xFF1E3A8A), Color(0xFF38BDF8)))
    )

    /** Gunmetal Chic palette featuring slate dark grey and purple accents. */
    val GUNMETAL_CHIC = CardColorPalette(
        id = "palette_gunmetal_chic",
        name = "Gunmetal Chic",
        cardBackground = Color(0xFF111318),
        surfaceColor = Color(0xFF1C202B),
        textColor = Color(0xFFF1F5F9),
        accentColor = Color(0xFFA855F7),
        gradientBrush = Brush.linearGradient(listOf(Color(0xFF334155), Color(0xFF475569), Color(0xFF8B5CF6), Color(0xFF6366F1))),
        storyRingBrush = Brush.sweepGradient(listOf(Color(0xFF8B5CF6), Color(0xFF64748B), Color(0xFF334155), Color(0xFF8B5CF6)))
    )

    /** Velvet Violet palette featuring deep violet and magenta highlights. */
    val VELVET_VIOLET = CardColorPalette(
        id = "palette_velvet_violet",
        name = "Velvet Violet",
        cardBackground = Color(0xFF180A28),
        surfaceColor = Color(0xFF2E124A),
        textColor = Color(0xFFFAF5FF),
        accentColor = Color(0xFFE879F9),
        gradientBrush = Brush.linearGradient(listOf(Color(0xFF581C87), Color(0xFF9333EA), Color(0xFFE879F9))),
        storyRingBrush = Brush.sweepGradient(listOf(Color(0xFFE879F9), Color(0xFF9333EA), Color(0xFF581C87), Color(0xFFE879F9)))
    )

    /** Backward-compatible reference to VELVET_VIOLET. */
    val ROYAL_AMETHYST = VELVET_VIOLET

    /** Slate Steel palette featuring dark slate blue and sky blue highlights. */
    val SLATE_STEEL = CardColorPalette(
        id = "palette_slate_steel",
        name = "Slate Steel",
        cardBackground = Color(0xFF0F172A),
        surfaceColor = Color(0xFF1E293B),
        textColor = Color(0xFFF8FAFC),
        accentColor = Color(0xFF38BDF8),
        gradientBrush = Brush.linearGradient(listOf(Color(0xFF0F172A), Color(0xFF334155), Color(0xFF0EA5E9))),
        storyRingBrush = Brush.sweepGradient(listOf(Color(0xFF0EA5E9), Color(0xFF334155), Color(0xFF0F172A), Color(0xFF0EA5E9)))
    )

    /** Ocean Abyss palette featuring deep teal blue and cyan tones. */
    val OCEAN_ABYSS = CardColorPalette(
        id = "palette_ocean_abyss",
        name = "Ocean Abyss",
        cardBackground = Color(0xFF041E26),
        surfaceColor = Color(0xFF0A3642),
        textColor = Color(0xFFECFEFF),
        accentColor = Color(0xFF22D3EE),
        gradientBrush = Brush.linearGradient(listOf(Color(0xFF0891B2), Color(0xFF06B6D4), Color(0xFF2DD4BF))),
        storyRingBrush = Brush.sweepGradient(listOf(Color(0xFF2DD4BF), Color(0xFF06B6D4), Color(0xFF0891B2), Color(0xFF2DD4BF)))
    )

    /** List of all available color palettes. */
    val ALL_PALETTES = listOf(
        PURPLE_NEBULA,
        MIDNIGHT_SAPPHIRE,
        GUNMETAL_CHIC,
        ROYAL_AMETHYST,
        SLATE_STEEL,
        OCEAN_ABYSS
    )

    /**
     * Looks up a [CardColorPalette] by its unique string ID.
     *
     * @param id The palette identifier.
     * @return Matching [CardColorPalette] or fallback [PURPLE_NEBULA].
     */
    fun findById(id: String): CardColorPalette {
        return ALL_PALETTES.find { it.id == id } ?: PURPLE_NEBULA
    }
}

/**
 * Predefined visual template defining color palettes, typography,
 * celebratory tags, and suggested greeting messages.
 *
 * @property id Unique identifier for persistent storage.
 * @property title Name of the template design.
 * @property category The [CardCategory] this template belongs to.
 * @property headline Celebratory banner headline (e.g. "HAPPY BIRTHDAY!" or "CONGRATS GRAD!").
 * @property tagText Themed sticker tag displayed at the top of the card headline.
 * @property defaultMessage Recommended heartfelt greeting message for this template.
 * @property cardBackground Main background color for the card container.
 * @property surfaceColor Accent surface color for headline banners and badges.
 * @property textColor Primary readable text color.
 * @property accentColor High-contrast accent color for badges and highlights.
 * @property gradientBrush Linear gradient used on banners and badges.
 * @property storyRingBrush Sweep gradient used for avatar celebration rings.
 * @property fontFamily Selected typography font family.
 * @property fontWeight Font weight for bold thematic statements.
 * @property subtitle Short description subtitle (defaults to [tagText]).
 */
data class CardTemplate(
    val id: String,
    val title: String,
    val category: CardCategory,
    val headline: String,
    val tagText: String,
    val defaultMessage: String,
    val cardBackground: Color,
    val surfaceColor: Color,
    val textColor: Color,
    val accentColor: Color,
    val gradientBrush: Brush,
    val storyRingBrush: Brush,
    val fontFamily: FontFamily,
    val fontWeight: FontWeight,
    val subtitle: String = tagText
)

/**
 * Curated template catalog for Birthday and Congratulations cards.
 */
object TemplateCatalog {

    // ==========================================
    // BIRTHDAY TEMPLATES
    // ==========================================

    val BIRTHDAY_VIBRANT = CardTemplate(
        id = "bday_vibrant",
        title = "Insta Glow",
        category = CardCategory.BIRTHDAY,
        headline = "HAPPY BIRTHDAY!",
        tagText = "🔥 CELEBRATING THE ICON 🔥",
        defaultMessage = "Another year older, wiser, and more fabulous! May this year bring endless joy, laughter, and wild success! 🥂✨",
        cardBackground = Color(0xFF121212),
        surfaceColor = Color(0xFF1E1E1E),
        textColor = Color.White,
        accentColor = Color(0xFFFD1D1D),
        gradientBrush = Brush.linearGradient(
            listOf(Color(0xFF833AB4), Color(0xFFFD1D1D), Color(0xFFFCB045))
        ),
        storyRingBrush = Brush.sweepGradient(
            listOf(Color(0xFF833AB4), Color(0xFFFD1D1D), Color(0xFFFCB045), Color(0xFF833AB4))
        ),
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Black
    )

    val BIRTHDAY_GOLDEN = CardTemplate(
        id = "bday_golden",
        title = "Elegant Gold",
        category = CardCategory.BIRTHDAY,
        headline = "Happy Birthday",
        tagText = "✨ GOLDEN CELEBRATION ✨",
        defaultMessage = "Cheers to another remarkable year of grace, elegance, and extraordinary accomplishments! May all your dreams unfold! 🌟",
        cardBackground = Color(0xFF0F0F14),
        surfaceColor = Color(0xFF1C1C24),
        textColor = Color(0xFFF9F6EE),
        accentColor = Color(0xFFE5C158),
        gradientBrush = Brush.linearGradient(
            listOf(Color(0xFFFFDF73), Color(0xFFD4AF37), Color(0xFFAA771C))
        ),
        storyRingBrush = Brush.sweepGradient(
            listOf(Color(0xFFFFDF73), Color(0xFFD4AF37), Color(0xFFAA771C), Color(0xFFFFDF73))
        ),
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.Bold
    )

    val BIRTHDAY_SWEET_PASTEL = CardTemplate(
        id = "bday_sweet_pastel",
        title = "Sweet Pastel",
        category = CardCategory.BIRTHDAY,
        headline = "Happy Birthday Sweetheart!",
        tagText = "🎂 SWEETEST WISHES 🎂",
        defaultMessage = "Wishing you a birthday filled with sweet moments, radiant smiles, and everything that makes your heart smile! 💖",
        cardBackground = Color(0xFFFFF7F9),
        surfaceColor = Color(0xFFFFE8EF),
        textColor = Color(0xFF6B21A8),
        accentColor = Color(0xFFEC4899),
        gradientBrush = Brush.linearGradient(
            listOf(Color(0xFFC084FC), Color(0xFFF472B6), Color(0xFFFDE047))
        ),
        storyRingBrush = Brush.sweepGradient(
            listOf(Color(0xFFC084FC), Color(0xFFF472B6), Color(0xFFFDE047), Color(0xFFC084FC))
        ),
        fontFamily = FontFamily.Cursive,
        fontWeight = FontWeight.Normal
    )

    val BIRTHDAY_RETRO = CardTemplate(
        id = "bday_retro",
        title = "Retro Memories",
        category = CardCategory.BIRTHDAY,
        headline = "Happy Birthday to You!",
        tagText = "🎞️ NOSTALGIC MOMENTS 🎞️",
        defaultMessage = "Here's to celebrating the timeless classic that is you. Keep shining brightly today and through all the years ahead! 📸",
        cardBackground = Color(0xFFFBF7F0),
        surfaceColor = Color(0xFFEDE5D8),
        textColor = Color(0xFF2C221E),
        accentColor = Color(0xFFE76F51),
        gradientBrush = Brush.linearGradient(
            listOf(Color(0xFFF4A261), Color(0xFFE76F51), Color(0xFF264653))
        ),
        storyRingBrush = Brush.sweepGradient(
            listOf(Color(0xFFE76F51), Color(0xFFF4A261), Color(0xFF2A9D8F), Color(0xFFE76F51))
        ),
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.SemiBold
    )

    // ==========================================
    // CONGRATULATIONS TEMPLATES
    // ==========================================

    val CONGRATS_GRADUATION = CardTemplate(
        id = "congrats_graduation",
        title = "Graduation",
        category = CardCategory.CONGRATULATIONS,
        headline = "CONGRATS GRAD!",
        tagText = "🎓 CLASS OF TRIUMPH • YOU DID IT!",
        defaultMessage = "Cap tossed, degree earned, future unlocked! So incredibly proud of your relentless dedication and brilliance! The world awaits you! 🎓🚀",
        cardBackground = Color(0xFF0A1128),
        surfaceColor = Color(0xFF101F42),
        textColor = Color.White,
        accentColor = Color(0xFFFFD700),
        gradientBrush = Brush.linearGradient(
            listOf(Color(0xFF001F54), Color(0xFF034078), Color(0xFFFFD700))
        ),
        storyRingBrush = Brush.sweepGradient(
            listOf(Color(0xFFFFD700), Color(0xFF0077B6), Color(0xFFFFD700), Color(0xFF0077B6))
        ),
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.ExtraBold
    )

    val CONGRATS_CAREER = CardTemplate(
        id = "congrats_career",
        title = "Promotion",
        category = CardCategory.CONGRATULATIONS,
        headline = "MAJOR PROMOTION!",
        tagText = "💼 CAREER LEVEL UP • SO PROUD! 💼",
        defaultMessage = "Hard work pays off and brilliance gets recognized! Massive congratulations on your well-deserved promotion and new leadership chapter! 🌟",
        cardBackground = Color(0xFF051C14),
        surfaceColor = Color(0xFF0B2E21),
        textColor = Color(0xFFE8F5E9),
        accentColor = Color(0xFF4ADE80),
        gradientBrush = Brush.linearGradient(
            listOf(Color(0xFF10B981), Color(0xFF059669), Color(0xFF34D399))
        ),
        storyRingBrush = Brush.sweepGradient(
            listOf(Color(0xFF4ADE80), Color(0xFF10B981), Color(0xFF047857), Color(0xFF4ADE80))
        ),
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold
    )

    val CONGRATS_WEDDING = CardTemplate(
        id = "congrats_wedding",
        title = "Wedding & Love",
        category = CardCategory.CONGRATULATIONS,
        headline = "Happily Ever After!",
        tagText = "💍 LOVE & COMMITMENT 💍",
        defaultMessage = "May your shared journey be blessed with unconditional love, infectious laughter, and an endless lifetime of magical memories together! 🥂",
        cardBackground = Color(0xFFFFF0F5),
        surfaceColor = Color(0xFFFFDFEA),
        textColor = Color(0xFF831843),
        accentColor = Color(0xFFDB2777),
        gradientBrush = Brush.linearGradient(
            listOf(Color(0xFFF472B6), Color(0xFFFB7185), Color(0xFFFDA4AF))
        ),
        storyRingBrush = Brush.sweepGradient(
            listOf(Color(0xFFF472B6), Color(0xFFFDA4AF), Color(0xFFF43F5E), Color(0xFFF472B6))
        ),
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.SemiBold
    )

    val CONGRATS_VICTORY = CardTemplate(
        id = "congrats_victory",
        title = "Big Win",
        category = CardCategory.CONGRATULATIONS,
        headline = "VICTORY & TRIUMPH!",
        tagText = "🏆 CHAMPION • YOU DID IT! 🏆",
        defaultMessage = "You faced the challenge, gave it everything, and conquered the summit! Standing ovation for a truly legendary achievement! 🥇",
        cardBackground = Color(0xFF1A0B00),
        surfaceColor = Color(0xFF2E1705),
        textColor = Color(0xFFFFF7ED),
        accentColor = Color(0xFFF97316),
        gradientBrush = Brush.linearGradient(
            listOf(Color(0xFFEA580C), Color(0xFFF59E0B), Color(0xFFFBBF24))
        ),
        storyRingBrush = Brush.sweepGradient(
            listOf(Color(0xFFF59E0B), Color(0xFFEA580C), Color(0xFFFBBF24), Color(0xFFF59E0B))
        ),
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Black
    )

    val PURPLE_NEON = CardTemplate(
        id = "purple_neon",
        title = "Purple Nebula",
        category = CardCategory.BIRTHDAY,
        headline = "NEON DREAMS",
        tagText = "💜 ELECTRIC VIBES & PURPLE SKIES 💜",
        defaultMessage = "Immersed in shades of violet, indigo, and neon purple magic! Wishing you a dazzling celebration that shines brighter than the stars! ✨💜",
        cardBackground = Color(0xFF0F071E),
        surfaceColor = Color(0xFF1D1038),
        textColor = Color(0xFFF3E8FF),
        accentColor = Color(0xFFC084FC),
        gradientBrush = Brush.linearGradient(
            listOf(Color(0xFF4C1D95), Color(0xFF7C3AED), Color(0xFFC084FC), Color(0xFFEC4899))
        ),
        storyRingBrush = Brush.sweepGradient(
            listOf(Color(0xFFC084FC), Color(0xFF7C3AED), Color(0xFFEC4899), Color(0xFFC084FC))
        ),
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold
    )

    val BLUE_MIDNIGHT = CardTemplate(
        id = "blue_midnight",
        title = "Midnight Sapphire",
        category = CardCategory.CONGRATULATIONS,
        headline = "MIDNIGHT SAPPHIRE",
        tagText = "💙 OCEAN BLUE & SLATE GLOW 💙",
        defaultMessage = "Smooth shades of blue, deep indigo, and cyan highlights. May your journey ahead be vast, peaceful, and extraordinarily successful! 🌊⭐",
        cardBackground = Color(0xFF070D1A),
        surfaceColor = Color(0xFF0F1A30),
        textColor = Color(0xFFE0F2FE),
        accentColor = Color(0xFF38BDF8),
        gradientBrush = Brush.linearGradient(
            listOf(Color(0xFF1E3A8A), Color(0xFF0284C7), Color(0xFF38BDF8), Color(0xFF06B6D4))
        ),
        storyRingBrush = Brush.sweepGradient(
            listOf(Color(0xFF38BDF8), Color(0xFF0284C7), Color(0xFF1E3A8A), Color(0xFF38BDF8))
        ),
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold
    )

    val GREY_CHIC = CardTemplate(
        id = "grey_chic",
        title = "Gunmetal Chic",
        category = CardCategory.CONGRATULATIONS,
        headline = "SLATE ELEGANCE",
        tagText = "🩶 MODERN GREY & PURPLE ACCENT 🩶",
        defaultMessage = "Sleek, sophisticated, and modern. A timeless design blending shades of grey, gunmetal slate, and subtle purple gradients. Pure style! 🍸",
        cardBackground = Color(0xFF111318),
        surfaceColor = Color(0xFF1C202B),
        textColor = Color(0xFFF1F5F9),
        accentColor = Color(0xFFA855F7),
        gradientBrush = Brush.linearGradient(
            listOf(Color(0xFF334155), Color(0xFF475569), Color(0xFF8B5CF6), Color(0xFF6366F1))
        ),
        storyRingBrush = Brush.sweepGradient(
            listOf(Color(0xFF8B5CF6), Color(0xFF64748B), Color(0xFF334155), Color(0xFF8B5CF6))
        ),
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.SemiBold
    )

    /** All templates ordered by category */
    val ALL_TEMPLATES: List<CardTemplate> = listOf(
        BIRTHDAY_VIBRANT,
        BIRTHDAY_GOLDEN,
        BIRTHDAY_SWEET_PASTEL,
        BIRTHDAY_RETRO,
        PURPLE_NEON,
        CONGRATS_GRADUATION,
        CONGRATS_CAREER,
        CONGRATS_WEDDING,
        CONGRATS_VICTORY,
        BLUE_MIDNIGHT,
        GREY_CHIC
    )

    /** Retrieves templates filtered by specific category */
    fun getTemplatesForCategory(category: CardCategory): List<CardTemplate> {
        return ALL_TEMPLATES.filter { it.category == category }
    }

    /** Finds a template by ID with safe fallback to vibrant birthday */
    fun findById(id: String): CardTemplate {
        return ALL_TEMPLATES.find { it.id == id } ?: BIRTHDAY_VIBRANT
    }
}

/**
 * Animated GIF sticker attached to cards, styled like story pins and stickers.
 *
 * @property id Unique sticker ID.
 * @property name Descriptive title.
 * @property emoji Quick visual representation.
 * @property gifUrl Web URL for the animated GIF asset.
 */
data class GifSticker(
    val id: String,
    val name: String,
    val emoji: String,
    val gifUrl: String
)

/**
 * Curated animated GIF sticker pack for celebrations.
 */
val SampleGifStickers = listOf(
    GifSticker(
        id = "party_popper",
        name = "Party Popper",
        emoji = "🎉",
        gifUrl = "https://media.giphy.com/media/26tOZ42Mg6pbTUPHW/giphy.gif"
    ),
    GifSticker(
        id = "birthday_cake",
        name = "Birthday Cake",
        emoji = "🎂",
        gifUrl = "https://media.giphy.com/media/l4KhQo2MESJkc6BLq/giphy.gif"
    ),
    GifSticker(
        id = "sparkles",
        name = "Sparkles",
        emoji = "✨",
        gifUrl = "https://media.giphy.com/media/3o7TKMt1VVNkHV2PaE/giphy.gif"
    ),
    GifSticker(
        id = "confetti_blast",
        name = "Confetti Blast",
        emoji = "🎊",
        gifUrl = "https://media.giphy.com/media/artj92V8o75VPL7AeQ/giphy.gif"
    ),
    GifSticker(
        id = "happy_birthday_neon",
        name = "Neon HBD",
        emoji = "💖",
        gifUrl = "https://media.giphy.com/media/xT0xeJpnrWC4XWblEk/giphy.gif"
    )
)

/**
 * Placed animated GIF sticker on a greeting card supporting movement (offset) and resizing (scale).
 *
 * @property id Unique instance identifier for this placed sticker.
 * @property model Source model string for the GIF asset.
 * @property name Descriptive name for the sticker.
 * @property offsetX Horizontal X-axis offset relative to initial placement position.
 * @property offsetY Vertical Y-axis offset relative to initial placement position.
 * @property scale Scaling multiplier controlling size of the sticker.
 */
data class PlacedGifSticker(
    val id: String = java.util.UUID.randomUUID().toString(),
    val model: String,
    val name: String,
    val offsetX: Float = 0f,
    val offsetY: Float = 0f,
    val scale: Float = 1.0f
) {
    companion object {
        /** Converts a list of placed stickers into a CSV-serialized string representation. */
        fun listToCsv(list: List<PlacedGifSticker>): String {
            return list.joinToString(";;") { "${it.id}~${it.model}~${it.name}~${it.offsetX}~${it.offsetY}~${it.scale}" }
        }

        /** Deserializes a CSV string representation back into a list of placed stickers. */
        fun csvToList(csv: String): List<PlacedGifSticker> {
            if (csv.isBlank()) return emptyList()
            return csv.split(";;").mapNotNull { entry ->
                val parts = entry.split("~")
                if (parts.size >= 6) {
                    PlacedGifSticker(
                        id = parts[0],
                        model = parts[1],
                        name = parts[2],
                        offsetX = parts[3].toFloatOrNull() ?: 0f,
                        offsetY = parts[4].toFloatOrNull() ?: 0f,
                        scale = parts[5].toFloatOrNull() ?: 1.0f
                    )
                } else null
            }
        }
    }
}

/**
 * Transient state for greeting card editing and creation studio.
 *
 * @property recipientName Current recipient name string.
 * @property headline Celebratory banner headline title.
 * @property customMessage Personalized greeting message body.
 * @property signOff Custom closing sign-off message.
 * @property selectedCategory Active occasion category.
 * @property selectedTemplate Active visual design template.
 * @property selectedPalette Active custom color palette.
 * @property layoutMode Active photo arrangement layout mode.
 * @property photoUris List of user-selected photo Uris.
 * @property sampleImageUrls Default studio sample images.
 * @property selectedGifSticker Active GIF sticker selected from catalog.
 * @property customGifUri Custom uploaded GIF Uri.
 * @property placedGifs List of placed moveable/resizable GIF stickers.
 * @property isLiked Heart reaction state.
 * @property likeCount Counter for celebratory likes.
 */
data class GreetingCardState(
    val recipientName: String = "Jessica",
    val headline: String = TemplateCatalog.BIRTHDAY_VIBRANT.headline,
    val customMessage: String = TemplateCatalog.BIRTHDAY_VIBRANT.defaultMessage,
    val signOff: String = "With love & warmest wishes ❤️",
    val selectedCategory: CardCategory = CardCategory.BIRTHDAY,
    val selectedTemplate: CardTemplate = TemplateCatalog.BIRTHDAY_VIBRANT,
    val selectedPalette: CardColorPalette = CardColorPaletteCatalog.PURPLE_NEBULA,
    val layoutMode: CardLayoutMode = CardLayoutMode.CAROUSEL,
    val photoUris: List<Uri> = emptyList(),
    val sampleImageUrls: List<Any> = listOf(
        R.drawable.img_0177,
        R.drawable.jessica
    ),
    val selectedGifSticker: GifSticker? = SampleGifStickers[0],
    val customGifUri: Uri? = null,
    val placedGifs: List<PlacedGifSticker> = emptyList(),
    val isLiked: Boolean = true,
    val likeCount: Int = 1842
)
