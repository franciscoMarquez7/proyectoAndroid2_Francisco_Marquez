package com.example.proyecto_francisco_marquez.data

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import android.util.Log
import com.example.proyecto_francisco_marquez.ui.screen.CharacterModel

class FirestoreService {
    private val db = FirebaseFirestore.getInstance()
    private val charactersCollection = db.collection("personajes")

    // Agregar un personaje
    suspend fun addCharacter(character: Map<String, Any>): Boolean {
        return try {
            charactersCollection.add(character).await() // Usamos .add() para agregar sin un ID especificado
            true
        } catch (e: Exception) {
            Log.e("FirestoreService", "Error agregando personaje: ${e.message}")
            false
        }
    }

    // Actualizar un personaje usando el ID del documento
    suspend fun updateCharacter(characterId: String, updatedData: Map<String, Any>): Boolean {
        return try {
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
    suspend fun getCharacters(): List<CharacterModel> {
        return try {
            val snapshot = charactersCollection.get().await()
            val charactersList = mutableListOf<CharacterModel>()
            for (document in snapshot.documents) {
                // Usar getString() para obtener los valores de cada campo
                val name = document.getString("name") ?: ""
                val status = document.getString("status") ?: ""
                val species = document.getString("species") ?: ""
                val episodeId = document.getString("episode_id") ?: ""  // Asegúrate de que el campo se llama "episode_id"
                val imageUrl = document.getString("imagenUrl") ?: ""    // Asegúrate de que el campo se llama "imagenUrl"

                // Añadir el personaje a la lista con el ID del documento
                charactersList.add(CharacterModel(name, imageUrl, status, species, document.id)) // Aquí solo estamos pasando los 5 parámetros correctos
            }
            charactersList
        } catch (e: Exception) {
            Log.e("FirestoreService", "Error obteniendo personajes: ${e.message}")
            emptyList()
        }
    }
}
