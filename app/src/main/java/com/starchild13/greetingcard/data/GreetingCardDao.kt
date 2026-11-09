/**
 * Data Access Object (DAO) declarations for Card Studio Room database.
 *
 * Defines database queries and operations for managing saved greeting cards.
 */
package com.starchild13.greetingcard.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for managing [GreetingCardEntity] instances in Room.
 *
 * Provides reactive observable queries using Kotlin Coroutines Flow and suspending
 * methods for database insertions and deletions.
 */
@Dao
interface GreetingCardDao {

    /**
     * Observes all saved greeting cards ordered by creation date (newest first).
     * Emits a fresh list whenever any database modification occurs.
     *
     * @return A reactive [Flow] emitting a list of [GreetingCardEntity] objects.
     */
    @Query("SELECT * FROM greeting_cards ORDER BY created_at DESC")
    fun getAllCards(): Flow<List<GreetingCardEntity>>

    /**
     * Retrieves a single greeting card by its unique identifier.
     *
     * @param id The primary key of the greeting card.
     * @return The found [GreetingCardEntity] or null if not found.
     */
    @Query("SELECT * FROM greeting_cards WHERE id = :id LIMIT 1")
    suspend fun getCardById(id: Long): GreetingCardEntity?

    /**
     * Inserts a new greeting card into the database. If a card with the same ID
     * already exists, it is replaced.
     *
     * @param card The [GreetingCardEntity] to insert.
     * @return The auto-generated row ID of the newly inserted card.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCard(card: GreetingCardEntity): Long

    /**
     * Deletes an existing greeting card from the database.
     *
     * @param card The [GreetingCardEntity] to remove.
     */
    @Delete
    suspend fun deleteCard(card: GreetingCardEntity)

    /**
     * Deletes a card by its primary key ID.
     *
     * @param id The unique identifier of the card to delete.
     */
    @Query("DELETE FROM greeting_cards WHERE id = :id")
    suspend fun deleteCardById(id: Long)
}
