package com.example.ui

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CompassCalibration
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.*
import java.util.Locale
import kotlin.math.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompassScreen(viewModel: ZadViewModel, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val lang by viewModel.selectedLanguage.collectAsStateWithLifecycle()

    val isCustomLocation by viewModel.isCustomLocation.collectAsStateWithLifecycle()
    val customLatitude by viewModel.customLatitude.collectAsStateWithLifecycle()
    val customLongitude by viewModel.customLongitude.collectAsStateWithLifecycle()

    val latToUse = if (isCustomLocation) customLatitude else viewModel.currentCity.latitude
    val lngToUse = if (isCustomLocation) customLongitude else viewModel.currentCity.longitude

    // Target Qibla degree for current selected coordinates
    val qiblaAngle = remember(latToUse, lngToUse) {
        viewModel.getQiblaAngle()
    }

    // Mecca Distance via Haversine
    val distanceToMecca = remember(latToUse, lngToUse) {
        val r = 6371.0 // Earth's radius in kilometers
        val dLat = Math.toRadians(21.4225 - latToUse)
        val dLng = Math.toRadians(39.8262 - lngToUse)
        val a = sin(dLat / 2).pow(2) + cos(Math.toRadians(latToUse)) * cos(Math.toRadians(21.4225)) * sin(dLng / 2).pow(2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        r * c
    }

    // Active device sensor listening
    var sensorHeading by remember { mutableStateOf(0f) }
    val sensorManager = remember { context.getSystemService(Context.SENSOR_SERVICE) as? SensorManager }

    DisposableEffect(sensorManager) {
        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                if (event != null && event.values.isNotEmpty()) {
                    sensorHeading = event.values[0]
                }
            }
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        val compassSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_ORIENTATION)
        if (compassSensor != null) {
            sensorManager.registerListener(listener, compassSensor, SensorManager.SENSOR_DELAY_UI)
        }

        onDispose {
            sensorManager?.unregisterListener(listener)
        }
    }

    // Manual slider indicator to test/rotate if device sensor is absent
    var manualDialAngle by remember { mutableStateOf(0f) }

    // Resultant direction to Mecca
    val finalAzimuthDegrees = remember(sensorHeading, manualDialAngle) {
        if (sensorHeading != 0f) sensorHeading else manualDialAngle
    }

    // Aligned status
    val angleDifference = abs((finalAzimuthDegrees - qiblaAngle + 360) % 360)
    val isAligned = angleDifference < 5.0 || (360 - angleDifference) < 5.0

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = Localization.get("qibla", lang),
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Location context card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
        ) {
            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.LocationOn, contentDescription = "Loc", tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = if (isCustomLocation) "إحداثياتك المخصصة" else viewModel.currentCity.nameAr,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "زاوية القبلة: ${String.format(Locale.US, "%.2f", qiblaAngle)}° درجات",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Aligned alignment message container
        AnimatedVisibility(
            visible = isAligned,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Card(
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "✓ تم توجيه البوصلة باتجاه الكعبة المشرفة بدقة!",
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                    color = Color.White,
                    modifier = Modifier.padding(12.dp).fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        }

        // Beautiful Graphical Compass Dial
        val dialBackgroundModifier = if (isAligned) {
            Modifier.background(
                Brush.radialGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                        Color.Transparent
                    )
                )
            )
        } else {
            Modifier
        }

        Box(
            modifier = Modifier
                .size(260.dp)
                .clip(CircleShape)
                .then(dialBackgroundModifier)
                .border(2.dp, if (isAligned) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.outlineVariant, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            // Draw compass on canvas
            Canvas(modifier = Modifier.fillMaxSize()) {
                val center = size.width / 2
                val radius = size.width * 0.82f / 2

                // 1. Draw outer circle
                val compassHeadingPaint = Stroke(width = 4f)
                drawCircle(
                    color = Color.Gray.copy(alpha = 0.5f),
                    radius = radius,
                    style = compassHeadingPaint
                )

                val rotationAngle = -finalAzimuthDegrees.toFloat()

                rotate(degrees = rotationAngle, pivot = androidx.compose.ui.geometry.Offset(center, center)) {
                    // Draw Cardinal Markers beautifully as clean vector triangles pointing N, S, E, W
                    // North arrow (Red)
                    val northPath = Path().apply {
                        moveTo(center, center - radius + 5f)
                        lineTo(center - 12f, center - radius + 30f)
                        lineTo(center + 12f, center - radius + 30f)
                        close()
                    }
                    drawPath(northPath, Color.Red)

                    // South arrow (Gray)
                    val southPath = Path().apply {
                        moveTo(center, center + radius - 5f)
                        lineTo(center - 10f, center + radius - 25f)
                        lineTo(center + 10f, center + radius - 25f)
                        close()
                    }
                    drawPath(southPath, Color.Gray)

                    // East dot
                    drawCircle(Color.Gray, radius = 6f, center = androidx.compose.ui.geometry.Offset(center + radius - 15f, center))

                    // West dot
                    drawCircle(Color.Gray, radius = 6f, center = androidx.compose.ui.geometry.Offset(center - radius + 15f, center))

                    // Draw the Kaaba / Qibla marker angle in gold
                    val qiblaRad = Math.toRadians(qiblaAngle)
                    val qiblaX = center + (radius - 50f) * sin(qiblaRad).toFloat()
                    val qiblaY = center - (radius - 50f) * cos(qiblaRad).toFloat()

                    // Draw golden star/anchor marker representing Mecca Qibla
                    drawCircle(
                        color = Color(0xFFCF9D15),
                        radius = 16f,
                        center = androidx.compose.ui.geometry.Offset(qiblaX, qiblaY)
                    )

                    // Draw beautiful line to Qibla
                    drawLine(
                        color = Color(0xFFCF9D15),
                        start = androidx.compose.ui.geometry.Offset(center, center),
                        end = androidx.compose.ui.geometry.Offset(qiblaX, qiblaY),
                        strokeWidth = 6f
                    )
                }

                // 2. Center magnetic compass pointer pointed straight up (N reference pointer)
                val arrowPath = Path().apply {
                    moveTo(center, center - 70f)
                    lineTo(center - 15f, center)
                    lineTo(center + 15f, center)
                    close()
                }
                drawPath(
                    path = arrowPath,
                    color = Color(0xFF0A4D34)
                )

                drawCircle(
                    color = Color(0xFFCF9D15),
                    radius = 8f,
                    center = androidx.compose.ui.geometry.Offset(center, center)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Distance text
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Info, contentDescription = "Distance", tint = MaterialTheme.colorScheme.secondary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = Localization.get("mecca_dist", lang),
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "${String.format(Locale.US, "%,.1f", distanceToMecca)} كم (km)",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Calibration simulator for emulator testing
        if (sensorHeading == 0f) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
            ) {
                Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CompassCalibration, contentDescription = "Calibrate", tint = Color.Gray)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "محاكاة دوران الهاتف (Emulator Dial Rotation)",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Slider(
                        value = manualDialAngle,
                        onValueChange = { manualDialAngle = it },
                        valueRange = 0f..359f,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = "محاكاة زاوية توجيه الهاتف: ${manualDialAngle.toInt()}°",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(80.dp))
    }
}
