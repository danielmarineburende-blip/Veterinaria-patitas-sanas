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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.patitassanas.ui.PantallaCitas
import com.example.patitassanas.ui.PantallaConsultas
import com.example.patitassanas.ui.PantallaInicio
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
                        AppPatitasSanas(onSalir = { finish() })
                    }
                }
            }
        }
    }
}

enum class Pantalla { INICIO, CITAS, CONSULTAS, SERVICIOS }

@Composable
fun AppPatitasSanas(onSalir: () -> Unit) {
    var pantalla by rememberSaveable { mutableStateOf(Pantalla.INICIO) }
    // id de la cita que se va a editar en el formulario (0 = cita nueva)
    var idEditar by rememberSaveable { mutableIntStateOf(0) }

    // Atrás: desde la lista regresa al formulario; desde las demás pantallas, al inicio
    BackHandler(enabled = pantalla != Pantalla.INICIO) {
        if (pantalla == Pantalla.CONSULTAS) { idEditar = 0; pantalla = Pantalla.CITAS }
        else pantalla = Pantalla.INICIO
    }

    when (pantalla) {
        Pantalla.INICIO -> PantallaInicio(
            onAgendar = { idEditar = 0; pantalla = Pantalla.CITAS },
            onServicios = { pantalla = Pantalla.SERVICIOS },
            onSalir = onSalir
        )
        Pantalla.CITAS -> PantallaCitas(
            idEditar = idEditar,
            onConsultar = { pantalla = Pantalla.CONSULTAS },
            onVolver = { pantalla = Pantalla.INICIO }
        )
        Pantalla.CONSULTAS -> PantallaConsultas(
            onEditar = { id -> idEditar = id; pantalla = Pantalla.CITAS },
            onVolver = { idEditar = 0; pantalla = Pantalla.CITAS }
        )
        Pantalla.SERVICIOS -> PantallaServicios { pantalla = Pantalla.INICIO }
    }
}