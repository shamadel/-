package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Exercise
import com.example.data.Player
import kotlinx.coroutines.delay
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestsScreen(
    viewModel: FootballViewModel,
    modifier: Modifier = Modifier
) {
    val exercises by viewModel.exercises.collectAsState()
    val players by viewModel.players.collectAsState()

    var showAddExerciseDialog by remember { mutableStateOf(false) }
    var selectedExercise by remember { mutableStateOf<Exercise?>(null) }
    var selectedPlayer by remember { mutableStateOf<Player?>(null) }

    // Custom exercise form state
    var exName by remember { mutableStateOf("") }
    var exCategory by remember { mutableStateOf("بدني") }
    var exDescription by remember { mutableStateOf("") }
    var exTargetStr by remember { mutableStateOf("") }
    val categories = listOf("بدني", "فني", "تكتيكي")
    var categoryExpanded by remember { mutableStateOf(false) }

    // Stopwatch states
    var isRunning by remember { mutableStateOf(false) }
    var timeMillis by remember { mutableStateOf(0L) }
    var notesInput by remember { mutableStateOf("") }

    // Auto-select first exercise when loaded
    if (selectedExercise == null && exercises.isNotEmpty()) {
        selectedExercise = exercises.first()
    }

    // Effect for active stopwatch ticker
    LaunchedEffect(isRunning) {
        if (isRunning) {
            val startTime = System.currentTimeMillis() - timeMillis
            while (isRunning) {
                timeMillis = System.currentTimeMillis() - startTime
                delay(10) // tick every 10ms for centisecond precision
            }
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("مؤقت التدريبات والاختبارات", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            
            // Section 1: Drills and Selections
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        "1. اختر التمرين واللاعب المقاس له",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    // Drill Selection Row
                    var showExMenu by remember { mutableStateOf(false) }
                    Box(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = selectedExercise?.name ?: "ادخل أو اختر تمريناً",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("نوع التمرين البدني/الفني") },
                            trailingIcon = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    IconButton(onClick = { showExMenu = !showExMenu }) {
                                        Icon(imageVector = Icons.Default.Sports, contentDescription = "عرض التمارين")
                                    }
                                    IconButton(onClick = { showAddExerciseDialog = true }) {
                                        Icon(imageVector = Icons.Default.AddCircle, contentDescription = "اضافة روتين", tint = MaterialTheme.colorScheme.primary)
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                        DropdownMenu(
                            expanded = showExMenu,
                            onDismissRequest = { showExMenu = false },
                            modifier = Modifier.fillMaxWidth(0.9f)
                        ) {
                            exercises.forEach { ex ->
                                DropdownMenuItem(
                                    text = { Text("${ex.name} (${ex.category})") },
                                    onClick = {
                                        selectedExercise = ex
                                        showExMenu = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Player selection Row
                    var showPlMenu by remember { mutableStateOf(false) }
                    Box(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = selectedPlayer?.name ?: "اختر اللاعب من فريقك",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("قائمة لاعبي الفريق") },
                            trailingIcon = {
                                IconButton(onClick = { showPlMenu = !showPlMenu }) {
                                    Icon(imageVector = Icons.Default.People, contentDescription = "عرض اللاعبين")
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                        DropdownMenu(
                            expanded = showPlMenu,
                            onDismissRequest = { showPlMenu = false },
                            modifier = Modifier.fillMaxWidth(0.9f)
                        ) {
                            players.forEach { pl ->
                                DropdownMenuItem(
                                    text = { Text("${pl.name} - ${pl.position} (#${pl.jerseyNumber})") },
                                    onClick = {
                                        selectedPlayer = pl
                                        showPlMenu = false
                                    }
                                )
                            }
                        }
                    }
                }
            }

            // Section 2: Active Stopwatch & Custom Timer Layout
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    
                    // Selected Exercise details overview
                    selectedExercise?.let { ex ->
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = ex.name,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "الهدف المبرمج: ${ex.targetDurationSeconds} ثواني | ${ex.category}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    // Large dynamic circular stopwatch visual
                    Box(
                        modifier = Modifier
                            .size(170.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        // Dynamic glowing neon outline depending on running state
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                                .background(
                                    if (isRunning) MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
                                    else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.03f)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            val displayMin = (timeMillis / 60000) % 60
                            val displaySec = (timeMillis / 1000) % 60
                            val displayMs = (timeMillis / 10) % 100
                            val timeStr = String.format(Locale.US, "%02d:%02d.%02d", displayMin, displaySec, displayMs)

                            Text(
                                text = timeStr,
                                style = MaterialTheme.typography.displayLarge.copy(
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Black
                                ),
                                color = if (isRunning) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    // Notes and direct recording parameters
                    OutlinedTextField(
                        value = notesInput,
                        onValueChange = { notesInput = it },
                        label = { Text("تعليق الكابتن على الأداء") },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        singleLine = true
                    )

                    // Stopwatch Action buttons
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        if (!isRunning && timeMillis == 0L) {
                            // First Start
                            Button(
                                onClick = { isRunning = true },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp)
                                    .testTag("start_stopwatch_button"),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                            ) {
                                Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "ابدأ")
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("ابدأ المؤقت", fontWeight = FontWeight.Bold)
                            }
                        } else if (isRunning) {
                            // Pause
                            Button(
                                onClick = { isRunning = false },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp)
                                    .testTag("pause_stopwatch_button"),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444))
                            ) {
                                Icon(imageVector = Icons.Default.Pause, contentDescription = "ايقاف مؤقت")
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("إيقاف مؤقت", fontWeight = FontWeight.Bold)
                            }
                        } else {
                            // Resume & Reset options
                            Row(
                                modifier = Modifier.weight(1f),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Button(
                                    onClick = { isRunning = true },
                                    modifier = Modifier.weight(1f).height(48.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                                ) {
                                    Text("استكمال")
                                }
                                OutlinedButton(
                                    onClick = { timeMillis = 0L },
                                    modifier = Modifier.weight(1f).height(48.dp)
                                ) {
                                    Text("تصفير")
                                }
                            }
                        }

                        // Save results
                        Button(
                            onClick = {
                                val player = selectedPlayer
                                val exercise = selectedExercise
                                if (player != null && exercise != null && timeMillis > 0L) {
                                    val secs = timeMillis.toDouble() / 1000.0
                                    viewModel.addPerformanceRecord(
                                        playerId = player.id,
                                        playerName = player.name,
                                        exerciseId = exercise.id,
                                        exerciseName = exercise.name,
                                        timeAchievedSeconds = secs,
                                        notes = notesInput
                                    )
                                    // Reset stopwatch
                                    timeMillis = 0L
                                    isRunning = false
                                    notesInput = ""
                                }
                            },
                            enabled = selectedPlayer != null && selectedExercise != null && timeMillis > 0L,
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .testTag("save_trial_button"),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.secondary,
                                disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
                            )
                        ) {
                            Icon(imageVector = Icons.Default.CheckCircle, contentDescription = "حفظ")
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("تسجيل النتيجة", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Custom Exercise Addition Dialogue
        if (showAddExerciseDialog) {
            AlertDialog(
                onDismissRequest = { showAddExerciseDialog = false },
                title = {
                    Text(
                        "إضافة تمرين / اختبار جديد",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Right
                    )
                },
                text = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedTextField(
                            value = exName,
                            onValueChange = { exName = it },
                            label = { Text("اسم روتين الاختبار الجديد") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        // Category Selector
                        Box(modifier = Modifier.fillMaxWidth()) {
                            OutlinedTextField(
                                value = exCategory,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("فئة التدريب") },
                                trailingIcon = {
                                    IconButton(onClick = { categoryExpanded = !categoryExpanded }) {
                                        Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { categoryExpanded = !categoryExpanded }
                            )
                            DropdownMenu(
                                expanded = categoryExpanded,
                                onDismissRequest = { categoryExpanded = false }
                            ) {
                                categories.forEach { cat ->
                                    DropdownMenuItem(
                                        text = { Text(cat) },
                                        onClick = {
                                            exCategory = cat
                                            categoryExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        OutlinedTextField(
                            value = exTargetStr,
                            onValueChange = { exTargetStr = it },
                            label = { Text("الزمن المستهدف (بالثواني)") },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = exDescription,
                            onValueChange = { exDescription = it },
                            label = { Text("وصف مختصر للتمارين وكيفية القياس") },
                            modifier = Modifier.fillMaxWidth(),
                            maxLines = 3
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            val targetSecs = exTargetStr.toIntOrNull() ?: 30
                            if (exName.isNotBlank()) {
                                viewModel.addExercise(
                                    name = exName,
                                    description = exDescription,
                                    category = exCategory,
                                    targetDurationSeconds = targetSecs
                                )
                                // reset fields
                                exName = ""
                                exTargetStr = ""
                                exDescription = ""
                                showAddExerciseDialog = false
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text("إضافة")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showAddExerciseDialog = false }) {
                        Text("إلغاء")
                    }
                }
            )
        }
    }
}
