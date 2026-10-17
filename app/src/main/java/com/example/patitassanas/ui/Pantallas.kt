@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.patitassanas.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Bathtub
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.MonitorHeart
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material.icons.filled.Vaccines
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.patitassanas.Cita
import com.example.patitassanas.ui.theme.AzulOscuro
import com.example.patitassanas.ui.theme.AzulPatitas
import com.example.patitassanas.ui.theme.FondoClaro
import com.example.patitassanas.ui.theme.GrisPatitas
import com.example.patitassanas.ui.theme.TarjetaGris
import com.example.patitassanas.ui.theme.VerdePatitas
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

/* ───────────────────────── 1. INICIO ───────────────────────── */

@Composable
fun PantallaInicio(onAgendar: () -> Unit, onServicios: () -> Unit, onSalir: () -> Unit) {
    Box(
        Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(FondoClaro, Color(0xFFD6EBF8))))
    ) {
        // Fondo de mascotas (perro y gato) con transparencia
        Row(
            Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 8.dp)
                .alpha(0.45f),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CaraPerro(Modifier.size(150.dp))
            CaraGato(Modifier.size(130.dp))
        }

        Column(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 36.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.weight(1f))
            LogoPatitas(120.dp)
            Spacer(Modifier.height(8.dp))
            Text("Patitas Sanas", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = AzulOscuro)
            Spacer(Modifier.height(44.dp))
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                BotonAccion("Agendar cita", AzulPatitas, onAgendar, Modifier.fillMaxWidth(), Icons.Default.CalendarMonth)
                BotonAccion("Nuestros servicios", VerdePatitas, onServicios, Modifier.fillMaxWidth(), Icons.Default.MedicalServices)
                BotonAccion("Salir", GrisPatitas, onSalir, Modifier.fillMaxWidth(), Icons.AutoMirrored.Filled.ExitToApp)
            }
            Spacer(Modifier.weight(1f))
            Spacer(Modifier.height(150.dp)) // espacio para las mascotas
        }
    }
}

/* ───────────────────────── 2. AGENDAR CITA ───────────────────────── */

