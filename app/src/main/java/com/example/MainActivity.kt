package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
            val viewModel: FootballViewModel = viewModel()

            // Athletic pitch-green theme styling
            MyApplicationTheme { 
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
                                Triple("اللاعبين", Icons.Default.People, 0),
                                Triple("دليل التدريبات", Icons.Default.Sports, 1),
                                Triple("التحليلات والمتابعة", Icons.Default.BarChart, 2)
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
                                            fontSize = 11.sp,
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
                                0 -> PlayersScreen(viewModel = viewModel)
                                1 -> TestsScreen(viewModel = viewModel)
                                2 -> AnalyticsScreen(viewModel = viewModel)
                            }
                        }
                        
                        // Integrated AdMob Banner at the bottom of screens
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
