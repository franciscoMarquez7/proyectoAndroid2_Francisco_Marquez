package com.example.proyecto_francisco_marquez.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
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
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatabaseScreenPersonaje(navController: NavHostController) {
    val firestoreService = FirestoreService()

    var firestoreCharacters by remember { mutableStateOf<List<CharacterModel>>(emptyList()) }

    val db = FirebaseFirestore.getInstance()
    val coroutineScope = rememberCoroutineScope()

    fun reloadCharacters() {
        coroutineScope.launch(Dispatchers.IO) {
            try {
                val snapshot = db.collection("personajes").get().await()
                firestoreCharacters = snapshot.documents.map { doc ->
                    CharacterModel(
                        name = doc.getString("name") ?: "Desconocido",
                        imageUrl = doc.getString("imagenUrl") ?: "",
                        status = doc.getString("status") ?: "Desconocido",
                        species = doc.getString("species") ?: "Desconocido",
                        id = doc.id // Usamos el ID del documento
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    LaunchedEffect(true) {
        reloadCharacters()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Personajes", style = TitleStyle) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver Atrás")
                    }
                },
                actions = {
                    IconButton(onClick = { reloadCharacters() }) {
                        Icon(Icons.Filled.Refresh, contentDescription = "Recargar Lista")
                    }
                    IconButton(onClick = { navController.navigate("agregarPersonajeScreen") }) {
                        Icon(Icons.Filled.Add, contentDescription = "Agregar Personaje")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            // Fondo de pantalla
            Image(
                painter = painterResource(id = R.drawable.imagen_fondo),
                contentDescription = "Fondo",
                modifier = Modifier.fillMaxSize().alpha(0.08f),
                contentScale = ContentScale.Crop
            )

            // Lista de personajes
            LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                items(firestoreCharacters) { character ->
                    CharacterCard(navController, character)
                }
            }
        }
    }
}

@Composable
fun CharacterCard(navController: NavHostController, character: CharacterModel) {
    val coroutineScope = rememberCoroutineScope()
    val db = FirebaseFirestore.getInstance()

    Card(
        modifier = Modifier.fillMaxWidth().padding(12.dp).shadow(8.dp, shape = MaterialTheme.shapes.medium),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF90CAF9))
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp).background(
                Brush.verticalGradient(colors = listOf(Color(0xFF42A5F5), Color(0xFF64B5F6))),
                shape = MaterialTheme.shapes.medium
            ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(character.name, style = TitleStyle, fontSize = 22.sp, modifier = Modifier.padding(bottom = 4.dp), color = Color.White)
            Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                Text("Especie: ${character.species}", fontSize = 18.sp, color = Color(0xFFE3F2FD), modifier = Modifier.padding(end = 16.dp))
                Text("Estado: ${character.status}", fontSize = 18.sp, color = Color(0xFFE3F2FD))
            }
            AsyncImage(
                model = character.imageUrl,
                contentDescription = "Imagen del personaje",
                modifier = Modifier.fillMaxWidth().height(200.dp).padding(8.dp),
                contentScale = ContentScale.Crop
            )

            Row(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { navController.navigate("modificarPersonajeScreen/${character.id}") },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF42A5F5))
                ) {
                    Text("Modificar")
                }

                Button(
                    onClick = {
                        coroutineScope.launch(Dispatchers.IO) {
                            try {
                                db.collection("personajes").document(character.id).delete()
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
                ) {
                    Text("Eliminar", color = Color.White)
                }
            }
        }
    }
}

data class CharacterModel(
    val name: String,
    val imageUrl: String,
    val status: String,
    val species: String,
    val id: String // Usamos el id del documento
)
