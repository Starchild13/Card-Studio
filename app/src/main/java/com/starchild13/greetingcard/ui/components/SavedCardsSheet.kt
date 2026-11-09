/**
 * Saved cards board — 2-column grid for Card Studio.
 *
 * Provides a slide-up panel displaying all user-created greeting cards
 * retrieved from Room SQLite database in a Pinterest-style grid.
 */
package com.starchild13.greetingcard.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Style
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.starchild13.greetingcard.data.GreetingCardEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Saved Cards Board — 2-column grid.
 *
 * Displays all greeting cards stored in the local Room SQLite database in a grid.
 * Users can browse, reload a card into the studio for further editing,
 * or delete cards from the database.
 *
 * @param savedCards The list of [GreetingCardEntity] fetched from Room.
 * @param onSelectCard Callback when a card is selected to load back into the editor.
 * @param onDeleteCard Callback when the user requests deleting a card from Room.
 * @param onClose Callback to close the saved cards sheet.
 * @param modifier Optional modifier for outer layout styling.
 */
@Composable
fun SavedCardsSheet(
    savedCards: List<GreetingCardEntity>,
    onSelectCard: (GreetingCardEntity) -> Unit,
    onDeleteCard: (GreetingCardEntity) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dateFormatter = remember { SimpleDateFormat("MMM dd", Locale.getDefault()) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
            .safeDrawingPadding()
            .padding(16.dp)
    ) {
        // ── Header ─────────────────────────────────────────────────────────
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "My Cards",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${savedCards.size} saved",
                    color = Color.White.copy(alpha = 0.5f),
                    fontSize = 12.sp
                )
            }

            IconButton(
                onClick = onClose,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF262626))
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ── Content ────────────────────────────────────────────────────────
        if (savedCards.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Style,
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.3f),
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "No saved cards yet",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Build a card and tap Save on the last step!",
                        color = Color.White.copy(alpha = 0.5f),
                        fontSize = 13.sp
                    )
                }
            }
        } else {
            // 2-column grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(savedCards, key = { it.id }) { card ->
                    SavedCardGridItem(
                        card = card,
                        dateFormatter = dateFormatter,
                        onSelectCard = onSelectCard,
                        onDeleteCard = onDeleteCard
                    )
                }
            }
        }
    }
}

@Composable
private fun SavedCardGridItem(
    card: GreetingCardEntity,
    dateFormatter: java.text.SimpleDateFormat,
    onSelectCard: (GreetingCardEntity) -> Unit,
    onDeleteCard: (GreetingCardEntity) -> Unit
) {
    val categoryIcon = if (card.category == "BIRTHDAY") "🎂" else "🎉"
    val categoryColor = if (card.category == "BIRTHDAY") Color(0xFF3897F0) else Color(0xFF10B981)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(16.dp))
            .clickable { onSelectCard(card) },
        backgroundColor = Color(0xFF1C1C24),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Category badge
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(categoryColor.copy(alpha = 0.18f))
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
                Text(
                    text = "$categoryIcon ${card.category}",
                    color = categoryColor,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Card title
            Text(
                text = card.title,
                color = Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            // Message preview
            Text(
                text = card.message,
                color = Color.White.copy(alpha = 0.6f),
                fontSize = 11.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            // Date + actions row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = dateFormatter.format(Date(card.createdAt)),
                    color = Color.White.copy(alpha = 0.35f),
                    fontSize = 10.sp
                )
                Row {
                    IconButton(
                        onClick = { onSelectCard(card) },
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit",
                            tint = Color(0xFF3897F0),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    IconButton(
                        onClick = { onDeleteCard(card) },
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.DeleteOutline,
                            contentDescription = "Delete",
                            tint = Color(0xFFEF4444),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}
