package com.example.patitassanas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.patitassanas.ui.PantallaAgendar
import com.example.patitassanas.ui.PantallaInicio
import com.example.patitassanas.ui.PantallaResumen
import com.example.patitassanas.ui.PantallaServicios
import com.example.patitassanas.ui.theme.PatitasSanasTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PatitasSanasTheme {
                Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Box(Modifier.systemBarsPadding()) {
                        AppPatitasSanas(onSalir = { finish() }) // Salir → cierra la app
                    }
                }
            }
        }
    }
}

enum class Pantalla { INICIO, AGENDAR, RESUMEN, SERVICIOS }

data class Cita(
    val propietario: String,
    val telefono: String,
    val fecha: String,
    val hora: String,
    val raza: String,
    val tamano: String
)

// Permite conservar la cita al rotar la pantalla
private val CitaSaver = listSaver<Cita?, String>(
    save = { c -> if (c == null) emptyList() else listOf(c.propietario, c.telefono, c.fecha, c.hora, c.raza, c.tamano) },
    restore = { l -> if (l.isEmpty()) null else Cita(l[0], l[1], l[2], l[3], l[4], l[5]) }
)

@Composable
fun AppPatitasSanas(onSalir: () -> Unit) {
    var pantalla by rememberSaveable { mutableStateOf(Pantalla.INICIO) }
    var cita by rememberSaveable(stateSaver = CitaSaver) { mutableStateOf<Cita?>(null) }

    // El botón "atrás" del teléfono regresa al inicio desde cualquier pantalla interna
    BackHandler(enabled = pantalla != Pantalla.INICIO) { pantalla = Pantalla.INICIO }

    when (pantalla) {
        Pantalla.INICIO -> PantallaInicio(
            onAgendar = { pantalla = Pantalla.AGENDAR },
            onServicios = { pantalla = Pantalla.SERVICIOS },
            onSalir = onSalir
        )
        Pantalla.AGENDAR -> PantallaAgendar(
            onCancelar = { pantalla = Pantalla.INICIO },            // sin guardar
            onGuardar = { nueva -> cita = nueva; pantalla = Pantalla.RESUMEN }
        )
        Pantalla.RESUMEN -> cita?.let { PantallaResumen(it) { pantalla = Pantalla.INICIO } }
        Pantalla.SERVICIOS -> PantallaServicios { pantalla = Pantalla.INICIO }
    }
}