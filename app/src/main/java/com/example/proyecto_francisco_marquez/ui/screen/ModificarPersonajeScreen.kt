package com.example.proyecto_francisco_marquez.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import com.example.proyecto_francisco_marquez.data.FirestoreService
import com.example.proyecto_francisco_marquez.ui.TitleStyle
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModificarPersonajeScreen(navController: NavHostController, documentId: String) {
    val db = FirebaseFirestore.getInstance()
    val firestoreService = FirestoreService()
    val scope = rememberCoroutineScope()

    // Estado para los campos (se rellenarán con los datos actuales de Firestore)
    var newName by remember { mutableStateOf("") }
    var newStatus by remember { mutableStateOf("") }
    var newSpecies by remember { mutableStateOf("") }
    var newEpisodeId by remember { mutableStateOf("") }
    var newImageUrl by remember { mutableStateOf("") }

    // Obtener los datos actuales del personaje desde Firestore
    LaunchedEffect(documentId) {
        db.collection("personajes").document(documentId).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    // Asegurémonos de usar los nombres correctos
                    newName = document.getString("name") ?: ""
                    newStatus = document.getString("status") ?: ""
                    newSpecies = document.getString("species") ?: ""
                    newEpisodeId = document.getString("episode_id") ?: ""  // Cambié de "episodeId" a "episode_id"
                    newImageUrl = document.getString("imagenUrl") ?: ""  // Cambié de "imageUrl" a "imagenUrl"
                }
            }
            .addOnFailureListener { exception ->
                exception.printStackTrace()
            }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Modificar Personaje", style = TitleStyle) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
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
            // Imagen de fondo con opacidad sutil
            Image(
                painter = painterResource(id = R.drawable.imagen_fondo),
                contentDescription = "Fondo de modificación",
                modifier = Modifier.fillMaxSize().alpha(0.05f),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Modificar: $newName",
                    fontSize = 24.sp,
                    modifier = Modifier.padding(bottom = 16.dp),
                    color = Color.White
                )

                // Imagen del personaje con margen y sombra
                AsyncImage(
                    model = newImageUrl.ifEmpty { "https://via.placeholder.com/150" }, // Imagen por defecto si no hay URL
                    contentDescription = "Imagen del personaje",
                    modifier = Modifier
                        .size(150.dp)
                        .padding(8.dp)
                        .background(Color.White.copy(alpha = 0.5f), shape = RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Campos pre-rellenados con los datos de Firestore
                OutlinedTextField(
                    value = newName,
                    onValueChange = { newName = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                    singleLine = true
                )

                OutlinedTextField(
                    value = newStatus,
                    onValueChange = { newStatus = it },
                    label = { Text("Estado") },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                    singleLine = true
                )

                OutlinedTextField(
                    value = newSpecies,
                    onValueChange = { newSpecies = it },
                    label = { Text("Especie") },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                    singleLine = true
                )

                OutlinedTextField(
                    value = newEpisodeId,
                    onValueChange = { newEpisodeId = it },
                    label = { Text("ID del episodio") },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                    singleLine = true
                )

                OutlinedTextField(
                    value = newImageUrl,
                    onValueChange = { newImageUrl = it },
                    label = { Text("URL de la imagen") },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                    singleLine = true
                )

                // Botón de actualizar
                Button(
                    onClick = {
                        scope.launch {
                            val updatedData = mutableMapOf<String, Any>()

                            // Solo actualizamos los campos si el usuario ha cambiado algo
                            if (newName.isNotEmpty()) updatedData["name"] = newName
                            if (newStatus.isNotEmpty()) updatedData["status"] = newStatus
                            if (newSpecies.isNotEmpty()) updatedData["species"] = newSpecies
                            if (newEpisodeId.isNotEmpty()) updatedData["episode_id"] = newEpisodeId
                            if (newImageUrl.isNotEmpty()) updatedData["imagenUrl"] = newImageUrl

                            if (updatedData.isNotEmpty()) {
                                try {
                                    firestoreService.updateCharacter(documentId, updatedData)
                                    navController.popBackStack() // Volver a la pantalla anterior tras modificar
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0288D1))
                ) {
                    Text("Guardar Cambios", color = Color.White)
                }
            }
        }
    }
}
