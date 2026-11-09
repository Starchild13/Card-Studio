/**
 * Database configuration for Card Studio Room persistence.
 *
 * Provides database instance initialization and singleton access.
 */
package com.starchild13.greetingcard.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Main Room Database for the Greeting Card Application.
 *
 * Implements a thread-safe singleton pattern to provide access to [GreetingCardDao].
 * Schema version is set to 3 with fallback to destructive migration enabled for updates.
 */
@Database(
    entities = [GreetingCardEntity::class],
    version = 4,
    exportSchema = false
)
abstract class GreetingCardDatabase : RoomDatabase() {

    /**
     * Provides the Data Access Object (DAO) for performing greeting card database operations.
     *
     * @return Instance of [GreetingCardDao].
     */
    abstract fun greetingCardDao(): GreetingCardDao

    companion object {
        @Volatile
        private var INSTANCE: GreetingCardDatabase? = null

        /**
         * Returns the thread-safe singleton instance of [GreetingCardDatabase].
         *
         * @param context Application context used for building database instance.
         * @return The active [GreetingCardDatabase] singleton instance.
         */
        fun getDatabase(context: Context): GreetingCardDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GreetingCardDatabase::class.java,
                    "greeting_card_database"
                )
                    .fallbackToDestructiveMigration(dropAllTables = true)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
