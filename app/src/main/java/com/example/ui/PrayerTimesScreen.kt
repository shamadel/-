package com.example.ui

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrayerTimesScreen(viewModel: ZadViewModel, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val lang by viewModel.selectedLanguage.collectAsStateWithLifecycle()
    val isAr = lang == "ar"

    val isCustomLocation by viewModel.isCustomLocation.collectAsStateWithLifecycle()
    val customLatitude by viewModel.customLatitude.collectAsStateWithLifecycle()
    val customLongitude by viewModel.customLongitude.collectAsStateWithLifecycle()
    val selectedCityIndex by viewModel.selectedCityIndex.collectAsStateWithLifecycle()
    
    val calcMethod by viewModel.calcMethod.collectAsStateWithLifecycle()
    val juristicMethod by viewModel.juristicMethod.collectAsStateWithLifecycle()
    val selectedAzanVoice by viewModel.selectedAzanVoice.collectAsStateWithLifecycle()
    val azanEnabled by viewModel.azanEnabled.collectAsStateWithLifecycle()

    var showCityDialog by remember { mutableStateOf(false) }
    var showCustomCoordDialog by remember { mutableStateOf(false) }
    var showSettingDialog by remember { mutableStateOf(false) }

    val prayerTimes = remember(isCustomLocation, customLatitude, customLongitude, selectedCityIndex, calcMethod, juristicMethod) {
        viewModel.getPrayerTimesForToday()
    }

    val currentCity = viewModel.currentCity
    val cityLabel = if (isCustomLocation) {
        "${Localization.get("custom_coordinate", lang)} (${String.format(Locale.US, "%.3f", customLatitude)}, ${String.format(Locale.US, "%.3f", customLongitude)})"
    } else {
        if (isAr) currentCity.nameAr else currentCity.nameEn
    }

    // Determine current/next prayer if possible
    val nowStr = remember {
        SimpleDateFormat("HH:mm", Locale.US).format(Date())
    }

    val prayersList = listOf(
        Triple(Localization.get("fajr", lang), prayerTimes.fajr, Icons.Default.WbTwilight),
        Triple(Localization.get("sunrise", lang), prayerTimes.sunrise, Icons.Default.WbSunny),
        Triple(Localization.get("dhuhr", lang), prayerTimes.dhuhr, Icons.Default.LightMode),
        Triple(Localization.get("asr", lang), prayerTimes.asr, Icons.Default.FilterDrama),
        Triple(Localization.get("maghrib", lang), prayerTimes.maghrib, Icons.Default.Nightlight),
        Triple(Localization.get("isha", lang), prayerTimes.isha, Icons.Default.Bedtime)
    )

    // Highlight the active prayer by simple time estimation
    val highlightedIndex = remember(prayerTimes, nowStr) {
        val currentStr = nowStr.replace(":", "").toIntOrNull() ?: 1200
        val fajrVal = prayerTimes.fajr.replace(":", "").toIntOrNull() ?: 500
        val sunriseVal = prayerTimes.sunrise.replace(":", "").toIntOrNull() ?: 600
        val dhuhrVal = prayerTimes.dhuhr.replace(":", "").toIntOrNull() ?: 1200
        val asrVal = prayerTimes.asr.replace(":", "").toIntOrNull() ?: 1530
        val maghribVal = prayerTimes.maghrib.replace(":", "").toIntOrNull() ?: 1830
        val ishaVal = prayerTimes.isha.replace(":", "").toIntOrNull() ?: 2000

        when {
            currentStr in fajrVal until sunriseVal -> 0
            currentStr in sunriseVal until dhuhrVal -> 1
            currentStr in dhuhrVal until asrVal -> 2
            currentStr in asrVal until maghribVal -> 3
            currentStr in maghribVal until ishaVal -> 4
            else -> 5
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Main header card
        Card(
            modifier = Modifier.fillMaxWidth().wrapContentHeight(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.85f)
                            )
                        )
                    )
                    .padding(24.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "City",
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = cityLabel,
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    val formattedDate = remember {
                        val df = SimpleDateFormat("EEEE, d MMMM yyyy", if (isAr) Locale("ar") else Locale.ENGLISH)
                        df.format(Date())
                    }
                    Text(
                        text = formattedDate,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.82f)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = { showCityDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(Localization.get("selected_city", lang), color = Color.Black, fontWeight = FontWeight.Bold)
                        }
                        Button(
                            onClick = { showCustomCoordDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(Localization.get("custom_coordinate", lang), color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Prayer Cards
        prayersList.forEachIndexed { index, prayer ->
            val isActive = index == highlightedIndex
            val cardColor = if (isActive) {
                MaterialTheme.colorScheme.secondaryContainer
            } else {
                MaterialTheme.colorScheme.surface
            }
            val borderModifier = if (isActive) {
                Modifier.fillMaxWidth().padding(vertical = 5.dp)
                    .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.15f), RoundedCornerShape(16.dp))
            } else {
                Modifier.fillMaxWidth().padding(vertical = 5.dp)
            }

            Card(
                modifier = borderModifier,
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = cardColor)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = prayer.third,
                                contentDescription = prayer.first,
                                tint = if (isActive) Color.White else MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = prayer.first,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = prayer.second,
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
                            color = if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Muezzin Custom Voice simulation card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = Localization.get("azan_alert", lang),
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "${Localization.get("voice", lang)}: $selectedAzanVoice",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Switch(
                        checked = azanEnabled,
                        onCheckedChange = { viewModel.setAzanEnabled(it) }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        // Play Simulated Azan Sound
                        val textSim = "${Localization.get("azan_notif_played", lang)} $selectedAzanVoice"
                        Toast.makeText(context, textSim, Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = "Test")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "تشغيل تجريبي للأذان (Simulation)")
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Configure button
                OutlinedButton(
                    onClick = { showSettingDialog = true },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Tune, contentDescription = "Config")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = Localization.get("settings", lang))
                }
            }
        }

        Spacer(modifier = Modifier.height(50.dp))
    }

    // 1. City Dropdown dialog
    if (showCityDialog) {
        Dialog(onDismissRequest = { showCityDialog = false }) {
            Card(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp).verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = Localization.get("selected_city", lang),
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(bottom = 16.dp),
                        color = MaterialTheme.colorScheme.primary
                    )

                    City.PREDEFINED_CITIES.forEachIndexed { i, city ->
                        val cityName = if (isAr) city.nameAr else city.nameEn
                        TextButton(
                            onClick = {
                                viewModel.setCity(i)
                                showCityDialog = false
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = cityName,
                                style = MaterialTheme.typography.bodyLarge,
                                color = if (selectedCityIndex == i && !isCustomLocation) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                            )
                        }
                        HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
                    }
                }
            }
        }
    }

    // 2. Custom Coordinates input dialog
    if (showCustomCoordDialog) {
        var inputLat by remember { mutableStateOf(customLatitude.toString()) }
        var inputLng by remember { mutableStateOf(customLongitude.toString()) }

        Dialog(onDismissRequest = { showCustomCoordDialog = false }) {
            Card(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = Localization.get("custom_coordinate", lang),
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(bottom = 16.dp),
                        color = MaterialTheme.colorScheme.primary
                    )

                    OutlinedTextField(
                        value = inputLat,
                        onValueChange = { inputLat = it },
                        label = { Text(Localization.get("lat", lang)) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = inputLng,
                        onValueChange = { inputLng = it },
                        label = { Text(Localization.get("lng", lang)) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            val parsedLat = inputLat.toDoubleOrNull() ?: 21.4225
                            val parsedLng = inputLng.toDoubleOrNull() ?: 39.8262
                            viewModel.setCustomLocation(parsedLat, parsedLng)
                            showCustomCoordDialog = false
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(Localization.get("save", lang))
                    }
                }
            }
        }
    }

    // 3. Settings Config calculation authority / voice selection
    if (showSettingDialog) {
        Dialog(onDismissRequest = { showSettingDialog = false }) {
            Card(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp).verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = Localization.get("settings", lang),
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center
                    )

                    // Muezzin Voices
                    Text(
                        text = Localization.get("voice", lang),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    val voices = listOf("عبد الباسط عبد الصمد", "مشاري العفاسي", "علي ملا (الحرم المكي)", "نغمة هادئة Beep")
                    voices.forEach { voice ->
                        Row(
                            modifier = Modifier.fillMaxWidth().clickable { viewModel.setAzanVoice(voice) }.padding(vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(selected = selectedAzanVoice == voice, onClick = { viewModel.setAzanVoice(voice) })
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(voice)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Calculation Method
                    Text(
                        text = Localization.get("calc_auth", lang),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    CalculationMethod.values().forEach { method ->
                        val nameStr = if (isAr) method.displayNameAr else method.displayNameEn
                        Row(
                            modifier = Modifier.fillMaxWidth().clickable { viewModel.setCalculationMethod(method) }.padding(vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(selected = calcMethod == method, onClick = { viewModel.setCalculationMethod(method) })
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(nameStr, fontSize = 13.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Juristic Method (Asr calculation shadow offset)
                    Text(
                        text = Localization.get("juristic", lang),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    JuristicMethod.values().forEach { juristic ->
                        val nameStr = if (isAr) juristic.displayNameAr else juristic.displayNameEn
                        Row(
                            modifier = Modifier.fillMaxWidth().clickable { viewModel.setJuristicMethod(juristic) }.padding(vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(selected = juristicMethod == juristic, onClick = { viewModel.setJuristicMethod(juristic) })
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(nameStr, fontSize = 13.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { showSettingDialog = false },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(Localization.get("save", lang))
                    }
                }
            }
        }
    }
}
