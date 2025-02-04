package com.example.proyecto_francisco_marquez.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.proyecto_francisco_marquez.viewmodel.CharactersViewModel

@Composable
fun CharacterScreen(navController: NavHostController, filter: String, viewModel: CharactersViewModel = viewModel()) {
    val characters by viewModel.characters.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    // Filtrar los personajes según el filtro seleccionado
    val filteredCharacters = when (filter) {
        "All" -> characters
        else -> characters.filter { it.status.equals(filter, ignoreCase = true) }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        if (viewModel.isLoading.collectAsState().value) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else if (errorMessage != null) {
            Text("Error: $errorMessage", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.error)
        } else if (filteredCharacters.isEmpty()) {
            Text("No characters available", style = MaterialTheme.typography.bodyLarge)
        } else {
            filteredCharacters.forEach { character ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable {
                            navController.navigate("characterDetail/${character.id}")
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = character.image,
                        contentDescription = character.name,
                        modifier = Modifier.size(80.dp)
                    )
                    Column(modifier = Modifier.padding(start = 8.dp)) {
                        Text(text = character.name, style = MaterialTheme.typography.bodyLarge)
                        Text(text = "Status: ${character.status}", style = MaterialTheme.typography.bodyMedium)
                        Text(text = "Species: ${character.species}", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }

        // Botón para volver al filtro
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { navController.navigate("filterScreen") },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Change Filter")
        }
    }
}
