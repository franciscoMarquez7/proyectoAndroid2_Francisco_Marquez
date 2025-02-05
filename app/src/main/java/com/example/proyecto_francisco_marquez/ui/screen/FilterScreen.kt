package com.example.proyecto_francisco_marquez.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.proyecto_francisco_marquez.ui.gradientBackground
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterScreen(navController: NavHostController) {
    var selectedFilter by remember { mutableStateOf("All") }
    val statusOptions = listOf("All", "Alive", "Dead")
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp).gradientBackground(),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Select Character Status", style = MaterialTheme.typography.headlineLarge)

        Spacer(modifier = Modifier.height(16.dp))

        // Menú desplegable corregido
        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
            OutlinedTextField(
                value = selectedFilter,
                onValueChange = { /* No se usa porque el cambio ocurre con el menú */ },
                label = { Text("Choose Status") },
                readOnly = true,
                modifier = Modifier.fillMaxWidth().menuAnchor().clickable { expanded = true }
            )
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                statusOptions.forEach { status ->
                    DropdownMenuItem(
                        text = { Text(status) },
                        onClick = {
                            selectedFilter = status
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para aplicar el filtro y navegar a CharacterScreen
        Button(
            onClick = { navController.navigate("characterScreen/$selectedFilter") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Filter")
        }

        // Botón para cerrar sesión
        Button(
            onClick = {
                FirebaseAuth.getInstance().signOut()
                navController.navigate("login") {
                    popUpTo("login") { inclusive = true }
                }
            },
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
        ) {
            Text("Log Out")
        }
    }
}
