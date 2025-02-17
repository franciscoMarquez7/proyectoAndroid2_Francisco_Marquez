package com.example.proyecto_francisco_marquez.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.proyecto_francisco_marquez.R
import com.example.proyecto_francisco_marquez.ui.TitleStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatabaseScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Base de Datos", style = TitleStyle) },
                navigationIcon = {
                    // Botón de retroceso con un emoticono
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Imagen de fondo con opacidad sutil en negro
            Image(
                painter = painterResource(id = R.drawable.imagen_fondo), // Imagen desde drawable
                contentDescription = "Imagen de Fondo",
                modifier = Modifier.fillMaxSize().alpha(0.05f), // Opacidad muy sutil
                contentScale = ContentScale.Crop
            )

            // Contenedor centralizado
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center // Centrar los botones en la pantalla
            ) {
                Text(
                    "Gestión de Base de Datos",
                    style = TitleStyle,
                    modifier = Modifier.padding(bottom = 32.dp),
                    fontSize = 24.sp
                )

                // Botón para gestionar personajes
                Button(
                    onClick = { navController.navigate("databasePersonajeScreen") },
                    shape = RoundedCornerShape(20.dp), // Bordes redondeados
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .height(56.dp), // Mayor altura para mejorar la estética
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE)) // Color atractivo
                ) {
                    Text(
                        "Gestionar Personajes",
                        fontSize = 18.sp,
                        color = Color.White
                    )
                }

                // Botón para gestionar episodios
                Button(
                    onClick = { navController.navigate("databaseEpisodioScreen") },
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF018786)) // Color atractivo
                ) {
                    Text(
                        "Gestionar Episodios",
                        fontSize = 18.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}
