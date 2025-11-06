package com.infinitysoftware.atomize.model.habit

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.infinitysoftware.atomize.model.settings.Settings
import com.infinitysoftware.atomize.model.settings.SettingsDao
import com.google.firebase.auth.FirebaseAuth

@Database(
    entities = [
        HabitEntity::class,
        Settings::class
    ],
    version = 3,
    exportSchema = false
)
abstract class HabitDatabase : RoomDatabase() {
    abstract fun habitDao(): HabitDao
    abstract fun settingsDao(): SettingsDao

    companion object {
        @Volatile
        private var INSTANCES: MutableMap<String, HabitDatabase> = mutableMapOf()
        
        private val lock = Any()

        /**
         * Vraća user ID ili "guest" za neprijavljenog korisnika
         */
        private fun getUserId(): String {
            val auth = FirebaseAuth.getInstance()
            return auth.currentUser?.uid ?: "guest"
        }

        /**
         * Dobija bazu podataka za trenutnog korisnika
         */
        fun getDatabase(context: Context): HabitDatabase {
            val userId = getUserId()
            return INSTANCES[userId] ?: synchronized(lock) {
                INSTANCES[userId] ?: buildDatabase(context, userId).also { 
                    INSTANCES[userId] = it 
                }
            }
        }

        /**
         * Kreira novu bazu podataka za određenog korisnika
         */
        private fun buildDatabase(context: Context, userId: String): HabitDatabase {
            val dbName = "habit_database_$userId"
            return Room.databaseBuilder(
                context.applicationContext,
                HabitDatabase::class.java,
                dbName
            )
                .fallbackToDestructiveMigration()
                .build()
        }

        /**
         * Zatvara bazu podataka za određenog korisnika
         * Koristi se kada se korisnik promeni
         */
        fun closeDatabase(userId: String) {
            synchronized(lock) {
                INSTANCES[userId]?.close()
                INSTANCES.remove(userId)
            }
        }

        /**
         * Zatvara bazu podataka za trenutnog korisnika
         */
        fun closeCurrentDatabase() {
            val userId = getUserId()
            closeDatabase(userId)
        }

        /**
         * Zatvara sve instance baza podataka
         */
        fun closeAllDatabases() {
            synchronized(lock) {
                INSTANCES.values.forEach { it.close() }
                INSTANCES.clear()
            }
        }
    }
}