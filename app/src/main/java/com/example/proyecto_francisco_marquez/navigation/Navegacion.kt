import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.proyecto_francisco_marquez.ui.screen.AgregarPersonajeScreen
import com.example.proyecto_francisco_marquez.ui.screen.CharacterDetailScreen
import com.example.proyecto_francisco_marquez.ui.screen.CharacterScreen
import com.example.proyecto_francisco_marquez.ui.screen.DatabaseScreen
import com.example.proyecto_francisco_marquez.ui.screen.DatabaseScreenPersonaje
import com.example.proyecto_francisco_marquez.ui.screen.EliminarPersonajeScreen
import com.example.proyecto_francisco_marquez.ui.screen.FilterScreen
import com.example.proyecto_francisco_marquez.ui.screen.ForgotPasswordScreen
import com.example.proyecto_francisco_marquez.ui.screen.LoginScreen
import com.example.proyecto_francisco_marquez.ui.screen.ModificarPersonajeScreen
import com.example.proyecto_francisco_marquez.ui.screen.RegisterScreen
import com.google.firebase.auth.FirebaseAuth

@Composable
fun Navegacion(navController: NavHostController) {
    val auth = FirebaseAuth.getInstance()
    val startDestination = if (auth.currentUser != null) "filterScreen" else "login"

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable("login") { LoginScreen(navController) }
        composable("register") { RegisterScreen(navController) }
        composable("forgotPassword") { ForgotPasswordScreen(navController) }
        composable("filterScreen") { FilterScreen(navController) }
        composable("databaseScreen") { DatabaseScreen(navController) }
        composable("databasePersonajeScreen") {
            DatabaseScreenPersonaje(navController)
        }
        composable("modificarPersonajeScreen/{characterName}") { backStackEntry ->
            val characterName = backStackEntry.arguments?.getString("characterName")
            ModificarPersonajeScreen(navController, characterName ?: "")
        }
        composable("agregarPersonajeScreen") {
            AgregarPersonajeScreen(navController)
        }
        composable("characterScreen/{filter}") { backStackEntry ->
            val filter = backStackEntry.arguments?.getString("filter") ?: "All"
            CharacterScreen(navController = navController, filter = filter)
        }
        composable("eliminarPersonajeScreen/{characterId}/{characterName}") { backStackEntry ->
            val characterId = backStackEntry.arguments?.getString("characterId") ?: ""
            val characterName = backStackEntry.arguments?.getString("characterName") ?: ""
            EliminarPersonajeScreen(navController, characterId, characterName)
        }
        composable("characterDetail/{characterId}") { backStackEntry ->
            val characterId = backStackEntry.arguments?.getString("characterId")
            CharacterDetailScreen(characterId = characterId, navController = navController)
        }
    }
}

