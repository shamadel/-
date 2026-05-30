package com.example.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.BuildConfig
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

@Composable
fun AdmobBanner(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    
    // Retrieve banner ad unit ID from BuildConfig or use Google's official Test Ad Unit ID as a fallback:
    val adUnitId = remember {
        val configuredId = try {
            BuildConfig.ADMOB_BANNER_AD_UNIT_ID
        } catch (e: Exception) {
            ""
        }
        if (configuredId.isNullOrEmpty() || configuredId == "MY_BANNER_AD_UNIT_ID_DEFAULT_VALUE") {
            "ca-app-pub-3940256099942544/6300978111" // Official Google Test Banner ID
        } else {
            configuredId
        }
    }

    var isAdFailedToLoad by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        contentAlignment = Alignment.Center
    ) {
        if (isAdFailedToLoad) {
            // Friendly Islamic reminder spacer/replacement when ad isn't loaded/offline/failed
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Remembrance",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "﴿ أَلَا بِذِكْرِ اللَّهِ تَطْمَئِنُّ الْقُلُوبُ ﴾",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        ),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        } else {
            // Standard Admob Banner using AndroidView
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Symmetrical thin "Sponsored" tag in Arabic/English to match premium styling and respect store policies
                Text(
                    text = "إعلان - Sponsored",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium
                    ),
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f),
                    modifier = Modifier.padding(bottom = 2.dp)
                )
                
                AndroidView(
                    modifier = Modifier.fillMaxWidth(),
                    factory = { ctx ->
                        AdView(ctx).apply {
                            setAdSize(AdSize.BANNER)
                            setAdUnitId(adUnitId)
                            
                            // Load ad
                            val adRequest = AdRequest.Builder().build()
                            loadAd(adRequest)
                            
                            adListener = object : com.google.android.gms.ads.AdListener() {
                                override fun onAdFailedToLoad(error: com.google.android.gms.ads.LoadAdError) {
                                    super.onAdFailedToLoad(error)
                                    isAdFailedToLoad = true
                                }
                            }
                        }
                    },
                    update = { adView ->
                        // Component updates can occur here
                    }
                )
            }
        }
    }
}
