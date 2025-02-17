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
import com.example.proyecto_francisco_marquez.data.FirestoreService
import com.example.proyecto_francisco_marquez.ui.TitleStyle
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EliminarPersonajeScreen(navController: NavHostController, characterName: String) {
    val firestoreService = FirestoreService()

    // Usamos un CoroutineScope para ejecutar la función suspend
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Eliminar Personaje", style = TitleStyle) },
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
            Text("¿Estás seguro de eliminar a $characterName?", fontSize = 20.sp, modifier = Modifier.padding(bottom = 16.dp))

            Button(
                onClick = {
                    // Llamar a la función suspend usando una corrutina
                    scope.launch {
                        // Llamamos a Firestore para eliminar el personaje
                        val success = firestoreService.deleteCharacter(characterName)
                        if (success) {
                            // Volver atrás después de la eliminación
                            navController.popBackStack()
                        } else {
                            // Mostrar un mensaje de error
                        }
                    }
                },
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth().padding(8.dp).height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF018786))
            ) {
                Text("Eliminar Personaje", color = Color.White)
            }
        }
    }
}
