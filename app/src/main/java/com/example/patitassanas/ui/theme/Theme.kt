package com.example.patitassanas.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Paleta de la propuesta de diseño
val AzulPatitas = Color(0xFF3B82C4)   // Agendar cita, Guardar, Volver al inicio
val VerdePatitas = Color(0xFF4CAF7A)  // Nuestros servicios
val GrisPatitas = Color(0xFF8A9199)   // Salir, Cancelar
val AzulOscuro = Color(0xFF12294A)    // Títulos
val FondoClaro = Color(0xFFF3F8FC)
val TarjetaGris = Color(0xFFE3E8EC)

private val Esquema = lightColorScheme(
    primary = AzulPatitas,
    secondary = VerdePatitas,
    tertiary = GrisPatitas,
    background = FondoClaro,
    surface = Color.White,
    onPrimary = Color.White,
    onBackground = AzulOscuro,
    onSurface = AzulOscuro
)

@Composable
fun PatitasSanasTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = Esquema, content = content)
}