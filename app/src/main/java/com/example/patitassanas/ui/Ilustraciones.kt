package com.example.patitassanas.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.unit.Dp
import com.example.patitassanas.ui.theme.AzulPatitas
import com.example.patitassanas.ui.theme.VerdePatitas

/** Logo: corazón mitad azul / mitad verde con una huella blanca. Todo dibujado con código. */
@Composable
fun LogoPatitas(tamano: Dp, modifier: Modifier = Modifier) {
    Canvas(modifier.size(tamano)) {
        val s = size.minDimension / 100f
        val corazon = Path().apply {
            moveTo(50f * s, 92f * s)
            cubicTo(8f * s, 62f * s, -2f * s, 36f * s, 14f * s, 18f * s)
            cubicTo(28f * s, 3f * s, 46f * s, 8f * s, 50f * s, 26f * s)
            cubicTo(54f * s, 8f * s, 72f * s, 3f * s, 86f * s, 18f * s)
            cubicTo(102f * s, 36f * s, 92f * s, 62f * s, 50f * s, 92f * s)
            close()
        }
        clipPath(corazon) {
            drawRect(AzulPatitas, Offset.Zero, Size(50f * s, 100f * s))
            drawRect(VerdePatitas, Offset(50f * s, 0f), Size(50f * s, 100f * s))
        }
        val b = Color.White
        drawOval(b, Offset(35f * s, 50f * s), Size(30f * s, 22f * s))   // almohadilla
        drawOval(b, Offset(26f * s, 38f * s), Size(10f * s, 13f * s))   // dedos
        drawOval(b, Offset(38f * s, 28f * s), Size(10f * s, 14f * s))
        drawOval(b, Offset(52f * s, 28f * s), Size(10f * s, 14f * s))
        drawOval(b, Offset(64f * s, 38f * s), Size(10f * s, 13f * s))
    }
}

private fun DrawScope.ojoFeliz(cx: Float, cy: Float, ancho: Float, s: Float, color: Color) {
    drawArc(
        color = color, startAngle = 180f, sweepAngle = 180f, useCenter = false,
        topLeft = Offset((cx - ancho / 2) * s, (cy - ancho / 3) * s),
        size = Size(ancho * s, ancho * 0.66f * s),
        style = Stroke(width = 3f * s, cap = StrokeCap.Round)
    )
}

private fun DrawScope.sonrisa(cx: Float, cy: Float, s: Float, color: Color) {
    val estilo = Stroke(width = 2.5f * s, cap = StrokeCap.Round)
    drawArc(color, 0f, 180f, false, Offset((cx - 10f) * s, cy * s), Size(10f * s, 9f * s), style = estilo)
    drawArc(color, 0f, 180f, false, Offset(cx * s, cy * s), Size(10f * s, 9f * s), style = estilo)
}

/** Cara de perro feliz (tipo caricatura). */
@Composable
fun CaraPerro(modifier: Modifier = Modifier) {
    Canvas(modifier) {
        val s = size.minDimension / 100f
        val pelaje = Color(0xFFF2C894)
        val oscuro = Color(0xFFD9A066)
        val claro = Color(0xFFFFF3E0)
        val tinta = Color(0xFF3A2A1E)
        drawOval(oscuro, Offset(4f * s, 16f * s), Size(28f * s, 52f * s))   // orejas caídas
        drawOval(oscuro, Offset(68f * s, 16f * s), Size(28f * s, 52f * s))
        drawCircle(pelaje, 36f * s, Offset(50f * s, 50f * s))                // cabeza
        drawCircle(oscuro, 11f * s, Offset(68f * s, 33f * s))                // mancha
        drawOval(claro, Offset(31f * s, 50f * s), Size(38f * s, 30f * s))    // hocico
        ojoFeliz(38f, 42f, 12f, s, tinta)
        ojoFeliz(62f, 42f, 12f, s, tinta)
        drawRoundRect(Color(0xFFF08A9B), Offset(45f * s, 66f * s), Size(10f * s, 12f * s), CornerRadius(5f * s)) // lengua
        drawOval(tinta, Offset(43f * s, 52f * s), Size(14f * s, 9f * s))     // nariz
        sonrisa(50f, 60f, s, tinta)
        drawRoundRect(AzulPatitas, Offset(26f * s, 84f * s), Size(48f * s, 9f * s), CornerRadius(4f * s)) // collar
    }
}

/** Cara de gato feliz (tipo caricatura). */
@Composable
fun CaraGato(modifier: Modifier = Modifier) {
    Canvas(modifier) {
        val s = size.minDimension / 100f
        val gris = Color(0xFFB4BCC6)
        val rosa = Color(0xFFF4A9B8)
        val tinta = Color(0xFF5B6470)
        val orejaIzq = Path().apply { moveTo(16f * s, 46f * s); lineTo(22f * s, 8f * s); lineTo(48f * s, 28f * s); close() }
        val orejaDer = Path().apply { moveTo(84f * s, 46f * s); lineTo(78f * s, 8f * s); lineTo(52f * s, 28f * s); close() }
        val internaIzq = Path().apply { moveTo(24f * s, 38f * s); lineTo(26f * s, 18f * s); lineTo(40f * s, 28f * s); close() }
        val internaDer = Path().apply { moveTo(76f * s, 38f * s); lineTo(74f * s, 18f * s); lineTo(60f * s, 28f * s); close() }
        drawPath(orejaIzq, gris); drawPath(orejaDer, gris)
        drawPath(internaIzq, rosa); drawPath(internaDer, rosa)
        drawCircle(gris, 36f * s, Offset(50f * s, 58f * s))
        ojoFeliz(37f, 52f, 12f, s, tinta)
        ojoFeliz(63f, 52f, 12f, s, tinta)
        val nariz = Path().apply { moveTo(46f * s, 60f * s); lineTo(54f * s, 60f * s); lineTo(50f * s, 65f * s); close() }
        drawPath(nariz, rosa)
        sonrisa(50f, 65f, s, tinta)
        val bigote = 1.5f * s
        drawLine(tinta, Offset(30f * s, 64f * s), Offset(6f * s, 59f * s), bigote)
        drawLine(tinta, Offset(30f * s, 68f * s), Offset(6f * s, 70f * s), bigote)
        drawLine(tinta, Offset(70f * s, 64f * s), Offset(94f * s, 59f * s), bigote)
        drawLine(tinta, Offset(70f * s, 68f * s), Offset(94f * s, 70f * s), bigote)
        drawRoundRect(AzulPatitas, Offset(28f * s, 88f * s), Size(44f * s, 8f * s), CornerRadius(4f * s))
    }
}