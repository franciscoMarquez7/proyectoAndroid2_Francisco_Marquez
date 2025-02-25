package com.example.proyecto_francisco_marquez.ui.screen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.proyecto_francisco_marquez.R
import com.example.proyecto_francisco_marquez.viewmodel.AuthViewModel
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.sp

@Composable
fun ForgotPasswordScreen(navController: NavHostController) {
    val authViewModel: AuthViewModel = viewModel()
    var email by remember { mutableStateOf("") }
    val context = LocalContext.current

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Imagen de fondo con efecto de oscurecimiento
        Image(
            painter = painterResource(id = R.drawable.imagen_fondo),
            contentDescription = "Fondo",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = 0.3f
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            Image(
                painter = painterResource(id = R.mipmap.imagen_logo_foreground),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(180.dp)
                    .padding(8.dp),
                contentScale = ContentScale.Fit
            )

            Text(
                text = "Recuperar Contraseña",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(bottom = 32.dp)
                    .shadow(elevation = 4.dp)
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email", color = Color.Black) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Black,
                    unfocusedBorderColor = Color.Black,
                    focusedLabelColor = Color.Black,
                    unfocusedLabelColor = Color.Black,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                ),
                shape = RoundedCornerShape(12.dp)
            )

            Button(
                onClick = {
                    authViewModel.sendPasswordResetEmail(
                        email = email,
                        onResult = { success, errorMessage ->
                            if (success) {
                                Toast.makeText(context, "Correo de recuperación enviado", Toast.LENGTH_SHORT).show()
                                navController.navigate("login")
                            } else {
                                Toast.makeText(context, "Error: $errorMessage", Toast.LENGTH_SHORT).show()
                            }
                        }
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(horizontal = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black.copy(alpha = 0.8f)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Enviar Correo", fontSize = 16.sp, color = Color.White)
            }

            TextButton(onClick = { navController.navigate("login") }) {
                Text(
                    "Volver al Login",
                    color = Color.Black,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
