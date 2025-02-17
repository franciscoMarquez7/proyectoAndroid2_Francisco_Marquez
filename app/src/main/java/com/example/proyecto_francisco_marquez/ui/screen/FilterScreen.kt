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
import com.example.proyecto_francisco_marquez.ui.TitleStyle
import com.example.proyecto_francisco_marquez.ui.gradientBackground
import com.example.proyecto_francisco_marquez.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterScreen(navController: NavHostController) {
    val authViewModel: AuthViewModel = viewModel()
    val user by authViewModel.userState.collectAsState()

    var selectedFilter by remember { mutableStateOf("All") }
    val statusOptions = listOf("All", "Alive", "Dead", "Unknown")
    var expanded by remember { mutableStateOf(false) }

    LaunchedEffect(user) {
        if (user == null) {
            navController.navigate("login") {
                popUpTo("filterScreen") { inclusive = true }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Menu", style = TitleStyle) },
                actions = {
                    IconButton(onClick = {
                        authViewModel.logout()
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .gradientBackground()
                .padding(paddingValues)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Filtrar por estado", style = TitleStyle, modifier = Modifier.padding(bottom = 8.dp))
                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = selectedFilter,
                        onValueChange = {},
                        label = { Text("Elegir estado") },
                        readOnly = true,
                        modifier = Modifier.fillMaxWidth().clickable { expanded = !expanded }
                    )
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
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
                Button(onClick = { navController.navigate("characterScreen/$selectedFilter") }) {
                    Text("Filtrar")
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { navController.navigate("databaseScreen") }) {
                    Text("Base de Datos")
                }
            }
        }
    }
}
