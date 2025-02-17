package com.example.proyecto_francisco_marquez.ui.screen

import androidx.compose.foundation.Image
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
import com.example.proyecto_francisco_marquez.R
import com.example.proyecto_francisco_marquez.data.FirestoreService
import com.example.proyecto_francisco_marquez.ui.TitleStyle
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModificarPersonajeScreen(navController: NavHostController, characterName: String) {
    var newName by remember { mutableStateOf(characterName) }
    var newStatus by remember { mutableStateOf("") }

    // Instanciamos FirestoreService
    val firestoreService = FirestoreService()

    // Usamos un CoroutineScope para ejecutar la función suspend
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Modificar Personaje", style = TitleStyle) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
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
            // Imagen de fondo con opacidad sutil en negro
            Image(
                painter = painterResource(id = R.drawable.imagen_fondo), // Imagen desde drawable
                contentDescription = "Fondo de modificación",
                modifier = Modifier.fillMaxSize().alpha(0.05f), // Opacidad muy sutil
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Modificar: $characterName", fontSize = 20.sp, modifier = Modifier.padding(bottom = 16.dp))

                OutlinedTextField(
                    value = newName,
                    onValueChange = { newName = it },
                    label = { Text("Nuevo nombre") },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = newStatus,
                    onValueChange = { newStatus = it },
                    label = { Text("Nuevo estado") },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                )

                Button(
                    onClick = {
                        // Llamar a la función suspend usando una corrutina
                        scope.launch {
                            val updatedData = mapOf(
                                "name" to newName,
                                "status" to newStatus
                            )
                            // Llamamos a Firestore para actualizar el personaje
                            val success = firestoreService.updateCharacter(characterName, updatedData)
                            if (success) {
                                // Volver atrás después de la actualización
                                navController.popBackStack()
                            } else {
                                // Mostrar un mensaje de error
                            }
                        }
                    },
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.fillMaxWidth().padding(8.dp).height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
                ) {
                    Text("Actualizar", color = Color.White)
                }
            }
        }
    }
}
