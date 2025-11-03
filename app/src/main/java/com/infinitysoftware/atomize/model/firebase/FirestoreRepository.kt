package com.infinitysoftware.atomize.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.infinitysoftware.atomize.model.habit.HabitEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class FirestoreRepository private constructor() {

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance().apply {
        firestoreSettings = com.google.firebase.firestore.FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .setCacheSizeBytes(com.google.firebase.firestore.FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED)
            .build()
    }

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    companion object {
        @Volatile
        private var INSTANCE: FirestoreRepository? = null

        fun getInstance(): FirestoreRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: FirestoreRepository().also { INSTANCE = it }
            }
        }
    }

    private fun getUserId(): String? = auth.currentUser?.uid

    private fun getUserHabitsCollection() = getUserId()?.let {
        firestore.collection("users").document(it).collection("habits")
    }

    suspend fun syncHabitsToFirestore(habits: List<HabitEntity>): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val collection = getUserHabitsCollection() ?: return@withContext Result.failure(
                Exception("User not authenticated")
            )

            val batch = firestore.batch()
            habits.forEach { habit ->
                val docRef = collection.document(habit.id.toString())
                batch.set(docRef, habit.toFirestoreMap(), SetOptions.merge())
            }
            batch.commit().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun syncHabitToFirestore(habit: HabitEntity): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val collection = getUserHabitsCollection() ?: return@withContext Result.failure(
                Exception("User not authenticated")
            )

            collection.document(habit.id.toString())
                .set(habit.toFirestoreMap(), SetOptions.merge())
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteHabitFromFirestore(habitId: Int): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val collection = getUserHabitsCollection() ?: return@withContext Result.failure(
                Exception("User not authenticated")
            )

            collection.document(habitId.toString())
                .delete()
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteHabitsByTextFromFirestore(text: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val collection = getUserHabitsCollection() ?: return@withContext Result.failure(
                Exception("User not authenticated")
            )

            val querySnapshot = collection.whereEqualTo("text", text).get().await()
            val batch = firestore.batch()
            querySnapshot.documents.forEach { doc ->
                batch.delete(doc.reference)
            }
            batch.commit().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun fetchAllHabitsFromFirestore(): Result<List<HabitEntity>> = withContext(Dispatchers.IO) {
        try {
            val collection = getUserHabitsCollection() ?: return@withContext Result.failure(
                Exception("User not authenticated")
            )

            val snapshot = collection.get().await()
            val habits = snapshot.documents.mapNotNull { doc ->
                doc.toHabitEntity()
            }
            Result.success(habits)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun observeHabitsForDateFromFirestore(date: String): Flow<List<HabitEntity>> = callbackFlow {
        val collection = getUserHabitsCollection()
        if (collection == null) {
            close()
            return@callbackFlow
        }

        val listener = collection.whereEqualTo("date", date)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val habits = snapshot?.documents?.mapNotNull { doc ->
                    doc.toHabitEntity()
                } ?: emptyList()

                trySend(habits)
            }

        awaitClose { listener.remove() }
    }

    suspend fun hasExistingData(): Result<Boolean> = withContext(Dispatchers.IO) {
        try {
            val collection = getUserHabitsCollection() ?: return@withContext Result.failure(
                Exception("User not authenticated")
            )

            val snapshot = collection.limit(1).get().await()
            Result.success(!snapshot.isEmpty)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun HabitEntity.toFirestoreMap(): Map<String, Any?> = mapOf(
        "id" to id,
        "text" to text,
        "isChecked" to isChecked,
        "streak" to streak,
        "date" to date,
        "days" to days,
        "notifyTime" to notifyTime,
        "notificationsEnabled" to notificationsEnabled,
        "updatedAt" to System.currentTimeMillis()
    )

    private fun com.google.firebase.firestore.DocumentSnapshot.toHabitEntity(): HabitEntity? {
        return try {
            HabitEntity(
                id = getLong("id")?.toInt() ?: return null,
                text = getString("text") ?: return null,
                isChecked = getBoolean("isChecked") ?: false,
                streak = getLong("streak")?.toInt() ?: 0,
                date = getString("date") ?: return null,
                days = getString("days") ?: "",
                notifyTime = getString("notifyTime"),
                notificationsEnabled = getBoolean("notificationsEnabled") ?: false
            )
        } catch (e: Exception) {
            null
        }
    }
}