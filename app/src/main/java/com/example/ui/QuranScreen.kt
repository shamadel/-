package com.example.ui

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.*
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuranScreen(viewModel: ZadViewModel, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val lang by viewModel.selectedLanguage.collectAsStateWithLifecycle()
    val isAr = lang == "ar"

    var selectedTab by remember { mutableStateOf(0) } // 0: Read & Search, 1: Reading Stats & Goals

    val tabs = listOf(
        Localization.get("quran", lang),
        Localization.get("khatm_scheduler", lang)
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TabRow(selectedTabIndex = selectedTab) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp) }
                )
            }
        }

        when (selectedTab) {
            0 -> ReadAndSearchSection(viewModel, lang, isAr)
            1 -> StatsAndGoalsSection(viewModel, lang, isAr)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReadAndSearchSection(viewModel: ZadViewModel, lang: String, isAr: Boolean) {
    var searchQuery by remember { mutableStateOf("") }
    val searchResults = remember(searchQuery) {
        QuranData.searchVerses(searchQuery)
    }

    var selectedSurah by remember { mutableStateOf<Surah?>(null) }
    var activeTafsirVerse by remember { mutableStateOf<Verse?>(null) }
    var activeNoteVerse by remember { mutableStateOf<Verse?>(null) }
    var showQuickLogDialog by remember { mutableStateOf(false) }
    var pageInput by remember { mutableStateOf("1") }

    if (showQuickLogDialog && selectedSurah != null) {
        val surah = selectedSurah!!
        Dialog(onDismissRequest = { showQuickLogDialog = false }) {
            Card(shape = RoundedCornerShape(24.dp), modifier = Modifier.padding(16.dp)) {
                Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${Localization.get("log_pages", lang)} (${if (isAr) surah.nameArabic else surah.nameEnglish})",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    OutlinedTextField(
                        value = pageInput,
                        onValueChange = { pageInput = it },
                        label = { Text("عدد الصفحات (Pages count)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            val pages = pageInput.toIntOrNull() ?: 1
                            viewModel.logReading(surah.number, surah.nameArabic, pages)
                            showQuickLogDialog = false
                            Toast.makeText(viewModel.getApplication(), "تم تسجيل القراءة بنجاح!", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(Localization.get("log_pages_button", lang))
                    }
                }
            }
        }
    }

    if (selectedSurah != null) {
        // Render Active Surah Reading View
        val surah = selectedSurah!!
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(horizontal = 8.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { selectedSurah = null }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = MaterialTheme.colorScheme.onPrimaryContainer)
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (isAr) surah.nameArabic else surah.nameEnglish,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }

                IconButton(onClick = { showQuickLogDialog = true }) {
                    Icon(Icons.Default.PostAdd, contentDescription = "Log Reading", tint = MaterialTheme.colorScheme.onPrimaryContainer)
                }
            }

            // Surah Details Card
            Card(
                modifier = Modifier.fillMaxWidth().padding(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "نزولها: ${surah.type}",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "ترتيبها: ${surah.number}",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "أياتها: ${surah.verses.size}",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }

            LazyColumn(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
                // If not Fatihah and not Al-Tawbah, print Basmala at start
                if (surah.number != 1 && surah.number != 9) {
                    item {
                        Box(modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp), contentAlignment = Alignment.Center) {
                            Text(
                                "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
                                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.primary,
                                fontFamily = FontFamily.Serif
                            )
                        }
                    }
                }

                items(surah.verses) { verse ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            // Arabic Verse text
                            Text(
                                text = "${verse.textArabic} ﴿${verse.verseNumber}﴾",
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 24.sp,
                                    lineHeight = 42.sp
                                ),
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Right,
                                fontFamily = FontFamily.Serif
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            // English Translation
                            Text(
                                text = verse.textEnglish,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Left
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            // Quick Interactive Action Pill Rows
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                // Add Note button
                                AssistChip(
                                    onClick = { activeNoteVerse = verse },
                                    label = { Text(Localization.get("add_note", lang)) },
                                    leadingIcon = { Icon(Icons.Default.EditNote, contentDescription = "Note") }
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                // Tafsir button
                                AssistChip(
                                    onClick = { activeTafsirVerse = verse },
                                    label = { Text(Localization.get("view_tafsir", lang)) },
                                    leadingIcon = { Icon(Icons.Default.AutoStories, contentDescription = "Tafsir") }
                                )
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }

        // Verse notes adding overlay
        if (activeNoteVerse != null) {
            val v = activeNoteVerse!!
            var noteInput by remember { mutableStateOf("") }
            Dialog(onDismissRequest = { activeNoteVerse = null }) {
                Card(shape = RoundedCornerShape(24.dp)) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = "${Localization.get("add_note", lang)} (${if (isAr) surah.nameArabic else surah.nameEnglish} - ${v.verseNumber})",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                        OutlinedTextField(
                            value = noteInput,
                            onValueChange = { noteInput = it },
                            placeholder = { Text(Localization.get("note_hint", lang)) },
                            modifier = Modifier.fillMaxWidth().height(100.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                viewModel.addQuranNote(surah.number, surah.nameArabic, v.verseNumber, noteInput)
                                activeNoteVerse = null
                                Toast.makeText(viewModel.getApplication(), "تمت إضافة ملاحظتك وحفظها محلياً!", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(Localization.get("save", lang))
                        }
                    }
                }
            }
        }

        // Verse Tafsir dialog
        if (activeTafsirVerse != null) {
            val v = activeTafsirVerse!!
            Dialog(onDismissRequest = { activeTafsirVerse = null }) {
                Card(shape = RoundedCornerShape(24.dp)) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = "${Localization.get("tafsir_title", lang)} ﴿${v.verseNumber}﴾",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                        Text(
                            text = v.tafsirArabic,
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Right,
                            modifier = Modifier.fillMaxWidth(),
                            lineHeight = 28.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { activeTafsirVerse = null },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("اغلاق (Close)")
                        }
                    }
                }
            }
        }

    } else {
        // Surah selection & Adv search list
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            // Search Input
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text(Localization.get("quran_search", lang)) },
                placeholder = { Text(Localization.get("search_hint", lang)) },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search Icon") },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear")
                        }
                    }
                },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (searchQuery.isNotEmpty()) {
                // Search Results
                Text(
                    text = "${Localization.get("bookmarks", lang)} - نتائج البحث (${searchResults.size})",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(bottom = 10.dp)
                )

                if (searchResults.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(Localization.get("no_results", lang), color = Color.Gray, textAlign = TextAlign.Center)
                    }
                } else {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(searchResults) { result ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 5.dp)
                                    .clickable {
                                        // Pick the Surah of the search outcome
                                        selectedSurah = QuranData.SURAHS.find { it.number == result.surahNumber }
                                        searchQuery = "" // Reset
                                    },
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            result.surahName,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                        Text(
                                            "${Localization.get("verse", lang)} ${result.verse.verseNumber}",
                                            style = MaterialTheme.typography.labelSmall
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        result.verse.textArabic,
                                        style = MaterialTheme.typography.bodyMedium.copy(fontFamily = FontFamily.Serif),
                                        textAlign = TextAlign.Right,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        result.verse.tafsirArabic,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }

            } else {
                // Typical Surah Index list
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(QuranData.SURAHS) { surah ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                                .clickable { selectedSurah = surah },
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(CircleShape)
                                            .background(MaterialTheme.colorScheme.primaryContainer),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = surah.number.toString(),
                                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(14.dp))
                                    Column {
                                        Text(
                                            text = if (isAr) surah.nameArabic else surah.nameEnglish,
                                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                        )
                                        Text(
                                            text = surah.nameEnglish,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = surah.type,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.secondary,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Icon(Icons.Default.ChevronRight, contentDescription = "Go", tint = Color.Gray)
                                }
                            }
                        }
                    }
                    item {
                        Spacer(modifier = Modifier.height(80.dp))
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsAndGoalsSection(viewModel: ZadViewModel, lang: String, isAr: Boolean) {
    val activeGoal by viewModel.activeGoal.collectAsStateWithLifecycle()
    val allLogs by viewModel.allLogs.collectAsStateWithLifecycle()
    val totalPagesRead by viewModel.totalPagesRead.collectAsStateWithLifecycle()
    val allNotes by viewModel.allNotes.collectAsStateWithLifecycle()

    var showNewGoalDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Goal header Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = Localization.get("active_goal", lang),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )

                if (activeGoal != null) {
                    val goal = activeGoal!!
                    val pagesLeft = (goal.totalPages - goal.currentPage).coerceAtLeast(0)
                    val daysPassed = ((System.currentTimeMillis() - goal.startDate) / (1000 * 60 * 60 * 24)).coerceAtLeast(0).toInt()
                    val daysLeft = (goal.targetDays - daysPassed).coerceAtLeast(0)
                    val percent = ((goal.currentPage.toFloat() / goal.totalPages.toFloat()) * 100).toInt().coerceIn(0, 100)

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "$percent%",
                        style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Black),
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    LinearProgressIndicator(
                        progress = goal.currentPage.toFloat() / goal.totalPages.toFloat(),
                        modifier = Modifier.fillMaxWidth().height(10.dp).clip(RoundedCornerShape(5.dp)),
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("${goal.currentPage}/${goal.totalPages}", fontWeight = FontWeight.Bold)
                            Text(Localization.get("pages_done", lang), fontSize = 11.sp, color = Color.Gray)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(pagesLeft.toString(), fontWeight = FontWeight.Bold)
                            Text(Localization.get("pages_left", lang), fontSize = 11.sp, color = Color.Gray)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(daysLeft.toString(), fontWeight = FontWeight.Bold)
                            Text(Localization.get("days_left", lang), fontSize = 11.sp, color = Color.Gray)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Daily goal computation
                    val dailyTarget = if (daysLeft > 0) String.format("%.1f", pagesLeft.toFloat() / daysLeft) else ""
                    if (dailyTarget.isNotEmpty()) {
                        Text(
                            text = "${Localization.get("daily_target", lang)}: $dailyTarget صفحة / يوم",
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }

                } else {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "لا توجد ختمة نشطة حالياً",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(onClick = { showNewGoalDialog = true }) {
                        Icon(Icons.Default.AddTask, contentDescription = "New")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(Localization.get("start_khatm", lang))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Live Custom Canvas Graph showing progress reports index
        Text(
            text = "مخطط الإنجاز القرآني الأسبوعي (Weekly Reading Chart)",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
            textAlign = TextAlign.Start,
            color = MaterialTheme.colorScheme.primary
        )

        Card(
            modifier = Modifier.fillMaxWidth().height(200.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Box(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                // Take reading logs for past days and draw a bar/line chart using direct Canvas drawing API
                val baseLineColor = MaterialTheme.colorScheme.primary
                val accentColor = MaterialTheme.colorScheme.secondary

                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height

                    // Drawing background horizontal gridlines
                    val gridPaint = Stroke(width = 1f)
                    for (i in 1..4) {
                        val yPos = h * (i / 5f)
                        drawLine(
                            color = Color.Gray.copy(alpha = 0.2f),
                            start = androidx.compose.ui.geometry.Offset(0f, yPos),
                            end = androidx.compose.ui.geometry.Offset(w, yPos),
                            strokeWidth = 1f
                        )
                    }

                    // Let's draw mock visual historic peaks or map real logs if we have them
                    // Standard mock points representing days 1 to 7 reading progress
                    val points = mutableListOf<Float>()
                    if (allLogs.isEmpty()) {
                        points.addAll(listOf(5f, 12f, 8f, 22f, 15f, 30f, 45f)) // Beautiful default placeholder trend
                    } else {
                        // Gather reading logs sorted by day in current week
                        val testPages = allLogs.map { it.pagesRead.toFloat() }.take(7).reversed()
                        points.addAll(testPages)
                        while (points.size < 7) {
                            points.add(0, 0f) // Fill back with zeros
                        }
                    }

                    val maxVal = (points.maxOrNull() ?: 50f).coerceAtLeast(30f)
                    val stepX = w / 6f
                    val path = Path()

                    points.forEachIndexed { idx, value ->
                        val x = idx * stepX
                        val y = h - ((value / maxVal) * h * 0.82f) // Scale inside canvas height

                        if (idx == 0) {
                            path.moveTo(x, y)
                        } else {
                            path.lineTo(x, y)
                        }

                        // Draw golden dot on apexes
                        drawCircle(
                            color = accentColor,
                            radius = 6.dp.toPx(),
                            center = androidx.compose.ui.geometry.Offset(x, y)
                        )
                    }

                    // Draw the line path connection
                    drawPath(
                        path = path,
                        color = baseLineColor,
                        style = Stroke(width = 4.dp.toPx())
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // User notes/commentaries list saved
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${Localization.get("notes", lang)} (${allNotes.size})",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        if (allNotes.isEmpty()) {
            Text(
                text = "لا توجد ملاحظات وتفاسير محفوظة بعد. يمكنك كتابتها أثناء قراءة السور.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        } else {
            allNotes.forEach { note ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "${note.surahName} - آية ${note.verseNumber}",
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.secondary
                            )
                            IconButton(
                                onClick = { viewModel.deleteQuranNote(note) },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.LightGray)
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = note.note,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        val dateFormatted = remember(note.timestamp) {
                            SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date(note.timestamp))
                        }
                        Text(
                            text = dateFormatted,
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.Gray
                        )
                    }
                }
            }
        }

        // Standard Reading Log lists
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = Localization.get("history_log", lang) + " (${allLogs.size})",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(10.dp))

        allLogs.take(5).forEach { log ->
            Card(
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier.padding(12.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = log.surahName, fontWeight = FontWeight.Bold)
                    Text(text = "+${log.pagesRead} صفحة (pages)")
                }
            }
        }

        Spacer(modifier = Modifier.height(60.dp))
    }

    // New Goal Creator dialog
    if (showNewGoalDialog) {
        var daysInput by remember { mutableStateOf("30") }
        Dialog(onDismissRequest = { showNewGoalDialog = false }) {
            Card(shape = RoundedCornerShape(24.dp)) {
                Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = Localization.get("start_khatm", lang),
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = Localization.get("khatm_hint", lang),
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    OutlinedTextField(
                        value = daysInput,
                        onValueChange = { daysInput = it },
                        label = { Text("مدة الختمة بالأيام (Khatm period in days)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = {
                            val days = daysInput.toIntOrNull() ?: 30
                            viewModel.startNewKhatmGoal(days)
                            showNewGoalDialog = false
                            Toast.makeText(viewModel.getApplication(), "تم إنشاء الختمة بنجاح! طاب مسعاك.", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(Localization.get("start_khatm", lang))
                    }
                }
            }
        }
    }
}
