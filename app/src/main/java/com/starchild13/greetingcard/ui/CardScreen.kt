/**
 * Greeting Card Studio Editor Screen UI module.
 *
 * Provides full editor functionality including moveable and resizable animated GIF stickers,
 * photo/GIF stage arrangements (Carousel, 3-Picture Collage, 2x2 Grid), Room database persistence,
 * bitmap image export/sharing, and preview mode toggles.
 */
package com.starchild13.greetingcard.ui

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.starchild13.greetingcard.data.GreetingCardDatabase
import com.starchild13.greetingcard.data.GreetingCardEntity
import com.starchild13.greetingcard.model.CardCategory
import com.starchild13.greetingcard.model.CardColorPaletteCatalog
import com.starchild13.greetingcard.model.CardLayoutMode
import com.starchild13.greetingcard.model.GreetingCardState
import com.starchild13.greetingcard.model.TemplateCatalog
import com.starchild13.greetingcard.ui.components.CardFooter
import com.starchild13.greetingcard.ui.components.CardHeader
import com.starchild13.greetingcard.ui.components.CardWizard
import com.starchild13.greetingcard.ui.components.PhotoCarousel
import com.starchild13.greetingcard.ui.components.PhotoGrid
import com.starchild13.greetingcard.ui.components.SavedCardsSheet
import com.starchild13.greetingcard.ui.components.ThreePictureCollage
import com.starchild13.greetingcard.util.CardExporter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Card Editor Screen in Card Studio.
 *
 * Allows users to design greeting cards, add moveable and resizable GIF stickers
 * (single or combination GIFs), edit fields, and save to Room database.
 *
 * @param initialCard Optional existing card entity to load for editing.
 * @param onBackClick Action navigating back to home screen.
 * @param modifier Optional layout modifier.
 * @param initialState Default fallback studio state.
 */
