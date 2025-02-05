package com.example.proyecto_francisco_marquez.ui.screen

import android.graphics.Bitmap
import android.widget.Toast
import android.graphics.drawable.BitmapDrawable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.imageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.example.proyecto_francisco_marquez.ui.gradientBackground
import kotlinx.coroutines.*

@Composable
fun CharacterDetailScreen(characterId: String?, navController: NavHostController) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    if (characterId.isNullOrEmpty()) {
        Text("Error: No character ID provided", style = MaterialTheme.typography.headlineLarge)
        return
    }

    val imageUrl = remember { "https://rickandmortyapi.com/api/character/avatar/$characterId.jpeg" }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .gradientBackground()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Mostrar imagen
        AsyncImage(
            model = imageUrl,
            contentDescription = "Character Image",
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        )

        Button(
            onClick = {
                coroutineScope.launch(Dispatchers.IO) {
                    val success = saveImageToGallery(imageUrl, context)
                    withContext(Dispatchers.Main) {
                        if (success) {
                            Toast.makeText(context, "Image saved!", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Failed to save image", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Save Image")
        }

        // Botón para volver atrás
        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Back")
        }
    }
}

suspend fun saveImageToGallery(imageUrl: String, context: android.content.Context): Boolean {
    return try {
        val bitmap = withContext(Dispatchers.IO) {
            val request = ImageRequest.Builder(context)
                .data(imageUrl)
                .allowHardware(false)
                .build()
            val result = (context.imageLoader.execute(request) as? SuccessResult)?.drawable
            (result as? BitmapDrawable)?.bitmap
        }

        if (bitmap == null) return false

        val filename = "Character_${System.currentTimeMillis()}.png"

        val contentValues = android.content.ContentValues().apply {
            put(android.provider.MediaStore.Images.Media.DISPLAY_NAME, filename)
            put(android.provider.MediaStore.Images.Media.MIME_TYPE, "image/png")
            put(android.provider.MediaStore.Images.Media.RELATIVE_PATH, "Pictures/RickAndMortyApp")
        }

        val uri = context.contentResolver.insert(
            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        ) ?: return false

        context.contentResolver.openOutputStream(uri)?.use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        }

        withContext(Dispatchers.Main) {
            Toast.makeText(context, "Image saved in gallery!", Toast.LENGTH_SHORT).show()
        }

        true
    } catch (e: Exception) {
        withContext(Dispatchers.Main) {
            Toast.makeText(context, "Failed to save image", Toast.LENGTH_SHORT).show()
        }
        e.printStackTrace()
        false
    }
}