@Composable
fun PantallaAgendar(onCancelar: () -> Unit, onGuardar: (Cita) -> Unit) {
    var propietario by rememberSaveable { mutableStateOf("") }
    var telefono by rememberSaveable { mutableStateOf("") }
    var fecha by rememberSaveable { mutableStateOf("") }
    var hora by rememberSaveable { mutableStateOf("") }
    var raza by rememberSaveable { mutableStateOf("") }
    var tamano by rememberSaveable { mutableStateOf("") }
    var intento by remember { mutableStateOf(false) }
    var mostrarFecha by remember { mutableStateOf(false) }
    var mostrarHora by remember { mutableStateOf(false) }
    var expandido by remember { mutableStateOf(false) }

    val colores = coloresCampo()
    val forma = RoundedCornerShape(8.dp)

    Column(
        Modifier
            .fillMaxSize()
            .imePadding()
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        Encabezado()
        Spacer(Modifier.height(12.dp))
        Text(
            "Agendar cita", fontSize = 24.sp, fontWeight = FontWeight.SemiBold,
            color = AzulOscuro, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center
        )
        Column(
            Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(top = 16.dp, bottom = 8.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            OutlinedTextField(
                value = propietario, onValueChange = { propietario = it },
                label = { Text("Nombre del propietario") },
                leadingIcon = { Icon(Icons.Default.Person, null) },
                singleLine = true, shape = forma, colors = colores,
                isError = intento && propietario.isBlank(),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = telefono,
                onValueChange = { telefono = it.filter { c -> c.isDigit() || c == '-' }.take(12) },
                label = { Text("Número de teléfono") },
                leadingIcon = { Icon(Icons.Default.Phone, null) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                singleLine = true, shape = forma, colors = colores,
                isError = intento && telefono.isBlank(),
                modifier = Modifier.fillMaxWidth()
            )
            CampoSeleccion(fecha, "Fecha de la cita", Icons.Default.CalendarMonth, intento && fecha.isBlank()) {
                mostrarFecha = true
            }
            CampoSeleccion(hora, "Hora de la cita", Icons.Default.Schedule, intento && hora.isBlank()) {
                mostrarHora = true
            }
            OutlinedTextField(
                value = raza, onValueChange = { raza = it },
                label = { Text("Raza de la mascota") },
                leadingIcon = { Icon(Icons.Default.Pets, null) },
                singleLine = true, shape = forma, colors = colores,
                isError = intento && raza.isBlank(),
                modifier = Modifier.fillMaxWidth()
            )
            ExposedDropdownMenuBox(expanded = expandido, onExpandedChange = { expandido = it }) {
                OutlinedTextField(
                    value = tamano, onValueChange = {}, readOnly = true,
                    label = { Text("Tamaño de la mascota") },
                    leadingIcon = { Icon(Icons.Default.Straighten, null) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandido) },
                    shape = forma, colors = colores,
                    isError = intento && tamano.isBlank(),
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(expanded = expandido, onDismissRequest = { expandido = false }) {
                    listOf("Pequeño", "Mediano", "Grande").forEach { opcion ->
                        DropdownMenuItem(
                            text = { Text(opcion) },
                            onClick = { tamano = opcion; expandido = false }
                        )
                    }
                }
            }
            val hayVacios = listOf(propietario, telefono, fecha, hora, raza, tamano).any { it.isBlank() }
            if (intento && hayVacios) {
                Text("Complete todos los campos para guardar la cita.", color = MaterialTheme.colorScheme.error, fontSize = 14.sp)
            }
        }
        Row(Modifier.padding(top = 8.dp), horizontalArrangement = Arrangement.spacedBy(14.dp)) {
            BotonAccion("Cancelar", GrisPatitas, onCancelar, Modifier.weight(1f))
            BotonAccion("Guardar", AzulPatitas, onClick = {
                intento = true
                val completo = listOf(propietario, telefono, fecha, hora, raza, tamano).none { it.isBlank() }
                if (completo) onGuardar(Cita(propietario.trim(), telefono, fecha, hora, raza.trim(), tamano))
            }, modifier = Modifier.weight(1f))
        }
    }

    if (mostrarFecha) {
        val estado = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { mostrarFecha = false },
            confirmButton = {
                TextButton(onClick = {
                    estado.selectedDateMillis?.let { fecha = formatearFecha(it) }
                    mostrarFecha = false
                }) { Text("Aceptar") }
            },
            dismissButton = { TextButton(onClick = { mostrarFecha = false }) { Text("Cancelar") } }
        ) { DatePicker(state = estado) }
    }

    if (mostrarHora) {
        val estado = rememberTimePickerState(initialHour = 10, initialMinute = 0, is24Hour = false)
        AlertDialog(
            onDismissRequest = { mostrarHora = false },
            confirmButton = {
                TextButton(onClick = {
                    hora = formatearHora(estado.hour, estado.minute)
                    mostrarHora = false
                }) { Text("Aceptar") }
            },
            dismissButton = { TextButton(onClick = { mostrarHora = false }) { Text("Cancelar") } },
            text = {
                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    TimePicker(state = estado)
                }
            }
        )
    }
}

@Composable
private fun coloresCampo() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = AzulPatitas,
    focusedLabelColor = AzulPatitas,
    cursorColor = AzulPatitas,
    focusedLeadingIconColor = AzulPatitas,
    unfocusedLeadingIconColor = GrisPatitas
)

/** Campo de solo lectura que abre un diálogo (fecha / hora) al tocarlo. */
@Composable
private fun CampoSeleccion(valor: String, etiqueta: String, icono: ImageVector, error: Boolean, onClick: () -> Unit) {
    Box {
        OutlinedTextField(
            value = valor, onValueChange = {}, readOnly = true,
            label = { Text(etiqueta) },
            leadingIcon = { Icon(icono, null) },
            shape = RoundedCornerShape(8.dp), colors = coloresCampo(), isError = error,
            modifier = Modifier.fillMaxWidth()
        )
        Box(
            Modifier
                .matchParentSize()
                .clickable(onClick = onClick)
        )
    }
}

