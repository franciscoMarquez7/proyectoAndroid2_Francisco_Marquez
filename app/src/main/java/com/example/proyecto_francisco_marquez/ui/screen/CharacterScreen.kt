package com.example.proyecto_francisco_marquez.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.proyecto_francisco_marquez.ui.ModernButton
import com.example.proyecto_francisco_marquez.ui.ModernCard
import com.example.proyecto_francisco_marquez.ui.SubtitleStyle
import com.example.proyecto_francisco_marquez.ui.TitleStyle
import com.example.proyecto_francisco_marquez.ui.gradientBackground
import com.example.proyecto_francisco_marquez.viewmodel.CharactersViewModel

@Composable
fun CharacterScreen(navController: NavHostController, filter: String, searchQuery: String = "", viewModel: CharactersViewModel = viewModel()) {
    val characters by viewModel.characters.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val scrollState = rememberScrollState()

    val filteredCharacters = characters.filter {
        (filter == "All" || it.status.equals(filter, ignoreCase = true)) &&
                (searchQuery.isEmpty() || it.name.contains(searchQuery, ignoreCase = true))
    }

    Box(modifier = Modifier.fillMaxSize().padding(16.dp).gradientBackground()) {
        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Character List", style = TitleStyle, modifier = Modifier.padding(bottom = 16.dp))

            if (viewModel.isLoading.collectAsState().value) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else if (errorMessage != null) {
                Text("Error: $errorMessage", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.error)
            } else if (filteredCharacters.isEmpty()) {
                Text("No characters available", style = MaterialTheme.typography.bodyLarge)
            } else {
                filteredCharacters.forEach { character ->
                    ModernCard {
                        Row(
                            modifier = Modifier.fillMaxWidth().clickable {
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
                                Text(text = "Name:  ${character.name}",style = MaterialTheme.typography.bodyMedium)
                                Text(text = "Status: ${character.status}", style = MaterialTheme.typography.bodyMedium)
                                Text(text = "Species: ${character.species}", style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            ModernButton(text = "Change Filter", onClick = { navController.navigate("filterScreen") })
        }
    }
}
