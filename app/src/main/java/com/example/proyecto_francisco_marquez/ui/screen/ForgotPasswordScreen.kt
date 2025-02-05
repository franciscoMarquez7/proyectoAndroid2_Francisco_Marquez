package com.example.proyecto_francisco_marquez.ui.screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.proyecto_francisco_marquez.ui.gradientBackground
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun ForgotPasswordScreen(navController: NavHostController) {
    val auth = Firebase.auth
    var email by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp).gradientBackground()) {
        Text(text = "Forgot Password Screen")
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") })

        Button(onClick = {
            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(navController.context, "Password Reset Email Sent", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(navController.context, "Error Sending Email", Toast.LENGTH_SHORT).show()
                    }
                }
        }) {
            Text("Reset Password")
        }

        Button(onClick = { navController.navigate("login") }) {
            Text("Back to Login")
        }
    }
}
