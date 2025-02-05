package com.example.proyecto_francisco_marquez.ui

import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Modifier

fun Modifier.gradientBackground(): Modifier {
    return this.background(
        brush = Brush.linearGradient(
            colors = listOf(
                Color(0xFF69F0AE), // Verde claro vibrante
                Color(0xFF00E676), // Verde portal de Rick
                Color(0xFF1DE9B6), // Verde agua brillante
                Color(0xFF64FFDA)  // Verde neón
            )
        )
    )
}