/**
 * Home Screen UI module for Card Studio.
 *
 * Provides the main landing page with search filtering, category tabs,
 * saved card collection grid, creation call-to-action banner, and delete confirmation dialogs.
 */
package com.starchild13.greetingcard.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.starchild13.greetingcard.data.GreetingCardEntity
import com.starchild13.greetingcard.model.TemplateCatalog

/**
 * Home Screen for Card Studio.
 *
 * Clean landing page featuring top icon navigation, greeting card creation prompt,
 * search and category filters, and saved cards gallery where users can view, edit,
 * duplicate, or delete created cards.
 * Styled with rich shades of blue, grey, purple, and modern gradients.
 *
 * @param savedCards List of [GreetingCardEntity] fetched from local Room database.
 * @param onCreateNewClick Action to launch blank editor.
 * @param onSelectCard Action to load selected card into editor.
 * @param onDeleteCard Action to delete card from Room database.
 * @param onDuplicateCard Action to create a duplicate copy of a card.
 * @param modifier Optional layout modifier.
 */
@Composable
fun HomeScreen(
    savedCards: List<GreetingCardEntity>,
    onCreateNewClick: () -> Unit,
    onSelectCard: (GreetingCardEntity) -> Unit,
    onDeleteCard: (GreetingCardEntity) -> Unit,
    onDuplicateCard: (GreetingCardEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("ALL") }
    var cardToDelete by remember { mutableStateOf<GreetingCardEntity?>(null) }

    val categories = listOf("ALL", "BIRTHDAY", "CONGRATULATIONS")

    // Dynamic search & category filtering logic
    val filteredCards = remember(savedCards, searchQuery, selectedCategory) {
        savedCards.filter { card ->
            val matchesSearch = searchQuery.isBlank() ||
                card.title.contains(searchQuery, ignoreCase = true) ||
                card.message.contains(searchQuery, ignoreCase = true) ||
                card.recipientName.contains(searchQuery, ignoreCase = true)

            val matchesCategory = selectedCategory == "ALL" || card.category.equals(selectedCategory, ignoreCase = true)

            matchesSearch && matchesCategory
        }
    }

    // Section 1: Delete confirmation alert dialog
    if (cardToDelete != null) {
        AlertDialog(
            onDismissRequest = { cardToDelete = null },
            title = { Text("Delete Card", color = Color.White, fontWeight = FontWeight.Bold) },
            text = { Text("Are you sure you want to delete '${cardToDelete?.title}'? This action cannot be undone.", color = Color(0xFF94A3B8)) },
            confirmButton = {
                Button(
                    onClick = {
                        cardToDelete?.let { onDeleteCard(it) }
                        cardToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFEF4444)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Delete", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                Button(
                    onClick = { cardToDelete = null },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF334155)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Cancel", color = Color.White)
                }
            },
            backgroundColor = Color(0xFF1E2538),
            shape = RoundedCornerShape(24.dp)
        )
    }

    // Rich gradient background blending deep slate, midnight blue, and electric purple
    val backgroundBrush = Brush.verticalGradient(
        listOf(
            Color(0xFF0D0F18),
            Color(0xFF13172B),
            Color(0xFF1B1430)
        )
    )

    Surface(
        modifier = modifier.fillMaxSize(),
        color = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundBrush)
                .safeDrawingPadding()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Section 2: Top Navigation Bar with Branding & Saved Cards Badge
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    Brush.linearGradient(
                                        listOf(Color(0xFF8B5CF6), Color(0xFF3897F0))
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Card Studio",
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Dynamic Greeting Creator",
                                color = Color(0xFF94A3B8),
                                fontSize = 11.sp
                            )
                        }
                    }

                    // Saved Cards Count Pill Badge
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color(0xFF1E2538))
                            .border(1.dp, Color(0xFF334155), RoundedCornerShape(20.dp))
                            .padding(horizontal = 14.dp, vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Bookmark,
                                contentDescription = null,
                                tint = Color(0xFF3897F0),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "${savedCards.size} Saved",
                                color = Color(0xFFE2E8F0),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Section 3: Hero Call-to-Action Banner for Creating New Greeting Cards
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onCreateNewClick() },
                    shape = RoundedCornerShape(24.dp),
                    backgroundColor = Color(0xFF1E2538),
                    elevation = 8.dp
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.horizontalGradient(
                                    listOf(
                                        Color(0xFF1E2538),
                                        Color(0xFF261D3A),
                                        Color(0xFF1A2A44)
                                    )
                                )
                            )
                            .border(1.dp, Color(0xFF475569).copy(alpha = 0.5f), RoundedCornerShape(24.dp))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = "✨", fontSize = 18.sp)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Create Personalized Card",
                                        color = Color.White,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Design gorgeous cards with custom photos, templates, and vibrant color palettes!",
                                    color = Color(0xFF94A3B8),
                                    fontSize = 12.sp,
                                    lineHeight = 16.sp
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Button(
                                onClick = onCreateNewClick,
                                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
                                elevation = ButtonDefaults.elevation(0.dp),
                                shape = RoundedCornerShape(16.dp),
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(
                                        Brush.linearGradient(
                                            listOf(Color(0xFF8B5CF6), Color(0xFF3897F0))
                                        )
                                    )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "New",
                                    color = Color.White,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Section 4: Search Bar for Filtering Saved Cards
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Search saved cards...", color = Color(0xFF64748B)) },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = Color(0xFF8B5CF6))
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear search", tint = Color(0xFF94A3B8))
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = Color.White,
                        backgroundColor = Color(0xFF1A1F30),
                        cursorColor = Color(0xFF8B5CF6),
                        focusedBorderColor = Color(0xFF8B5CF6),
                        unfocusedBorderColor = Color(0xFF334155)
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Section 5: Occasion Category Filter Chips (All, Birthday, Congratulations)
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(categories) { category ->
                        val isSelected = selectedCategory == category
                        val label = when (category) {
                            "ALL" -> "🌟 All Cards"
                            "BIRTHDAY" -> "🎂 Birthday"
                            "CONGRATULATIONS" -> "🎉 Congratulations"
                            else -> category
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) Color(0xFF8B5CF6) else Color(0xFF1E2538))
                                .border(1.dp, if (isSelected) Color(0xFF8B5CF6) else Color(0xFF334155), RoundedCornerShape(12.dp))
                                .clickable { selectedCategory = category }
                                .padding(horizontal = 14.dp, vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = label,
                                color = if (isSelected) Color.White else Color(0xFF94A3B8),
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Section 6: Saved Cards Section Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "MY COLLECTION (${filteredCards.size})",
                        color = Color(0xFF94A3B8),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Section 7: Saved Card Collection Grid (with Empty and Filtered states)
                if (savedCards.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(RoundedCornerShape(24.dp))
                                    .background(Color(0xFF1E2538)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CardGiftcard,
                                    contentDescription = null,
                                    tint = Color(0xFF64748B),
                                    modifier = Modifier.size(36.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "No saved cards yet",
                                color = Color(0xFFCBD5E1),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Tap 'New' above to design your first dynamic card!",
                                color = Color(0xFF64748B),
                                fontSize = 12.sp
                            )
                        }
                    }
                } else if (filteredCards.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "No matching cards found",
                                color = Color(0xFFCBD5E1),
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Button(
                                onClick = {
                                    searchQuery = ""
                                    selectedCategory = "ALL"
                                },
                                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF1E2538)),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("Clear Filters", color = Color(0xFF3897F0), fontSize = 12.sp)
                            }
                        }
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp),
                        contentPadding = PaddingValues(bottom = 90.dp)
                    ) {
                        items(filteredCards, key = { it.id }) { card ->
                            val template = TemplateCatalog.findById(card.templateId)
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onSelectCard(card) },
                                shape = RoundedCornerShape(20.dp),
                                backgroundColor = template.cardBackground,
                                elevation = 6.dp
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .border(1.dp, Color(0xFF334155).copy(alpha = 0.6f), RoundedCornerShape(20.dp))
                                        .padding(16.dp)
                                ) {
                                    Column(modifier = Modifier.fillMaxWidth()) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(10.dp))
                                                    .background(template.gradientBrush)
                                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                                            ) {
                                                Text(
                                                    text = if (card.category == "BIRTHDAY") "🎂 Birthday" else "🎉 Congrats",
                                                    color = Color.White,
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }

                                            Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                                                // Card duplication icon action
                                                IconButton(
                                                    onClick = { onDuplicateCard(card) },
                                                    modifier = Modifier.size(28.dp)
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.ContentCopy,
                                                        contentDescription = "Duplicate",
                                                        tint = Color(0xFF3897F0),
                                                        modifier = Modifier.size(14.dp)
                                                    )
                                                }

                                                // Card deletion icon action
                                                IconButton(
                                                    onClick = { cardToDelete = card },
                                                    modifier = Modifier.size(28.dp)
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.Delete,
                                                        contentDescription = "Delete",
                                                        tint = Color(0xFFEF4444).copy(alpha = 0.8f),
                                                        modifier = Modifier.size(15.dp)
                                                    )
                                                }
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(12.dp))

                                        Text(
                                            text = card.title,
                                            color = template.textColor,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            maxLines = 1
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = card.message,
                                            color = template.textColor.copy(alpha = 0.7f),
                                            fontSize = 11.sp,
                                            maxLines = 2,
                                            lineHeight = 15.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Section 8: Floating Action Button (FAB) for Creating New Card
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(24.dp)
                    .size(60.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(Color(0xFF8B5CF6), Color(0xFF3897F0))
                        )
                    )
                    .clickable { onCreateNewClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Create New Card",
                    tint = Color.White,
                    modifier = Modifier.size(26.dp)
                )
            }
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        savedCards = emptyList(),
        onCreateNewClick = {},
        onSelectCard = {},
        onDeleteCard = {},
        onDuplicateCard = {}
    )
}
