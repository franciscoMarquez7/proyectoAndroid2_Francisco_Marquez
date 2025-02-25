package com.example.proyecto_francisco_marquez.ui.screen

import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.proyecto_francisco_marquez.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatabaseScreenPersonaje(navController: NavHostController) {
    val auth = FirebaseAuth.getInstance()
    val context = LocalContext.current
    var characters by remember { mutableStateOf<List<CharacterModel>>(emptyList()) }
    val coroutineScope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }
    var showNoData by remember { mutableStateOf(false) }

    fun reloadCharacters() {
        isLoading = true
        showNoData = false
        coroutineScope.launch(Dispatchers.IO) {
            try {
                if (auth.currentUser == null) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Sesión expirada. Por favor, vuelve a iniciar sesión", Toast.LENGTH_LONG).show()
                        navController.navigate("login")
                    }
                    return@launch
                }

                val db = FirebaseFirestore.getInstance()
                val snapshot = db.collection("personajes").get().await()
                characters = snapshot.documents.map { doc ->
                    CharacterModel(
                        name = doc.getString("name") ?: "Desconocido",
                        imageUrl = doc.getString("imagenUrl") ?: "",
                        status = doc.getString("status") ?: "Desconocido",
                        species = doc.getString("species") ?: "Desconocido",
                        id = doc.id
                    )
                }
                withContext(Dispatchers.Main) {
                    isLoading = false
                    showNoData = characters.isEmpty()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    isLoading = false
                    showNoData = true
                    val errorMessage = when {
                        e.message?.contains("PERMISSION_DENIED") == true -> 
                            "No tienes permisos suficientes. Por favor, cierra sesión y vuelve a iniciarla."
                        else -> "Error: ${e.message}"
                    }
                    Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun deleteCharacter(characterId: String) {
        coroutineScope.launch(Dispatchers.IO) {
            try {
                FirebaseFirestore.getInstance()
                    .collection("personajes")
                    .document(characterId)
                    .delete()
                    .await()
                
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Personaje eliminado correctamente", Toast.LENGTH_SHORT).show()
                    reloadCharacters()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Error al eliminar el personaje: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    LaunchedEffect(auth.currentUser) {
        if (auth.currentUser != null) {
            reloadCharacters()
        } else {
            navController.navigate("login") {
                popUpTo(navController.graph.startDestinationId) { inclusive = true }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Fondo
        Image(
            painter = painterResource(id = R.drawable.imagen_fondo),
            contentDescription = "Fondo",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = 0.3f
        )

        Column(modifier = Modifier.fillMaxSize()) {
            // TopAppBar
            TopAppBar(
                title = { 
                    Text(
                        "Personajes",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color.Black
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.Black
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { reloadCharacters() }) {
                        Icon(
                            Icons.Filled.Refresh,
                            contentDescription = "Recargar",
                            tint = Color.Black
                        )
                    }
                    IconButton(onClick = { navController.navigate("agregarPersonajeScreen") }) {
                        Icon(
                            Icons.Filled.Add,
                            contentDescription = "Agregar",
                            tint = Color.Black
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White.copy(alpha = 0.9f)
                )
            )

            // Contenido
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (showNoData) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .padding(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White.copy(alpha = 0.9f)
                        )
                    ) {
                        Text(
                            "NO HAY REGISTROS",
                            modifier = Modifier
                                .padding(32.dp)
                                .fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.Gray
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    items(characters) { character ->
                        CharacterCard(navController, character, onDelete = {
                            deleteCharacter(character.id)
                        })
                    }
                }
            }
        }
    }
}

@Composable
private fun CharacterCard(
    navController: NavHostController,
    character: CharacterModel,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.9f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                character.name,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            AsyncImage(
                model = character.imageUrl,
                contentDescription = "Imagen del personaje",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop,
                error = painterResource(id = R.drawable.imagen_fondo)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text("Especie: ${character.species}", color = Color.Gray)
                Text("Estado: ${character.status}", color = Color.Gray)
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { navController.navigate("modificarPersonajeScreen/${character.id}") },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Blue.copy(alpha = 0.8f)
                    )
                ) {
                    Text("Modificar")
                }
                
                Button(
                    onClick = onDelete,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red.copy(alpha = 0.8f)
                    )
                ) {
                    Text("Eliminar")
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
    val id: String
)
