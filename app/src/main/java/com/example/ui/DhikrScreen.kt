package com.example.ui

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DhikrScreen(viewModel: ZadViewModel, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val lang by viewModel.selectedLanguage.collectAsStateWithLifecycle()
    val isAr = lang == "ar"

    val categories = listOf("الأذكار الصباحية", "الأذكار المسائية", "أذكار بعد الصلاة", "أذكار النوم")
    var selectedCategory by remember { mutableStateOf("الأذكار الصباحية") }

    val azkarList = remember(selectedCategory) {
        DhikrData.ITEMS.filter { it.category == selectedCategory }
    }

    // Capture temporary count states for the current session to simulate ticking off Azkars
    // Key: Dhikr.id, Value: Completed Counts
    var clickStates = remember(selectedCategory) { mutableStateMapOf<Int, Int>() }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Horizontally scrolling category chips
        ScrollableTabRow(
            selectedTabIndex = categories.indexOf(selectedCategory).coerceAtLeast(0),
            edgePadding = 16.dp,
            divider = {}
        ) {
            categories.forEach { cat ->
                val localizedCat = when (cat) {
                    "الأذكار الصباحية" -> if (isAr) "الصباح" else "Morning"
                    "الأذكار المسائية" -> if (isAr) "المساء" else "Evening"
                    "أذكار بعد الصلاة" -> if (isAr) "بعد الصلاة" else "After Prayer"
                    "أذكار النوم" -> if (isAr) "النوم" else "Sleep"
                    else -> cat
                }
                Tab(
                    selected = selectedCategory == cat,
                    onClick = { selectedCategory = cat },
                    text = { Text(localizedCat, fontWeight = FontWeight.Bold, fontSize = 14.sp) }
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Quick action row to reset current list
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${Localization.get("azkar", lang)} (${azkarList.size})",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary
            )
            IconButton(onClick = { clickStates.clear() }) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Reset",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(Localization.get("reset", lang), fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            itemsIndexed(azkarList) { index, item ->
                val progress = clickStates[item.id] ?: 0
                val isCompleted = progress >= item.countTarget

                val cardColor = if (isCompleted) {
                    MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
                } else {
                    MaterialTheme.colorScheme.surface
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            if (!isCompleted) {
                                val current = clickStates[item.id] ?: 0
                                clickStates[item.id] = current + 1
                                if (current + 1 >= item.countTarget) {
                                    Toast.makeText(context, "تقبل الله طاعتك!", Toast.LENGTH_SHORT).show()
                                }
                            }
                        },
                    colors = CardDefaults.cardColors(containerColor = cardColor),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = item.text,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Medium,
                                fontSize = 18.sp,
                                lineHeight = 32.sp
                            ),
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Right,
                            modifier = Modifier.fillMaxWidth()
                        )

                        if (!isAr && item.englishText.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = item.englishText,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray,
                                textAlign = TextAlign.Left,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = item.reward,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.secondary,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Right,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Counter indicators
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = if (isCompleted) Icons.Default.DoneAll else Icons.Default.KeyboardArrowLeft,
                                    contentDescription = "Status",
                                    tint = if (isCompleted) MaterialTheme.colorScheme.primary else Color.Gray
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (isCompleted) "مكتمل (Completed)" else "${Localization.get("target", lang)}: ${item.countTarget}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isCompleted) MaterialTheme.colorScheme.primary else Color.Gray
                                )
                            }

                            // Circular Clicker indicator bubble
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (isCompleted) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondaryContainer
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (isCompleted) "✓" else "${item.countTarget - progress}",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black),
                                    color = if (isCompleted) Color.White else MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}
