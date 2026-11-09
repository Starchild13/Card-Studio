/**
 * Entity definitions for Card Studio Room database.
 *
 * Defines SQLite table structure for storing created greeting cards.
 */
package com.starchild13.greetingcard.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room Database Entity representing a saved greeting card.
 *
 * Stores personalized details, category (Birthday or Congratulations),
 * chosen visual template, color palette, layout mode, uploaded photo/GIF URIs, and metadata.
 *
 * @property id Auto-generated primary key integer identifier.
 * @property title Card collection title (e.g. "Jessica's Birthday Card").
 * @property recipientName Recipient name text.
 * @property headline Celebratory banner headline title.
 * @property message Heartfelt greeting message body.
 * @property signOff Custom closing sign-off text (e.g., "With love, Theo").
 * @property category Occasion category string ("BIRTHDAY" or "CONGRATULATIONS").
 * @property templateId Identifier of chosen [com.starchild13.greetingcard.model.CardTemplate].
 * @property paletteId Identifier of chosen [com.starchild13.greetingcard.model.CardColorPalette].
 * @property layoutMode Layout arrangement mode string ("CAROUSEL", "THREE_PICTURE", "GRID").
 * @property photoUrisCsv CSV string of photo URIs attached to card.
 * @property gifStickerId Optional ID of single selected GIF sticker.
 * @property placedGifsCsv Serialized CSV string of placed moveable and resizable GIF stickers.
 * @property likeCount Count of celebratory heart likes on card.
 * @property createdAt Epoch timestamp (in milliseconds) when card was saved.
 */
@Entity(tableName = "greeting_cards")
data class GreetingCardEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "recipient_name")
    val recipientName: String,

    @ColumnInfo(name = "headline")
    val headline: String = "",

    @ColumnInfo(name = "message")
    val message: String,

    @ColumnInfo(name = "sign_off")
    val signOff: String = "",

    @ColumnInfo(name = "category")
    val category: String,

    @ColumnInfo(name = "template_id")
    val templateId: String,

    @ColumnInfo(name = "palette_id")
    val paletteId: String = "palette_purple_nebula",

    @ColumnInfo(name = "layout_mode")
    val layoutMode: String,

    @ColumnInfo(name = "photo_uris_csv")
    val photoUrisCsv: String = "",

    @ColumnInfo(name = "gif_sticker_id")
    val gifStickerId: String? = null,

    @ColumnInfo(name = "placed_gifs_csv")
    val placedGifsCsv: String = "",

    @ColumnInfo(name = "like_count")
    val likeCount: Int = 1842,

    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
)
