/**
 * Main Activity entry point for Card Studio application.
 *
 * Configures top-level Compose content, handles screen navigation sealed interface state,
 * initializes Room SQLite Database, and manages background coroutines.
 */
package com.starchild13.greetingcard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.starchild13.greetingcard.data.GreetingCardDatabase
import com.starchild13.greetingcard.data.GreetingCardEntity
import com.starchild13.greetingcard.ui.GreetingCardScreen
import com.starchild13.greetingcard.ui.HomeScreen
import com.starchild13.greetingcard.ui.theme.GreetingCardTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Screen state sealed interface defining top-level application navigation routes.
 */
sealed interface AppScreen {
    /** Home screen route showing saved card collection and creation triggers. */
    object Home : AppScreen
    /** Studio editor screen route for customizing and designing greeting cards. */
    object Editor : AppScreen
}

/**
 * Main Activity serving as the entry point for Card Studio.
 *
 * Hosts navigation between [HomeScreen] and [GreetingCardScreen] (Editor),
 * utilizing Kotlin Coroutines for thread and background task management.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set up Jetpack Compose layout content
        setContent {
            GreetingCardTheme {
                // Section 1: Room DB & Reactive state initialization
                val context = LocalContext.current
                val coroutineScope = rememberCoroutineScope()
                val database = remember { GreetingCardDatabase.getDatabase(context) }
                val cardDao = remember { database.greetingCardDao() }
                val savedCards by cardDao.getAllCards().collectAsState(initial = emptyList())

                // Section 2: Top-level navigation state & active editing card state
                var currentScreen by remember { mutableStateOf<AppScreen>(AppScreen.Home) }
                var editingCard by remember { mutableStateOf<GreetingCardEntity?>(null) }

                // Section 3: Screen navigation route switcher
                when (currentScreen) {
                    AppScreen.Home -> {
                        HomeScreen(
                            savedCards = savedCards,
                            onCreateNewClick = {
                                editingCard = null
                                currentScreen = AppScreen.Editor
                            },
                            onSelectCard = { card ->
                                editingCard = card
                                currentScreen = AppScreen.Editor
                            },
                            onDeleteCard = { card ->
                                coroutineScope.launch(Dispatchers.IO) {
                                    cardDao.deleteCard(card)
                                }
                            },
                            onDuplicateCard = { card ->
                                coroutineScope.launch(Dispatchers.IO) {
                                    val duplicated = GreetingCardEntity(
                                        title = "${card.title} (Copy)",
                                        recipientName = card.recipientName,
                                        headline = card.headline,
                                        message = card.message,
                                        signOff = card.signOff,
                                        category = card.category,
                                        templateId = card.templateId,
                                        paletteId = card.paletteId,
                                        layoutMode = card.layoutMode,
                                        photoUrisCsv = card.photoUrisCsv,
                                        gifStickerId = card.gifStickerId,
                                        placedGifsCsv = card.placedGifsCsv,
                                        likeCount = card.likeCount,
                                        createdAt = System.currentTimeMillis()
                                    )
                                    cardDao.insertCard(duplicated)
                                }
                            },
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    AppScreen.Editor -> {
                        GreetingCardScreen(
                            initialCard = editingCard,
                            onBackClick = { currentScreen = AppScreen.Home },
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
    }
}
