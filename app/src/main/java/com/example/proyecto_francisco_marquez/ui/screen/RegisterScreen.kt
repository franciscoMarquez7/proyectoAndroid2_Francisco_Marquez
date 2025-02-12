package com.example.proyecto_francisco_marquez.ui.screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.proyecto_francisco_marquez.ui.ModernButton
import com.example.proyecto_francisco_marquez.ui.SubtitleStyle
import com.example.proyecto_francisco_marquez.ui.TitleStyle
import com.example.proyecto_francisco_marquez.ui.gradientBackground
import com.example.proyecto_francisco_marquez.viewmodel.AuthViewModel

@Composable
fun RegisterScreen(navController: NavHostController) {
    val authViewModel: AuthViewModel = viewModel()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current

    Box(
        modifier = Modifier.fillMaxSize().padding(16.dp).gradientBackground()
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center).fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Create Account",
                style = TitleStyle,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            )

            ModernButton(text = "Register", onClick = {
                authViewModel.register(email, password) { success, errorMessage ->
                    if (success) {
                        navController.navigate("login")
                    } else {
                        Toast.makeText(context, "Error: $errorMessage", Toast.LENGTH_LONG).show()
                    }
                }
            })

            TextButton(onClick = { navController.navigate("login") }) {
                Text("Already have an account? Log in", style = SubtitleStyle)
            }
        }
    }
}
