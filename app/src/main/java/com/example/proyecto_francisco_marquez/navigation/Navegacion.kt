@file:Suppress("NAME_SHADOWING")

import android.annotation.SuppressLint
import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.proyecto_francisco_marquez.ui.screen.LoginScreen
import com.example.proyecto_francisco_marquez.ui.screen.RegisterScreen
import com.google.firebase.auth.FirebaseAuth
import android.util.Log
import com.example.proyecto_francisco_marquez.ui.screen.CharacterScreen
import com.example.proyecto_francisco_marquez.ui.screen.FilterScreen
import com.example.proyecto_francisco_marquez.ui.screen.ForgotPasswordScreen
import com.example.proyecto_francisco_marquez.ui.screen.CharacterDetailScreen
import kotlinx.coroutines.delay

@SuppressLint("ComposableDestinationInComposeScope")
@Composable
fun Navegacion(navController: NavHostController) {
    val auth = FirebaseAuth.getInstance()
    var startDestination by remember { mutableStateOf("login") } // Default to login

    LaunchedEffect(Unit) {
        delay(1000) // Reducido a 1 segundo para evitar bloqueos
        startDestination = if (auth.currentUser != null) "filterScreen" else "login"
        Log.d("AuthCheck", "Usuario actual: ${auth.currentUser?.email ?: "No autenticado"}")
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable("login") { LoginScreen(navController) }
        composable("register") { RegisterScreen(navController) }
        composable("forgotPassword") { ForgotPasswordScreen(navController) }
        composable("filterScreen") { FilterScreen(navController) }
        composable("characterScreen/{filter}") { backStackEntry ->
            val filter = backStackEntry.arguments?.getString("filter") ?: "All"
            CharacterScreen(navController = navController, filter = filter)
        }
        composable("characterDetail/{characterId}") { backStackEntry ->
            val characterId = backStackEntry.arguments?.getString("characterId")
            CharacterDetailScreen(characterId = characterId, navController = navController)
        }
    }
}
