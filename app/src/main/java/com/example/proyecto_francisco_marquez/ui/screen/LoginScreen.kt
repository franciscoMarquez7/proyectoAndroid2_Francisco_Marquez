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
import com.example.proyecto_francisco_marquez.utils.InicioConGoogle
import com.example.proyecto_francisco_marquez.ui.ModernButton
import com.example.proyecto_francisco_marquez.ui.SubtitleStyle
import com.example.proyecto_francisco_marquez.ui.TitleStyle
import com.example.proyecto_francisco_marquez.ui.gradientBackground
import com.example.proyecto_francisco_marquez.viewmodel.AuthViewModel

@Composable
fun LoginScreen(navController: NavHostController) {
    val authViewModel: AuthViewModel = viewModel()
    val context = LocalContext.current
    val user by authViewModel.userState.collectAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val launcher = rememberLauncherForActivityResult(
        contract = InicioConGoogle()
    ) { result ->
        if (result != null) {
            authViewModel.loginWithGoogle(result) { success, errorMessage ->
                if (success) {
                    navController.navigate("filterScreen") {
                        popUpTo("login") { inclusive = true }
                    }
                } else {
                    Toast.makeText(context, "Error: $errorMessage", Toast.LENGTH_LONG).show()
                }
            }
        } else {
            Toast.makeText(context, "Inicio con Google cancelado", Toast.LENGTH_LONG).show()
        }
    }

    // Verificar si el usuario ya está autenticado y redirigir automáticamente
    LaunchedEffect(user) {
        if (user != null) {
            navController.navigate("filterScreen") {
                popUpTo("login") { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize().gradientBackground()
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.app_logo),
                contentDescription = "App Logo",
                modifier = Modifier.size(170.dp).padding(bottom = 16.dp)
            )

            Text(
                text = "Rick And Morty",
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
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                visualTransformation = PasswordVisualTransformation()
            )

            ModernButton(text = "Login", onClick = {
                if (email.isNotEmpty() && password.isNotEmpty()) {
                    authViewModel.login(email, password) { success, errorMessage ->
                        if (success) {
                            navController.navigate("filterScreen") {
                                popUpTo("login") { inclusive = true }
                            }
                        } else {
                            val message = errorMessage ?: "Email o contraseña incorrecta"
                            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                        }
                    }
                } else {
                    Toast.makeText(context, "Por favor introduzca email y contraseña correctos", Toast.LENGTH_LONG).show()
                }
            })

            ModernButton(text = "Continuar con Google", onClick = { launcher.launch(Unit) })

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
            ) {
                TextButton(onClick = { navController.navigate("register") }) {
                    Text("Registrar", style = SubtitleStyle)
                }
                TextButton(onClick = { navController.navigate("forgotPassword") }) {
                    Text("¿Has olvidado la contraseña?", style = SubtitleStyle)
                }
            }
        }
    }
}
