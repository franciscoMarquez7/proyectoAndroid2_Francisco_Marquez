package com.example.proyecto_francisco_marquez.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.proyecto_francisco_marquez.ui.TitleStyle
import com.example.proyecto_francisco_marquez.data.FirestoreService
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgregarPersonajeScreen(navController: NavHostController) {
    var name by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("") }
    var species by remember { mutableStateOf("") }
    var episodeId by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }

    // Usamos el scope de corrutina
    val scope = rememberCoroutineScope()

    val firestoreService = FirestoreService()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Agregar Personaje", style = TitleStyle) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Agregar un nuevo personaje", fontSize = 20.sp, modifier = Modifier.padding(bottom = 16.dp))

            // Campos de texto para agregar el personaje
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            )

            OutlinedTextField(
                value = status,
                onValueChange = { status = it },
                label = { Text("Estado") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            )

            OutlinedTextField(
                value = species,
                onValueChange = { species = it },
                label = { Text("Especie") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            )

            OutlinedTextField(
                value = episodeId,
                onValueChange = { episodeId = it },
                label = { Text("ID del episodio") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            )

            OutlinedTextField(
                value = imageUrl,
                onValueChange = { imageUrl = it },
                label = { Text("URL de la imagen") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            )

            Button(
                onClick = {
                    // Llamar a la función suspend usando una corrutina
                    scope.launch {
                        // Crear un objeto de datos para agregar al Firestore
                        val character = mapOf(
                            "name" to name,
                            "status" to status,
                            "species" to species,
                            "episode_id" to episodeId,
                            "imagenUrl" to imageUrl
                        )
                        // Llamar al servicio de Firestore para agregar el personaje
                        val success = firestoreService.addCharacter(character)
                        if (success) {
                            // Volver atrás después de agregar el personaje
                            navController.popBackStack()
                        } else {
                            // Mostrar un mensaje de error si no se pudo agregar el personaje
                        }
                    }
                },
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth().padding(8.dp).height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
            ) {
                Text("Agregar Personaje", color = Color.White)
            }
        }
    }
}
