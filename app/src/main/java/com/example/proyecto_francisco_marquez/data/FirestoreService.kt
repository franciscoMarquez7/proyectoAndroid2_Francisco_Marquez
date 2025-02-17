package com.example.proyecto_francisco_marquez.data

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import android.util.Log

class FirestoreService {
    private val db = FirebaseFirestore.getInstance()
    private val charactersCollection = db.collection("personajes")

    // Actualizar un personaje
    suspend fun updateCharacter(characterId: String, updatedData: Map<String, Any>): Boolean {
        return try {
            // Usamos document(characterId) para actualizar un personaje por su ID
            charactersCollection.document(characterId).update(updatedData).await()
            true
        } catch (e: Exception) {
            Log.e("FirestoreService", "Error actualizando personaje: ${e.message}")
            false
        }
    }

    // Eliminar un personaje
    suspend fun deleteCharacter(characterId: String): Boolean {
        return try {
            // Eliminar un personaje usando su ID
            charactersCollection.document(characterId).delete().await()
            true
        } catch (e: Exception) {
            Log.e("FirestoreService", "Error eliminando personaje: ${e.message}")
            false
        }
    }

    // Agregar un personaje
    suspend fun addCharacter(character: Map<String, Any>): Boolean {
        return try {
            // Usamos .add() para agregar un personaje sin especificar el ID
            charactersCollection.add(character).await()
            true
        } catch (e: Exception) {
            Log.e("FirestoreService", "Error agregando personaje: ${e.message}")
            false
        }
    }
}
