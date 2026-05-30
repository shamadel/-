package com.example.ui

import android.content.Context
import android.os.Build
import android.os.Vibrator
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasbihScreen(viewModel: ZadViewModel, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val lang by viewModel.selectedLanguage.collectAsStateWithLifecycle()
    val isAr = lang == "ar"

    val counters by viewModel.allCounters.collectAsStateWithLifecycle()

    var selectedCounterId by remember { mutableStateOf<Int?>(null) }
    var showCustomCounterDialog by remember { mutableStateOf(false) }

    // Select first counter as default if none selected, or if the selected one has been deleted
    LaunchedEffect(counters) {
        if (counters.isNotEmpty() && (selectedCounterId == null || counters.none { it.id == selectedCounterId })) {
            selectedCounterId = counters[0].id
        }
    }

    val activeCounter = counters.find { it.id == selectedCounterId }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = Localization.get("tasbih_electronic", lang),
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Dropdown Selection or List of active tasbih lines
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                counters.forEach { countItem ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                if (selectedCounterId == countItem.id) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f) else Color.Transparent
                            )
                            .clickable { selectedCounterId = countItem.id }
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                countItem.phrase,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text(
                                "${Localization.get("target", lang)}: ${countItem.target}",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                countItem.count.toString(),
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            // Only allow deleting custom ones if desired
                            IconButton(onClick = { viewModel.deleteTasbih(countItem) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.LightGray)
                            }
                        }
                    }
                    HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Button to insert new tasbih phrase
                OutlinedButton(
                    onClick = { showCustomCounterDialog = true },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(Localization.get("new_tasbih", lang))
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        // Big major circular tapping counter button
        activeCounter?.let { counterObj ->
            // Click animation state
            var isPressed by remember { mutableStateOf(false) }
            val scale by animateFloatAsState(
                targetValue = if (isPressed) 0.9f else 1.0f,
                animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
                finishedListener = { isPressed = false }
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Phrase label
                Text(
                    text = counterObj.phrase,
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold),
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Box(
                    modifier = Modifier
                        .size(240.dp)
                        .scale(scale)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.secondary,
                                    MaterialTheme.colorScheme.primary
                                )
                            )
                        )
                        .clickable {
                            isPressed = true
                            // Action: trigger increment
                            viewModel.incrementTasbih(counterObj.id, counterObj.count, counterObj.target)
                            
                            // Trigger simple haptic vibration simulation via context
                            val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                            try {
                                vibrator?.vibrate(50) // Vibrates for 50 milliseconds
                            } catch (e: Exception) {
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    // Internal golden circle border
                    Box(
                        modifier = Modifier
                            .size(210.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = counterObj.count.toString(),
                                style = MaterialTheme.typography.displayLarge.copy(
                                    fontWeight = FontWeight.Black,
                                    fontSize = 64.sp
                                ),
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "/ ${counterObj.target}",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.Gray
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Reset button specifically for this phrase
                OutlinedButton(
                    onClick = { viewModel.resetTasbih(counterObj.id) },
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = "Reset")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(Localization.get("reset", lang))
                }
            }
        }

        Spacer(modifier = Modifier.height(100.dp))
    }

    // New Custom Counter dialog popup creator
    if (showCustomCounterDialog) {
        var termInput by remember { mutableStateOf("") }
        var targetInput by remember { mutableStateOf("33") }

        Dialog(onDismissRequest = { showCustomCounterDialog = false }) {
            Card(shape = RoundedCornerShape(24.dp)) {
                Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = Localization.get("new_tasbih", lang),
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    OutlinedTextField(
                        value = termInput,
                        onValueChange = { termInput = it },
                        placeholder = { Text(Localization.get("phrase_hint", lang)) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = targetInput,
                        onValueChange = { targetInput = it },
                        label = { Text("العدد المستهدف (Target count)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = {
                            val target = targetInput.toIntOrNull() ?: 33
                            if (termInput.trim().isNotEmpty()) {
                                viewModel.addCustomTasbih(termInput.trim(), target)
                                showCustomCounterDialog = false
                                Toast.makeText(context, "تمت الإضافة بنجاح!", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(Localization.get("add_phrase", lang))
                    }
                }
            }
        }
    }
}
