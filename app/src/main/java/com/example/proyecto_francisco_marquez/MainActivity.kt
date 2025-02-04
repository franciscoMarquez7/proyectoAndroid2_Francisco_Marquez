package com.example.proyecto_francisco_marquez

import Navegacion
import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.proyecto_francisco_marquez.ui.theme.Proyecto_Francisco_MarquezTheme
import com.google.firebase.auth.FirebaseAuth
import android.util.Log
import androidx.core.content.FileProvider
import java.io.File

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Verificar usuario autenticado
        val auth = FirebaseAuth.getInstance()
        Log.d("AuthCheck", "Usuario actual: ${auth.currentUser?.email ?: "No autenticado"}")

        setContent {
            Proyecto_Francisco_MarquezTheme {
                val navController = rememberNavController()
                Navegacion(navController = navController) // Llama a la navegación
            }
        }
    }
}
