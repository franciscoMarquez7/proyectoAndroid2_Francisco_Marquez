package com.example.proyecto_francisco_marquez.ui.screen

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.proyecto_francisco_marquez.R
import com.example.proyecto_francisco_marquez.viewmodel.AuthViewModel
import com.example.proyecto_francisco_marquez.utils.InicioConGoogle

@Composable
fun LoginScreen(navController: NavHostController) {
    val authViewModel: AuthViewModel = viewModel()
    val context = LocalContext.current

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Google Sign-In launcher
    val launcher = rememberLauncherForActivityResult(
        contract = InicioConGoogle()
    ) { result ->
        if (result != null) {
            authViewModel.loginWithGoogle(result) { success, errorMessage ->
                if (success) {
                    navController.navigate("home")
                } else {
                    Toast.makeText(context, "Error: $errorMessage", Toast.LENGTH_LONG).show()
                }
            }
        } else {
            Toast.makeText(context, "Google Sign-In cancelled", Toast.LENGTH_LONG).show()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // App Logo
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "App Logo",
                modifier = Modifier.size(120.dp).padding(bottom = 16.dp)
            )

            // Title
            Text(
                text = "Rick And Morty",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Email Input
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            )

            // Password Input
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                visualTransformation = PasswordVisualTransformation()
            )

            // Login Button
            Button(
                onClick = {
                    authViewModel.login(email, password) { success, errorMessage ->
                        if (success) {
                            navController.navigate("home")
                        } else {
                            Toast.makeText(context, "Error: $errorMessage", Toast.LENGTH_LONG).show()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            ) {
                Text("Login")
            }

            // Google Sign-In Button
            Button(
                onClick = { launcher.launch(Unit) },
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            ) {
                Text("Continue with Google")
            }

            // Navigation Options
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
            ) {
                TextButton(onClick = { navController.navigate("register") }) {
                    Text("Registrar")
                }
                TextButton(onClick = { navController.navigate("forgotPassword") }) {
                    Text("Has olvidado la contraseña?")
                }
            }
        }
    }
}