private fun formatearFecha(millis: Long): String =
    SimpleDateFormat("dd/MM/yyyy", Locale("es", "DO")).apply {
        timeZone = TimeZone.getTimeZone("UTC") // el DatePicker entrega la fecha en UTC
    }.format(Date(millis))

private fun formatearHora(hora24: Int, minutos: Int): String {
    val h12 = if (hora24 % 12 == 0) 12 else hora24 % 12
    val sufijo = if (hora24 < 12) "AM" else "PM"
    return "%02d:%02d %s".format(h12, minutos, sufijo)
}

/* ───────────────────────── 3. RESUMEN ───────────────────────── */

@Composable
fun PantallaResumen(cita: Cita, onVolver: () -> Unit) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        Encabezado()
        Spacer(Modifier.height(12.dp))
        Text(
            "Resumen de la cita", fontSize = 24.sp, fontWeight = FontWeight.SemiBold,
            color = AzulOscuro, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(16.dp))
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = TarjetaGris),
            elevation = CardDefaults.cardElevation(6.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                FilaDato(Icons.Default.Person, "Propietario", cita.propietario)
                FilaDato(Icons.Default.Phone, "Teléfono", cita.telefono)
                FilaDato(Icons.Default.CalendarMonth, "Fecha", cita.fecha)
                FilaDato(Icons.Default.Schedule, "Hora", cita.hora)
                FilaDato(Icons.Default.Pets, "Raza", cita.raza)
                FilaDato(Icons.Default.Straighten, "Tamaño", cita.tamano)
            }
        }
        Spacer(Modifier.weight(1f))
        Text(
            "¡Su cita ha sido registrada exitosamente!", fontSize = 20.sp, color = AzulOscuro,
            textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.weight(1f))
        BotonAccion("Volver al inicio", AzulPatitas, onVolver, Modifier.fillMaxWidth())
    }
}

@Composable
private fun FilaDato(icono: ImageVector, etiqueta: String, valor: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icono, null, tint = GrisPatitas)
        Spacer(Modifier.width(12.dp))
        Text("$etiqueta: $valor", fontSize = 17.sp, color = AzulOscuro)
    }
}

/* ───────────────────────── 4. NUESTROS SERVICIOS ───────────────────────── */

private data class Servicio(val titulo: String, val descripcion: String, val icono: ImageVector)

private val servicios = listOf(
    Servicio("Consulta veterinaria", "Evaluación general y seguimiento de la salud de su mascota.", Icons.Default.MedicalServices),
    Servicio("Vacunación", "Aplicación y control del calendario de vacunas.", Icons.Default.Vaccines),
    Servicio("Desparasitación", "Prevención y tratamiento de parásitos internos y externos.", Icons.Default.Medication),
    Servicio("Baño y peluquería", "Higiene, baño y cuidado estético de su mascota.", Icons.Default.Bathtub),
    Servicio("Emergencias 24 h", "Atención inmediata a cualquier hora del día.", Icons.Default.Emergency),
    Servicio("Cirugía", "Procedimientos quirúrgicos con seguimiento postoperatorio.", Icons.Default.ContentCut),
    Servicio("Control de mascotas", "Chequeos periódicos, peso y registro de su historial.", Icons.Default.MonitorHeart)
)

@Composable
fun PantallaServicios(onVolver: () -> Unit) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        Encabezado()
        Spacer(Modifier.height(12.dp))
        Text(
            "Nuestros servicios", fontSize = 24.sp, fontWeight = FontWeight.SemiBold,
            color = AzulOscuro, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center
        )
        LazyColumn(
            Modifier
                .weight(1f)
                .padding(vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(servicios) { s ->
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = TarjetaGris),
                    elevation = CardDefaults.cardElevation(4.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(s.icono, null, tint = VerdePatitas, modifier = Modifier.size(32.dp))
                        Spacer(Modifier.width(14.dp))
                        Column {
                            Text(s.titulo, fontSize = 17.sp, fontWeight = FontWeight.SemiBold, color = AzulOscuro)
                            Text(s.descripcion, fontSize = 13.sp, color = Color(0xFF5B6470))
                        }
                    }
                }
            }
        }
        BotonAccion("Volver al inicio", AzulPatitas, onVolver, Modifier.fillMaxWidth())
    }
}