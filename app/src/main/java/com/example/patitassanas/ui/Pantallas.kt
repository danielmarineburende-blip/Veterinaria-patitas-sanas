@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.patitassanas.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Bathtub
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.MonitorHeart
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Vaccines
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.patitassanas.Cita
import com.example.patitassanas.CitaDatabase
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

private val NaranjaPatitas = Color(0xFFF59E0B)
private val RojoPatitas = Color(0xFFD64545)
private val TextoSecundario = Color(0xFF5B6470)
private val AzulSuave = Color(0xFFD6EBF8)

/* ───────────────────────── 1. INICIO ───────────────────────── */

@Composable
fun PantallaInicio(onAgendar: () -> Unit, onServicios: () -> Unit, onSalir: () -> Unit) {
    Box(
        Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(FondoClaro, Color(0xFFD6EBF8))))
    ) {
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
            Spacer(Modifier.height(150.dp))
        }
    }
}

/* ───────────────────────── 2. FORMULARIO DE CITAS ───────────────────────── */

@Composable
fun PantallaCitas(idEditar: Int, onConsultar: () -> Unit, onVolver: () -> Unit) {
    val context = LocalContext.current
    val db = remember { CitaDatabase(context) }
    // Si venimos desde la lista pulsando "editar", se cargan los datos de esa cita
    val inicial = remember { if (idEditar != 0) db.consultarPorId(idEditar) else null }

    var idSeleccionado by rememberSaveable { mutableIntStateOf(inicial?.id ?: 0) } // 0 = cita nueva
    var propietario by rememberSaveable { mutableStateOf(inicial?.nombre ?: "") }
    var telefono by rememberSaveable { mutableStateOf(inicial?.telefono ?: "") }
    var fecha by rememberSaveable { mutableStateOf(inicial?.fecha ?: "") }
    var hora by rememberSaveable { mutableStateOf(inicial?.hora ?: "") }
    var raza by rememberSaveable { mutableStateOf(inicial?.raza ?: "") }
    var tamano by rememberSaveable { mutableStateOf(inicial?.tamano ?: "") }
    var intento by remember { mutableStateOf(false) }
    var mostrarFecha by remember { mutableStateOf(false) }
    var mostrarHora by remember { mutableStateOf(false) }
    var confirmarEliminar by remember { mutableStateOf(false) }

    val colores = coloresCampo()
    val forma = RoundedCornerShape(10.dp)

    fun aviso(m: String) = Toast.makeText(context, m, Toast.LENGTH_SHORT).show()
    fun limpiar() {
        idSeleccionado = 0
        propietario = ""; telefono = ""; fecha = ""; hora = ""; raza = ""; tamano = ""
        intento = false
    }
    fun hayVacios() = listOf(propietario, telefono, fecha, hora, raza, tamano).any { it.isBlank() }
    fun formulario() = Cita(idSeleccionado, propietario.trim(), telefono, fecha, hora, raza.trim(), tamano)

    Column(
        Modifier
            .fillMaxSize()
            .imePadding()
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        Encabezado()
        Spacer(Modifier.height(8.dp))
        Text(
            "Agendar cita", fontSize = 22.sp, fontWeight = FontWeight.SemiBold,
            color = AzulOscuro, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center
        )

        Column(
            Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(top = 10.dp, bottom = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(
                    if (idSeleccionado != 0) "Editando cita #$idSeleccionado" else "Nueva cita",
                    fontWeight = FontWeight.SemiBold, fontSize = 15.sp,
                    color = if (idSeleccionado != 0) NaranjaPatitas else TextoSecundario,
                    modifier = Modifier.weight(1f)
                )
                TextButton(onClick = { limpiar() }) {
                    Icon(Icons.Default.Clear, null, Modifier.size(18.dp), tint = GrisPatitas)
                    Spacer(Modifier.width(4.dp))
                    Text("Limpiar", color = GrisPatitas)
                }
            }

            Seccion("Datos del propietario") {
                OutlinedTextField(
                    value = propietario, onValueChange = { propietario = it },
                    label = { Text("Nombre completo") },
                    leadingIcon = { Icon(Icons.Default.Person, null) },
                    singleLine = true, shape = forma, colors = colores,
                    isError = intento && propietario.isBlank(),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = telefono,
                    onValueChange = { telefono = it.filter { c -> c.isDigit() || c == '-' }.take(12) },
                    label = { Text("Teléfono") },
                    leadingIcon = { Icon(Icons.Default.Phone, null) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    singleLine = true, shape = forma, colors = colores,
                    isError = intento && telefono.isBlank(),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Seccion("Fecha y hora") {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Box(Modifier.weight(1f)) {
                        CampoSeleccion(fecha, "Fecha", Icons.Default.CalendarMonth, intento && fecha.isBlank()) {
                            mostrarFecha = true
                        }
                    }
                    Box(Modifier.weight(1f)) {
                        CampoSeleccion(hora, "Hora", Icons.Default.Schedule, intento && hora.isBlank()) {
                            mostrarHora = true
                        }
                    }
                }
            }

            Seccion("Datos de la mascota") {
                OutlinedTextField(
                    value = raza, onValueChange = { raza = it },
                    label = { Text("Raza") },
                    leadingIcon = { Icon(Icons.Default.Pets, null) },
                    singleLine = true, shape = forma, colors = colores,
                    isError = intento && raza.isBlank(),
                    modifier = Modifier.fillMaxWidth()
                )
                Text("Tamaño", fontSize = 13.sp, color = TextoSecundario)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("Pequeño", "Mediano", "Grande").forEach { opcion ->
                        FilterChip(
                            selected = tamano == opcion,
                            onClick = { tamano = opcion },
                            label = {
                                Text(opcion, Modifier.fillMaxWidth(), textAlign = TextAlign.Center, fontSize = 14.sp)
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = AzulPatitas,
                                selectedLabelColor = Color.White
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp)
                        )
                    }
                }
                if (intento && tamano.isBlank()) {
                    Text("Seleccione el tamaño.", color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
                }
            }

            if (intento && hayVacios()) {
                Text("Complete todos los campos.", color = MaterialTheme.colorScheme.error, fontSize = 14.sp)
            }

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                BotonAccion("Guardar", AzulPatitas, onClick = {
                    intento = true
                    when {
                        idSeleccionado != 0 ->
                            aviso("Está editando una cita: use Actualizar, o Limpiar para crear una nueva")
                        hayVacios() -> Unit
                        else -> { db.insertar(formulario()); limpiar(); aviso("Cita guardada") }
                    }
                }, modifier = Modifier.weight(1f), icono = Icons.Default.Save)
                BotonAccion("Actualizar", NaranjaPatitas, onClick = {
                    intento = true
                    when {
                        idSeleccionado == 0 -> aviso("Abra una cita desde Consultar para actualizarla")
                        hayVacios() -> Unit
                        else -> { db.actualizar(formulario()); limpiar(); aviso("Cita actualizada") }
                    }
                }, modifier = Modifier.weight(1f), icono = Icons.Default.Edit)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                BotonAccion("Consultar", VerdePatitas, onConsultar, Modifier.weight(1f), Icons.Default.Search)
                BotonAccion("Eliminar", RojoPatitas, onClick = {
                    if (idSeleccionado == 0) aviso("Abra una cita desde Consultar para eliminarla")
                    else confirmarEliminar = true
                }, modifier = Modifier.weight(1f), icono = Icons.Default.Delete)
            }
        }

        BotonAccion("Volver al inicio", GrisPatitas, onVolver, Modifier.fillMaxWidth())
    }

    if (confirmarEliminar) {
        AlertDialog(
            onDismissRequest = { confirmarEliminar = false },
            title = { Text("Eliminar cita") },
            text = { Text("¿Desea eliminar la cita de $propietario? Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(onClick = {
                    db.eliminar(idSeleccionado)
                    limpiar()
                    confirmarEliminar = false
                    aviso("Cita eliminada")
                }) { Text("Eliminar", color = RojoPatitas) }
            },
            dismissButton = { TextButton(onClick = { confirmarEliminar = false }) { Text("Cancelar") } }
        )
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

/** Tarjeta blanca con título para agrupar campos del formulario. */
@Composable
private fun Seccion(titulo: String, contenido: @Composable ColumnScope.() -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(titulo, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = AzulPatitas)
            contenido()
        }
    }
}

