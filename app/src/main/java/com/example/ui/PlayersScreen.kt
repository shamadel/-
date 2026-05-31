package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.data.Player

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayersScreen(
    viewModel: FootballViewModel,
    modifier: Modifier = Modifier
) {
    val players by viewModel.players.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    // Form states
    var name by remember { mutableStateOf("") }
    var position by remember { mutableStateOf("مهاجم") }
    var ageStr by remember { mutableStateOf("") }
    var heightStr by remember { mutableStateOf("") }
    var weightStr by remember { mutableStateOf("") }
    var jerseyStr by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    val positions = listOf("مهاجم", "خط وسط", "مدافع", "حارس مرمى")
    var positionExpanded by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.testTag("add_player_fab")
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "إضافة لاعب")
                Spacer(modifier = Modifier.width(8.dp))
                Text("إضافة لاعب جديد", fontWeight = FontWeight.Bold)
            }
        },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "قائمة اللاعبين",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (players.isEmpty()) {
                // Empty state
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.People,
                        contentDescription = "لا يوجد لاعبين",
                        tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                        modifier = Modifier.size(72.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "لا يوجد لاعبون مسجلون حالياً",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "ابدأ بإضافة لاعبي فريقك لمتابعة اللياقة والمهارات بشكل متصل وزمني.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 16.dp),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 80.dp, top = 8.dp)
                ) {
                    items(players, key = { it.id }) { player ->
                        PlayerItem(
                            player = player,
                            onDelete = { viewModel.deletePlayer(player.id) }
                        )
                    }
                }
            }

            // Simple dialog layer for adding players
            if (showAddDialog) {
                AlertDialog(
                    onDismissRequest = { showAddDialog = false },
                    title = {
                        Text(
                            text = "تسجيل لاعب جديد",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Right
                        )
                    },
                    text = {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 420.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedTextField(
                                value = name,
                                onValueChange = { name = it },
                                label = { Text("اسم اللاعب بالكامل") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("player_name_input"),
                                singleLine = true
                            )

                            // Custom drop down for position
                            Box(modifier = Modifier.fillMaxWidth()) {
                                OutlinedTextField(
                                    value = position,
                                    onValueChange = {},
                                    readOnly = true,
                                    label = { Text("مركز اللعب") },
                                    trailingIcon = {
                                        IconButton(onClick = { positionExpanded = !positionExpanded }) {
                                            Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "عرض المراكز")
                                        }
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { positionExpanded = !positionExpanded }
                                )
                                DropdownMenu(
                                    expanded = positionExpanded,
                                    onDismissRequest = { positionExpanded = false },
                                    modifier = Modifier.fillMaxWidth(0.8f)
                                ) {
                                    positions.forEach { pos ->
                                        DropdownMenuItem(
                                            text = { Text(pos) },
                                            onClick = {
                                                position = pos
                                                positionExpanded = false
                                            }
                                        )
                                    }
                                }
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedTextField(
                                    value = ageStr,
                                    onValueChange = { ageStr = it },
                                    label = { Text("العمر") },
                                    modifier = Modifier.weight(1f),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    singleLine = true
                                )
                                OutlinedTextField(
                                    value = jerseyStr,
                                    onValueChange = { jerseyStr = it },
                                    label = { Text("الرقم") },
                                    modifier = Modifier.weight(1f),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    singleLine = true
                                )
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedTextField(
                                    value = heightStr,
                                    onValueChange = { heightStr = it },
                                    label = { Text("الطول (سم)") },
                                    modifier = Modifier.weight(1f),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    singleLine = true
                                )
                                OutlinedTextField(
                                    value = weightStr,
                                    onValueChange = { weightStr = it },
                                    label = { Text("الوزن (كجم)") },
                                    modifier = Modifier.weight(1f),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    singleLine = true
                                )
                            }

                            OutlinedTextField(
                                value = notes,
                                onValueChange = { notes = it },
                                label = { Text("ملاحظات الكابتن") },
                                modifier = Modifier.fillMaxWidth(),
                                maxLines = 3
                            )
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                val age = ageStr.toIntOrNull() ?: 18
                                val height = heightStr.toDoubleOrNull() ?: 175.0
                                val weight = weightStr.toDoubleOrNull() ?: 70.0
                                val jersey = jerseyStr.toIntOrNull() ?: 10

                                if (name.isNotBlank()) {
                                    viewModel.addPlayer(
                                        name = name,
                                        position = position,
                                        age = age,
                                        height = height,
                                        weight = weight,
                                        jerseyNumber = jersey,
                                        notes = notes
                                    )
                                    // Reset fields
                                    name = ""
                                    ageStr = ""
                                    heightStr = ""
                                    weightStr = ""
                                    jerseyStr = ""
                                    notes = ""
                                    showAddDialog = false
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                            modifier = Modifier.testTag("save_player_button")
                        ) {
                            Text("حفظ اللاعب")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showAddDialog = false }) {
                            Text("إلغاء")
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun PlayerItem(
    player: Player,
    onDelete: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "#${player.jerseyNumber}",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 18.sp
                        )
                    }

                    Column {
                        Text(
                            text = player.name,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        
                        // Badge color according to position
                        val badgeBg = when (player.position) {
                            "مهاجم" -> Color(0xFFEF4444).copy(alpha = 0.15f)
                            "خط وسط" -> Color(0xFF10B981).copy(alpha = 0.15f)
                            "مدافع" -> Color(0xFF3B82F6).copy(alpha = 0.15f)
                            else -> Color(0xFFF59E0B).copy(alpha = 0.15f)
                        }
                        val badgeTextColor = when (player.position) {
                            "مهاجم" -> Color(0xFFEF4444)
                            "خط وسط" -> Color(0xFF10B981)
                            "مدافع" -> Color(0xFF3B82F6)
                            else -> Color(0xFFF59E0B)
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(badgeBg)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = player.position,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = badgeTextColor
                            )
                        }
                    }
                }

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.testTag("delete_player_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "حذف اللاعب",
                        tint = MaterialTheme.colorScheme.error.copy(alpha = 0.8f)
                    )
                }
            }

            if (expanded) {
                Spacer(modifier = Modifier.height(16.dp))
                Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f))
                Spacer(modifier = Modifier.height(12.dp))

                // Physical status grid layout
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("العمر", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("${player.age} عام", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("الطول", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("${player.height} سم", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("الوزن", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("${player.weight} كجم", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                }

                if (player.notes.isNotBlank()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = MaterialTheme.colorScheme.background.copy(alpha = 0.5f),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(12.dp)
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.EditNote,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    "ملاحظات تدريبية:",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = player.notes,
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}
