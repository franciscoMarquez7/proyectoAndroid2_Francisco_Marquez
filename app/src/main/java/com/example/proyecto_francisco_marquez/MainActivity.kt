package com.example.proyecto_francisco_marquez

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.proyecto_francisco_marquez.ui.screen.*
import com.example.proyecto_francisco_marquez.ui.theme.Proyecto_Francisco_MarquezTheme
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicialización de Firebase
        try {
            FirebaseApp.initializeApp(this)
        } catch (e: Exception) {
            Log.e("Firebase", "Error inicializando Firebase: ${e.message}")
        }

        // Verificación de Google Play Services
        checkGooglePlayServices()

        // Verificar usuario autenticado
        try {
            val auth = FirebaseAuth.getInstance()
            Log.d("AuthCheck", "Usuario actual: ${auth.currentUser?.email ?: "No autenticado"}")
        } catch (e: Exception) {
            Log.e("Auth", "Error verificando autenticación: ${e.message}")
        }

        setContent {
            Proyecto_Francisco_MarquezTheme {
                val navController = rememberNavController()
                AppNavigation(navController)
            }
        }
    }

    private fun checkGooglePlayServices() {
        val googleApiAvailability = GoogleApiAvailability.getInstance()
        val resultCode = googleApiAvailability.isGooglePlayServicesAvailable(this)
        
        if (resultCode != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(resultCode)) {
                googleApiAvailability.getErrorDialog(this, resultCode, 9000)?.show()
            } else {
                Log.e("GooglePlayServices", "Este dispositivo no es compatible con Google Play Services")
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val googleApiAvailability = GoogleApiAvailability.getInstance()
        val resultCode = googleApiAvailability.isGooglePlayServicesAvailable(this)
        if (resultCode != ConnectionResult.SUCCESS) {
            googleApiAvailability.makeGooglePlayServicesAvailable(this)
        }
    }
}

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        composable("splash") { 
            SplashScreen(navController) 
        }
        
        composable("login") { 
            LoginScreen(navController) 
        }
        
        composable("filter") { 
            FilterScreen(navController) 
        }
        
        composable("characterScreen/{status}") { backStackEntry ->
            val status = backStackEntry.arguments?.getString("status")
            CharacterScreen(navController, status ?: "all")
        }
        
        composable("databaseScreen") { 
            DatabaseScreen(navController) 
        }
        
        composable("databasePersonajeScreen") { 
            DatabaseScreenPersonaje(navController) 
        }
        
        composable("databaseEpisodioScreen") { 
            DatabaseScreenEpisodio(navController) 
        }
        
        composable(
            route = "verPersonajesEpisodio/{episodeId}",
            arguments = listOf(
                navArgument("episodeId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val episodeId = backStackEntry.arguments?.getString("episodeId") ?: return@composable
            VerPersonajesEpisodioScreen(
                navController = navController,
                episodeId = episodeId
            )
        }
        
        composable("agregarPersonajeScreen") {
            AgregarPersonajeScreen(navController)
        }
        
        composable("agregarEpisodioScreen") {
            AgregarEpisodioScreen(navController)
        }
        
        composable(
            "modificarPersonajeScreen/{characterId}",
            arguments = listOf(
                navArgument("characterId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val characterId = backStackEntry.arguments?.getString("characterId") ?: return@composable
            ModificarPersonajeScreen(navController, characterId)
        }
        
        composable(
            "modificarEpisodioScreen/{episodeId}",
            arguments = listOf(
                navArgument("episodeId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val episodeId = backStackEntry.arguments?.getString("episodeId") ?: return@composable
            ModificarEpisodioScreen(navController, episodeId)
        }
    }
}