@Composable
private fun coloresCampo() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = AzulPatitas,
    focusedLabelColor = AzulPatitas,
    cursorColor = AzulPatitas,
    focusedLeadingIconColor = AzulPatitas,
    unfocusedLeadingIconColor = GrisPatitas,
    unfocusedBorderColor = Color(0xFFC5CDD6)
)

/** Campo de solo lectura que abre un diálogo (fecha / hora) al tocarlo. */
@Composable
private fun CampoSeleccion(valor: String, etiqueta: String, icono: ImageVector, error: Boolean, onClick: () -> Unit) {
    Box {
        OutlinedTextField(
            value = valor, onValueChange = {}, readOnly = true,
            label = { Text(etiqueta) },
            leadingIcon = { Icon(icono, null) },
            shape = RoundedCornerShape(10.dp), colors = coloresCampo(), isError = error,
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
        timeZone = TimeZone.getTimeZone("UTC")
    }.format(Date(millis))

private fun formatearHora(hora24: Int, minutos: Int): String {
    val h12 = if (hora24 % 12 == 0) 12 else hora24 % 12
    val sufijo = if (hora24 < 12) "AM" else "PM"
    return "%02d:%02d %s".format(h12, minutos, sufijo)
}

/* ───────────────────────── 3. CONSULTAS (lista + buscador) ───────────────────────── */

@Composable
fun PantallaConsultas(onEditar: (Int) -> Unit, onVolver: () -> Unit) {
    val context = LocalContext.current
    val db = remember { CitaDatabase(context) }
    var citas by remember { mutableStateOf(db.consultarTodas()) }
    var busqueda by rememberSaveable { mutableStateOf("") }
    var citaAEliminar by remember { mutableStateOf<Cita?>(null) }

    val q = busqueda.trim()
    val qDigitos = q.filter { it.isDigit() }
    val filtradas = if (q.isEmpty()) citas else citas.filter { c ->
        c.nombre.contains(q, ignoreCase = true) ||
                c.telefono.contains(q) ||
                (qDigitos.isNotEmpty() && c.telefono.filter { it.isDigit() }.contains(qDigitos))
    }

    Column(
        Modifier
            .fillMaxSize()
            .imePadding()
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        Encabezado()
        Spacer(Modifier.height(8.dp))
        Text(
            "Citas registradas", fontSize = 22.sp, fontWeight = FontWeight.SemiBold,
            color = AzulOscuro, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(14.dp))

        OutlinedTextField(
            value = busqueda, onValueChange = { busqueda = it },
            placeholder = { Text("Buscar por nombre o teléfono") },
            leadingIcon = { Icon(Icons.Default.Search, null) },
            trailingIcon = {
                if (busqueda.isNotEmpty()) {
                    IconButton(onClick = { busqueda = "" }) { Icon(Icons.Default.Clear, "Borrar búsqueda") }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(28.dp),
            colors = coloresCampo(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        Text(
            if (q.isEmpty()) "${citas.size} cita(s) en total" else "${filtradas.size} resultado(s)",
            fontSize = 13.sp, color = TextoSecundario
        )

        if (filtradas.isEmpty()) {
            Column(
                Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(Icons.Default.Pets, null, Modifier.size(56.dp), tint = Color(0xFFB4BCC6))
                Spacer(Modifier.height(8.dp))
                Text(
                    if (citas.isEmpty()) "Aún no hay citas registradas" else "No se encontraron resultados",
                    color = TextoSecundario, fontSize = 16.sp
                )
            }
        } else {
            LazyColumn(
                Modifier
                    .weight(1f)
                    .padding(top = 8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filtradas, key = { it.id }) { c ->
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(3.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            Modifier.padding(start = 12.dp, top = 10.dp, bottom = 10.dp, end = 2.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                Modifier
                                    .size(46.dp)
                                    .clip(CircleShape)
                                    .background(AzulSuave),
                                contentAlignment = Alignment.Center
                            ) { Icon(Icons.Default.Pets, null, tint = AzulPatitas) }
                            Spacer(Modifier.width(12.dp))
                            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                                Text(c.nombre, fontSize = 17.sp, fontWeight = FontWeight.SemiBold, color = AzulOscuro)
                                DatoLinea(Icons.Default.Phone, c.telefono)
                                DatoLinea(Icons.Default.CalendarMonth, "${c.fecha} · ${c.hora}")
                                DatoLinea(Icons.Default.Pets, "${c.raza} · ${c.tamano}")
                            }
                            IconButton(onClick = { onEditar(c.id) }) {
                                Icon(Icons.Default.Edit, "Editar", tint = NaranjaPatitas)
                            }
                            IconButton(onClick = { citaAEliminar = c }) {
                                Icon(Icons.Default.Delete, "Eliminar", tint = RojoPatitas)
                            }
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(8.dp))
        BotonAccion("Volver al formulario", AzulPatitas, onVolver, Modifier.fillMaxWidth())
    }

    citaAEliminar?.let { c ->
        AlertDialog(
            onDismissRequest = { citaAEliminar = null },
            title = { Text("Eliminar cita") },
            text = { Text("¿Desea eliminar la cita de ${c.nombre}? Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(onClick = {
                    db.eliminar(c.id)
                    citas = db.consultarTodas()
                    citaAEliminar = null
                    Toast.makeText(context, "Cita eliminada", Toast.LENGTH_SHORT).show()
                }) { Text("Eliminar", color = RojoPatitas) }
            },
            dismissButton = { TextButton(onClick = { citaAEliminar = null }) { Text("Cancelar") } }
        )
    }
}

@Composable
private fun DatoLinea(icono: ImageVector, texto: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icono, null, Modifier.size(15.dp), tint = GrisPatitas)
        Spacer(Modifier.width(6.dp))
        Text(texto, fontSize = 14.sp, color = TextoSecundario)
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
                            Text(s.descripcion, fontSize = 13.sp, color = TextoSecundario)
                        }
                    }
                }
            }
        }
        BotonAccion("Volver al inicio", AzulPatitas, onVolver, Modifier.fillMaxWidth())
    }
}