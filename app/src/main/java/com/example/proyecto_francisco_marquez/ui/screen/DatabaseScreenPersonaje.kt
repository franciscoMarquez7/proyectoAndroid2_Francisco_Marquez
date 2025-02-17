package com.example.proyecto_francisco_marquez.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.proyecto_francisco_marquez.R
import com.example.proyecto_francisco_marquez.ui.TitleStyle
import com.example.proyecto_francisco_marquez.data.FirestoreService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatabaseScreenPersonaje(navController: NavHostController) {
    val apiUrl = "https://rickandmortyapi.com/api/character"
    var characters by remember { mutableStateOf<List<Character>>(emptyList()) }

    // Cargar los personajes desde la API
    LaunchedEffect(true) {
        withContext(Dispatchers.IO) {
            try {
                val url = URL(apiUrl)
                val connection = url.openConnection()
                val inputStream = connection.getInputStream()
                val response = inputStream.bufferedReader().readText()
                val jsonResponse = JSONObject(response)
                val results = jsonResponse.getJSONArray("results")
                val fetchedCharacters = mutableListOf<Character>()
                for (i in 0 until results.length()) {
                    val characterObj = results.getJSONObject(i)
                    val name = characterObj.getString("name")
                    val imageUrl = characterObj.getString("image")
                    val status = characterObj.getString("status")
                    fetchedCharacters.add(Character(name, imageUrl, status))
                }
                characters = fetchedCharacters
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Crear un scope para la corrutina
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Personajes", style = TitleStyle) },
                actions = {
                    // Botón para agregar un nuevo personaje
                    IconButton(onClick = { navController.navigate("agregarPersonajeScreen") }) {
                        Icon(Icons.Filled.Add, contentDescription = "Agregar Personaje")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Fondo de pantalla
            Image(
                painter = painterResource(id = R.drawable.imagen_fondo),
                contentDescription = "Fondo",
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(0.1f),
                contentScale = ContentScale.Crop
            )

            // Contenedor de la lista de personajes
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
            ) {
                items(characters) { character ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .background(Color.White.copy(alpha = 0.7f), shape = MaterialTheme.shapes.medium),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            character.name,
                            style = TitleStyle,
                            fontSize = 20.sp,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            "Estado: ${character.status}",
                            style = TitleStyle,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        AsyncImage(
                            model = character.imageUrl,
                            contentDescription = "Imagen del personaje",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .padding(8.dp),
                            contentScale = ContentScale.Crop
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Botón para modificar personaje
                        Button(
                            onClick = { navController.navigate("modificarPersonajeScreen/${character.name}") },
                            modifier = Modifier.fillMaxWidth().padding(8.dp)
                        ) {
                            Text("Modificar")
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Botón para eliminar personaje
                        Button(
                            onClick = {
                                // Ejecutar la eliminación en una corrutina
                                coroutineScope.launch {
                                    val firestoreService = FirestoreService()
                                    val success = firestoreService.deleteCharacter(character.name)
                                    if (success) {
                                        // El personaje se eliminó correctamente
                                        // Actualizar la lista de personajes después de eliminar
                                        characters = characters.filter { it.name != character.name }
                                    } else {
                                        // Mostrar un mensaje de error si no se eliminó
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth().padding(8.dp)
                        ) {
                            Text("Eliminar")
                        }
                    }
                }
            }
        }
    }
}

data class Character(
    val name: String,
    val imageUrl: String,
    val status: String
)
