package com.example.proyecto_francisco_marquez.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.proyecto_francisco_marquez.ui.ModernButton
import com.example.proyecto_francisco_marquez.ui.TitleStyle
import com.example.proyecto_francisco_marquez.ui.gradientBackground
import com.example.proyecto_francisco_marquez.viewmodel.CharactersViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterScreen(navController: NavHostController, viewModel: CharactersViewModel = viewModel()) {
    var selectedFilter by remember { mutableStateOf("All") }
    val statusOptions = listOf("All", "Alive", "Dead")
    var expanded by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var characterToModify by remember { mutableStateOf("") }
    var characterToDelete by remember { mutableStateOf("") }
    var newCharacter by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Menu", style = TitleStyle) },
                actions = {
                    IconButton(onClick = {
                        navController.navigate("login") {
                            popUpTo("login") { inclusive = true }
                        }
                    }) {
                        Icon(Icons.Filled.Logout, contentDescription = "Log Out")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp).gradientBackground(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Search Character", style = TitleStyle)
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        label = { Text("Enter character name") },
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                    )
                    ModernButton(text = "Search", onClick = {
                        navController.navigate("characterScreen/$selectedFilter/$searchQuery")
                    })
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Filter by Status", style = TitleStyle)
                    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
                        OutlinedTextField(
                            value = selectedFilter,
                            onValueChange = {},
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
                    ModernButton(text = "Filter", onClick = {
                        navController.navigate("characterScreen/$selectedFilter")
                    })
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Modify Character", style = TitleStyle)
                    OutlinedTextField(
                        value = characterToModify,
                        onValueChange = { characterToModify = it },
                        label = { Text("Enter Character ID") },
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                    )
                    ModernButton(text = "Modify", onClick = {
                        navController.navigate("modifyCharacterScreen/$characterToModify")
                    })
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Delete Character", style = TitleStyle)
                    OutlinedTextField(
                        value = characterToDelete,
                        onValueChange = { characterToDelete = it },
                        label = { Text("Enter Character ID") },
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                    )
                    ModernButton(text = "Delete", onClick = {
                        navController.navigate("deleteCharacterScreen/$characterToDelete")
                    })
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Add Character", style = TitleStyle)
                    OutlinedTextField(
                        value = newCharacter,
                        onValueChange = { newCharacter = it },
                        label = { Text("Enter New Character Name") },
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                    )
                    ModernButton(text = "Add", onClick = {
                        navController.navigate("addCharacterScreen/$newCharacter")
                    })
                }
            }
        }
    }
}
