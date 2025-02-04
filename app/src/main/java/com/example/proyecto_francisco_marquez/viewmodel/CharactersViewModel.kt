package com.example.proyecto_francisco_marquez.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

// Modelo de datos para un personaje
data class Character(
    val id: String,
    val name: String,
    val status: String,
    val species: String,
    val image: String,
)

// Modelo de respuesta para la API
data class RickAndMortyResponse(
    val results: List<Character>
)

// Servicio de la API
interface ApiService {
    @GET("character")
    suspend fun getCharacters(): RickAndMortyResponse
}

// ViewModel para manejar la lógica de datos
class CharactersViewModel : ViewModel() {

    private val _characters = MutableStateFlow<List<Character>>(emptyList())
    val characters: StateFlow<List<Character>> get() = _characters

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> get() = _errorMessage

    private val apiService: ApiService = Retrofit.Builder()
        .baseUrl("https://rickandmortyapi.com/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiService::class.java)

    init {
        fetchCharacters()
    }

    private fun fetchCharacters() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = apiService.getCharacters()
                _characters.value = response.results
                _isLoading.value = false
            } catch (e: Exception) {
                _isLoading.value = false
                _errorMessage.value = "Error: ${e.message}"
            }
        }
    }
}
