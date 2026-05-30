package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.*
import com.example.ui.theme.MyApplicationTheme
import com.google.android.gms.ads.MobileAds

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialize Google Mobile Ads SDK asynchronously
        try {
            MobileAds.initialize(this) {}
        } catch (e: Exception) {
            e.printStackTrace()
        }

        enableEdgeToEdge()
        setContent {
            val viewModel: ZadViewModel = viewModel()
            val darkModeOverride by viewModel.darkModeOverride.collectAsStateWithLifecycle()
            val selectedLanguage by viewModel.selectedLanguage.collectAsStateWithLifecycle()

            // Resolve whether to use Light or Dark theme
            val isDarkTheme = when (darkModeOverride) {
                true -> true
                false -> false
                null -> androidx.compose.foundation.isSystemInDarkTheme()
            }

            MyApplicationTheme(darkTheme = isDarkTheme) {
                var selectedTabIndex by remember { mutableStateOf(0) }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        NavigationBar(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(72.dp)
                                .windowInsetsPadding(WindowInsets.navigationBars),
                            containerColor = MaterialTheme.colorScheme.surface,
                            tonalElevation = 8.dp
                        ) {
                            val navItems = listOf(
                                Triple(Localization.get("prayers", selectedLanguage), Icons.Default.AccessTime, 0),
                                Triple(Localization.get("quran", selectedLanguage), Icons.Default.Book, 1),
                                Triple(Localization.get("azkar", selectedLanguage), Icons.Default.VolunteerActivism, 2),
                                Triple(Localization.get("tasbih", selectedLanguage), Icons.Default.Adjust, 3),
                                Triple(Localization.get("qibla", selectedLanguage), Icons.Default.Explore, 4),
                                Triple(Localization.get("sync", selectedLanguage), Icons.Default.Settings, 5)
                            )

                            navItems.forEach { item ->
                                val isSelected = selectedTabIndex == item.third
                                NavigationBarItem(
                                    selected = isSelected,
                                    onClick = { selectedTabIndex = item.third },
                                    icon = {
                                        Icon(
                                            imageVector = item.second,
                                            contentDescription = item.first,
                                            tint = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    },
                                    label = {
                                        Text(
                                            text = item.first,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                            fontSize = 9.sp,
                                            maxLines = 1,
                                            color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray
                                        )
                                    },
                                    alwaysShowLabel = true
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                        ) {
                            when (selectedTabIndex) {
                                0 -> PrayerTimesScreen(viewModel = viewModel)
                                1 -> QuranScreen(viewModel = viewModel)
                                2 -> DhikrScreen(viewModel = viewModel)
                                3 -> TasbihScreen(viewModel = viewModel)
                                4 -> CompassScreen(viewModel = viewModel)
                                5 -> SyncScreen(viewModel = viewModel)
                            }
                        }
                        
                        // Integrated AdMob Banner at the bottom of every tab (Modern standard)
                        AdmobBanner(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.surface)
                        )
                    }
                }
            }
        }
    }
}
