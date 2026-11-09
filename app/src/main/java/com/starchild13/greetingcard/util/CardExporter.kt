/**
 * Export and sharing utilities for Card Studio.
 *
 * Provides async bitmap export capabilities to device gallery using Android MediaStore
 * and sharing of generated card images using FileProvider and system intents.
 */
package com.starchild13.greetingcard.util

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.content.FileProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

/**
 * Utility object handling asynchronous card image export to device gallery
 * and system sharing using Kotlin Coroutines and Dispatchers.IO.
 */
object CardExporter {

    /**
     * Saves card bitmap to device Pictures gallery asynchronously on Dispatchers.IO via MediaStore.
     *
     * Handles MediaStore content values preparation, stream writing, and API 29+ IS_PENDING flags.
     *
     * @param context Application context for ContentResolver access.
     * @param bitmap Rendered greeting card [Bitmap] to save.
     * @return Saved image [Uri] or null if saving fails.
     */
    suspend fun saveCardToGallery(context: Context, bitmap: Bitmap): Uri? = withContext(Dispatchers.IO) {
        // Step 1: Prepare MediaStore content values for PNG image
        val filename = "GreetingCard_${System.currentTimeMillis()}.png"
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.MediaColumns.RELATIVE_PATH, "${Environment.DIRECTORY_PICTURES}/GreetingCards")
                put(MediaStore.MediaColumns.IS_PENDING, 1)
            }
        }

        // Step 2: Insert into MediaStore and write PNG compressed bitmap stream
        val resolver = context.contentResolver
        val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        if (uri != null) {
            resolver.openOutputStream(uri)?.use { stream ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            }
            // Step 3: Clear IS_PENDING flag on Android Q (API 29) and higher
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                contentValues.clear()
                contentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
                resolver.update(uri, contentValues, null, null)
            }
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Card saved to Pictures/GreetingCards! 📸🎉", Toast.LENGTH_LONG).show()
            }
        } else {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Failed to save greeting card", Toast.LENGTH_SHORT).show()
            }
        }
        uri
    }

    /**
     * Shares card bitmap via system share sheet asynchronously on Dispatchers.IO using FileProvider.
     *
     * Saves bitmap to local cache directory, retrieves content Uri via FileProvider, and launches share chooser intent.
     *
     * @param context Context used for launching share chooser activity.
     * @param bitmap Rendered greeting card [Bitmap] to share.
     */
    suspend fun shareCard(context: Context, bitmap: Bitmap) = withContext(Dispatchers.IO) {
        try {
            // Step 1: Save PNG bitmap to application cache directory
            val cachePath = File(context.cacheDir, "shared_cards")
            cachePath.mkdirs()
            val file = File(cachePath, "greeting_card.png")
            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            }

            // Step 2: Generate content URI via androidx FileProvider
            val contentUri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )

            // Step 3: Construct ACTION_SEND intent with READ permission flag
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "image/png"
                putExtra(Intent.EXTRA_STREAM, contentUri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            // Step 4: Launch system share chooser on Main thread
            withContext(Dispatchers.Main) {
                context.startActivity(Intent.createChooser(shareIntent, "Share Greeting Card"))
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Error sharing greeting card: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
