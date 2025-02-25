package com.example.proyecto_francisco_marquez.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import android.util.Log
import com.example.proyecto_francisco_marquez.ui.screen.CharacterModel

class FirestoreService {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val charactersCollection = db.collection("personajes")
    private val episodesCollection = db.collection("episodios")

    private fun isUserAuthenticated(): Boolean {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            Log.e("FirestoreService", "Usuario no autenticado")
            return false
        }
        return true
    }

    private suspend fun verifyAuthentication(): Boolean {
        val currentUser = auth.currentUser ?: return false
        try {
            // Forzar actualización del token
            currentUser.getIdToken(true).await()
            return true
        } catch (e: Exception) {
            Log.e("FirestoreService", "Error verificando autenticación", e)
            return false
        }
    }

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
        if (!isUserAuthenticated()) return emptyList()
        
        return try {
            val snapshot = charactersCollection.get().await()
            snapshot.documents.map { doc ->
                CharacterModel(
                    name = doc.getString("name") ?: "",
                    imageUrl = doc.getString("imagenUrl") ?: "",
                    status = doc.getString("status") ?: "",
                    species = doc.getString("species") ?: "",
                    id = doc.id
                )
            }
        } catch (e: Exception) {
            Log.e("FirestoreService", "Error obteniendo personajes: ${e.message}")
            emptyList()
        }
    }

    suspend fun addEpisode(episode: Map<String, Any>): Boolean {
        return try {
            // Verificar autenticación
            val currentUser = auth.currentUser
            if (currentUser == null) {
                Log.e("FirestoreService", "Usuario no autenticado")
                return false
            }

            Log.d("FirestoreService", "Usuario autenticado: ${currentUser.uid}")
            Log.d("FirestoreService", "Intentando agregar episodio: $episode")

            // Obtener token fresco
            val token = currentUser.getIdToken(true).await()
            Log.d("FirestoreService", "Token obtenido: ${token.token?.take(10)}...")

            // Agregar episodio
            val docRef = episodesCollection
                .document() // Crear un nuevo documento con ID automático
                .set(episode) // Usar set en lugar de add
                .await()

            Log.d("FirestoreService", "Episodio agregado exitosamente")
            true
        } catch (e: Exception) {
            when {
                e.message?.contains("PERMISSION_DENIED") == true -> {
                    Log.e("FirestoreService", "Error de permisos. Token: ${auth.currentUser?.getIdToken(false)?.await()?.token?.take(10)}")
                }
                e.message?.contains("UNAUTHENTICATED") == true -> {
                    Log.e("FirestoreService", "Error de autenticación. Usuario: ${auth.currentUser?.uid}")
                }
                else -> Log.e("FirestoreService", "Error desconocido", e)
            }
            false
        }
    }

    suspend fun updateEpisode(episodeId: String, updatedData: Map<String, Any>): Boolean {
        return try {
            val currentUser = auth.currentUser
            if (currentUser == null) {
                Log.e("FirestoreService", "Usuario no autenticado")
                return false
            }

            // Obtener token fresco
            currentUser.getIdToken(true).await()

            episodesCollection.document(episodeId).update(updatedData).await()
            true
        } catch (e: Exception) {
            Log.e("FirestoreService", "Error actualizando episodio", e)
            false
        }
    }

    suspend fun deleteEpisode(episodeId: String): Boolean {
        return try {
            val currentUser = auth.currentUser
            if (currentUser == null) {
                Log.e("FirestoreService", "Usuario no autenticado")
                return false
            }

            // Obtener token fresco
            currentUser.getIdToken(true).await()

            episodesCollection.document(episodeId).delete().await()
            true
        } catch (e: Exception) {
            Log.e("FirestoreService", "Error eliminando episodio", e)
            false
        }
    }
}