@Composable
fun GreetingCardScreen(
    initialCard: GreetingCardEntity? = null,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    initialState: GreetingCardState = GreetingCardState()
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val graphicsLayer = rememberGraphicsLayer()

    // Section 1: Room Database & DAO initialization
    val database = remember { GreetingCardDatabase.getDatabase(context) }
    val cardDao = remember { database.greetingCardDao() }
    val savedCards by cardDao.getAllCards().collectAsState(initial = emptyList())

    // Section 2: Card state initialization (from initialCard if editing, else initialState)
    var recipientName by remember { mutableStateOf(initialCard?.recipientName ?: initialState.recipientName) }
    var headline by remember { mutableStateOf(initialCard?.headline?.takeIf { it.isNotBlank() } ?: initialState.headline) }
    var customMessage by remember { mutableStateOf(initialCard?.message ?: initialState.customMessage) }
    var signOff by remember { mutableStateOf(initialCard?.signOff?.takeIf { it.isNotBlank() } ?: initialState.signOff) }
    var selectedCategory by remember {
        mutableStateOf(
            try {
                if (initialCard != null) CardCategory.valueOf(initialCard.category) else initialState.selectedCategory
            } catch (_: Exception) {
                initialState.selectedCategory
            }
        )
    }
    var selectedTemplate by remember {
        mutableStateOf(
            if (initialCard != null) TemplateCatalog.findById(initialCard.templateId) else initialState.selectedTemplate
        )
    }
    var selectedPalette by remember {
        mutableStateOf(
            if (initialCard != null) CardColorPaletteCatalog.findById(initialCard.paletteId) else initialState.selectedPalette
        )
    }
    var layoutMode by remember {
        mutableStateOf(
            try {
                if (initialCard != null) CardLayoutMode.valueOf(initialCard.layoutMode) else initialState.layoutMode
            } catch (_: Exception) {
                initialState.layoutMode
            }
        )
    }
    var photoUris by remember {
        mutableStateOf(
            if (initialCard != null && initialCard.photoUrisCsv.isNotBlank()) {
                initialCard.photoUrisCsv.split(",").mapNotNull { runCatching { Uri.parse(it) }.getOrNull() }
            } else {
                initialState.photoUris
            }
        )
    }

    var isLiked by remember { mutableStateOf(initialCard == null || initialCard.likeCount > 0) }
    var likeCount by remember { mutableIntStateOf(initialCard?.likeCount ?: initialState.likeCount) }
    var showBigHeartAnimation by remember { mutableStateOf(false) }
    var showSavedCardsSheet by remember { mutableStateOf(false) }
    var isPreviewMode by remember { mutableStateOf(false) }

    // Section 4: Photo picker launcher
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(maxItems = 10)
    ) { uris ->
        if (uris.isNotEmpty()) {
            photoUris = uris
        }
    }

    // Effective images: uploaded media or default sample photos
    val effectiveImages: List<Any> = photoUris.ifEmpty {
        initialState.sampleImageUrls
    }

    val bigHeartScale by animateFloatAsState(
        targetValue = if (showBigHeartAnimation) 1.25f else 0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "bigHeartScale"
    )
    val bigHeartAlpha by animateFloatAsState(
        targetValue = if (showBigHeartAnimation) 0.95f else 0f,
        label = "bigHeartAlpha"
    )

    // Section 5: Room database saving helper method
    fun saveCardToRoom() {
        coroutineScope.launch {
            try {
                val urisCsv = photoUris.joinToString(",") { it.toString() }
                val title = when (selectedCategory) {
                    CardCategory.BIRTHDAY -> "$recipientName's Birthday Card"
                    CardCategory.CONGRATULATIONS -> "Congratulations $recipientName"
                }

                val entity = GreetingCardEntity(
                    id = initialCard?.id ?: 0,
                    title = title,
                    recipientName = recipientName,
                    headline = headline,
                    message = customMessage,
                    signOff = signOff,
                    category = selectedCategory.name,
                    templateId = selectedTemplate.id,
                    paletteId = selectedPalette.id,
                    layoutMode = layoutMode.name,
                    photoUrisCsv = urisCsv,
                    placedGifsCsv = "",
                    likeCount = likeCount
                )

                cardDao.insertCard(entity)
                Toast.makeText(context, "Saved to My Collection! 💾🎉", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(context, "Error saving: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Section 6: Gallery export helper method using GraphicsLayer capture
    fun handleDownloadCard() {
        coroutineScope.launch {
            try {
                val bitmap = graphicsLayer.toImageBitmap().asAndroidBitmap()
                CardExporter.saveCardToGallery(context, bitmap)
            } catch (e: Exception) {
                Toast.makeText(context, "Error saving card: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Section 7: System sharing helper method using FileProvider
    fun handleShareCard() {
        coroutineScope.launch {
            try {
                val bitmap = graphicsLayer.toImageBitmap().asAndroidBitmap()
                CardExporter.shareCard(context, bitmap)
            } catch (e: Exception) {
                Toast.makeText(context, "Error sharing card: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = Color(0xFF09090D)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                // Section 8: Studio Top Bar with navigation and action triggers
                StudioTopBar(
                    onBackClick = onBackClick,
                    savedCardsCount = savedCards.size,
                    onOpenSavedCards = { showSavedCardsSheet = true },
                    onSaveClick = { saveCardToRoom() },
                    isPreviewMode = isPreviewMode,
                    onPreviewToggle = { isPreviewMode = !isPreviewMode }
                )

                // Section 9: Main Greeting Card Stage
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .drawWithContent {
                                graphicsLayer.record {
                                    this@drawWithContent.drawContent()
                                }
                                drawLayer(graphicsLayer)
                            },
                        shape = RoundedCornerShape(24.dp),
                        backgroundColor = selectedPalette.cardBackground,
                        elevation = 10.dp
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(
                                    width = 1.dp,
                                    color = selectedPalette.accentColor.copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(24.dp)
                                )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 8.dp)
                            ) {
                                // 1. Header
                                CardHeader(
                                    recipientName = recipientName,
                                    template = selectedTemplate,
                                    headline = headline,
                                    palette = selectedPalette
                                )

                                // 2. Photo Stage Area
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp)
                                        .clickable(
                                            interactionSource = remember { MutableInteractionSource() },
                                            indication = null
                                        ) {
                                            if (!isLiked) {
                                                isLiked = true
                                                likeCount += 1
                                            }
                                            showBigHeartAnimation = true
                                            coroutineScope.launch {
                                                delay(1200)
                                                showBigHeartAnimation = false
                                            }
                                        }
                                ) {
                                    when (layoutMode) {
                                        CardLayoutMode.CAROUSEL -> {
                                            PhotoCarousel(
                                                images = effectiveImages,
                                                template = selectedTemplate
                                            )
                                        }
                                        CardLayoutMode.THREE_PICTURE -> {
                                            ThreePictureCollage(
                                                images = effectiveImages,
                                                template = selectedTemplate
                                            )
                                        }
                                        CardLayoutMode.GRID -> {
                                            PhotoGrid(
                                                images = effectiveImages,
                                                template = selectedTemplate
                                            )
                                        }
                                    }

                                    // Big Floating Heart Reaction Animation Overlay
                                    if (bigHeartAlpha > 0.05f) {
                                        Icon(
                                            imageVector = Icons.Filled.Favorite,
                                            contentDescription = null,
                                            tint = Color.White.copy(alpha = bigHeartAlpha),
                                            modifier = Modifier
                                                .size(100.dp)
                                                .scale(bigHeartScale)
                                                .align(Alignment.Center)
                                        )
                                    }
                                }

                                // 3. Footer
                                CardFooter(
                                    customMessage = customMessage,
                                    template = selectedTemplate,
                                    isLiked = isLiked,
                                    onLikeToggle = {
                                        if (isLiked) {
                                            isLiked = false
                                            likeCount = (likeCount - 1).coerceAtLeast(0)
                                        } else {
                                            isLiked = true
                                            likeCount += 1
                                            showBigHeartAnimation = true
                                            coroutineScope.launch {
                                                delay(1000)
                                                showBigHeartAnimation = false
                                            }
                                        }
                                    },
                                    signOff = signOff
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Stepped Card Builder Wizard
                if (!isPreviewMode) {
                    CardWizard(
                        selectedCategory = selectedCategory,
                        onCategoryChange = { category ->
                            selectedCategory = category
                            val categoryTemplates = TemplateCatalog.getTemplatesForCategory(category)
                            val defaultTemplate = categoryTemplates.firstOrNull() ?: TemplateCatalog.BIRTHDAY_VIBRANT
                            selectedTemplate = defaultTemplate
                            headline = defaultTemplate.headline
                            customMessage = defaultTemplate.defaultMessage
                        },
                        selectedPalette = selectedPalette,
                        onPaletteChange = { selectedPalette = it },
                        selectedTemplate = selectedTemplate,
                        onTemplateChange = { template ->
                            selectedTemplate = template
                            headline = template.headline
                            customMessage = template.defaultMessage
                        },
                        selectedLayout = layoutMode,
                        onLayoutChange = { layoutMode = it },
                        uploadedPhotoCount = photoUris.size,
                        onUploadPhotosClick = {
                            photoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                        photoUris = photoUris,
                        onRemovePhoto = { uri -> photoUris = photoUris.filter { it != uri } },
                        onClearPhotos = { photoUris = emptyList() },
                        onBackClick = onBackClick,
                        recipientName = recipientName,
                        onNameChange = { recipientName = it },
                        headline = headline,
                        onHeadlineChange = { headline = it },
                        customMessage = customMessage,
                        onMessageChange = { customMessage = it },
                        signOff = signOff,
                        onSignOffChange = { signOff = it },
                        onSaveCardClick = { saveCardToRoom() },
                        onDownloadClick = { handleDownloadCard() },
                        onShareClick = { handleShareCard() }
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Button(
                            onClick = { isPreviewMode = false },
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF8B5CF6)),
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Text("✨ Exit Preview Mode", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }

            // Section 11: Saved Cards Board Bottom Sheet Overlay
            AnimatedVisibility(
                visible = showSavedCardsSheet,
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it })
            ) {
                SavedCardsSheet(
                    savedCards = savedCards,
                    onSelectCard = { card ->
                        // Load card into editor state
                        recipientName = card.recipientName
                        headline = card.headline
                        customMessage = card.message
                        signOff = card.signOff
                        selectedCategory = try { CardCategory.valueOf(card.category) } catch (_: Exception) { CardCategory.BIRTHDAY }
                        selectedTemplate = TemplateCatalog.findById(card.templateId)
                        layoutMode = try { CardLayoutMode.valueOf(card.layoutMode) } catch (_: Exception) { CardLayoutMode.CAROUSEL }
                        photoUris = if (card.photoUrisCsv.isNotBlank()) {
                            card.photoUrisCsv.split(",").mapNotNull { runCatching { Uri.parse(it) }.getOrNull() }
                        } else emptyList()
                        likeCount = card.likeCount
                        showSavedCardsSheet = false
                        Toast.makeText(context, "Loaded '${card.title}'!", Toast.LENGTH_SHORT).show()
                    },
                    onDeleteCard = { card ->
                        coroutineScope.launch {
                            cardDao.deleteCard(card)
                            Toast.makeText(context, "Card deleted from Collection", Toast.LENGTH_SHORT).show()
                        }
                    },
                    onClose = { showSavedCardsSheet = false }
                )
            }
        }
    }
}

/**
 * Top Bar for Card Editor with Back button and quick actions.
 *
 * @param onBackClick Action to navigate back to home screen.
 * @param savedCardsCount Total count of saved cards in database.
 * @param onOpenSavedCards Action opening saved cards sheet.
 * @param onSaveClick Quick-save action to Room DB.
 * @param isPreviewMode Current preview mode state.
 * @param onPreviewToggle Callback toggling preview mode.
 * @param modifier Optional layout modifier.
 */
@Composable
private fun StudioTopBar(
    onBackClick: () -> Unit,
    savedCardsCount: Int,
    onOpenSavedCards: () -> Unit,
    onSaveClick: () -> Unit,
    isPreviewMode: Boolean,
    onPreviewToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color(0xFF1E293B))
                    .size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back to Home",
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = Color(0xFFFFD700),
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Studio Editor",
                    color = Color.White,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Quick action icon buttons
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = onOpenSavedCards,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = if (savedCardsCount > 0) Color(0xFF1E293B) else Color(0xFF18181F)
                ),
                shape = RoundedCornerShape(16.dp),
                elevation = ButtonDefaults.elevation(defaultElevation = 0.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Bookmark,
                    contentDescription = null,
                    tint = Color(0xFF3897F0),
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Cards ($savedCardsCount)",
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

                        IconButton(
                    onClick = onSaveClick,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color(0xFF10B981).copy(alpha = 0.2f))
                        .size(34.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.BookmarkAdd,
                        contentDescription = "Save to Collection",
                        tint = Color(0xFF10B981),
                        modifier = Modifier.size(16.dp)
                    )
                }

                IconButton(
                    onClick = onPreviewToggle,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(if (isPreviewMode) Color(0xFF8B5CF6) else Color(0xFF262626))
                        .size(34.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Visibility,
                        contentDescription = "Preview Mode",
                        tint = Color.White,
                        modifier = Modifier.size(15.dp)
                    )
                }
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
fun CardScreenPreview() {
    GreetingCardScreen(
        onBackClick = {}
    )
}
