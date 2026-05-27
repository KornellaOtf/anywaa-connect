package com.anywaa.connect.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.anywaa.connect.R

/**
 * Redesigned Home screen matching the requested UI/UX guidelines.
 * Features:
 * - Top bar with app logo, notification and settings icons.
 * - Central "Chat Now" button navigating to the chat route.
 * - Grid of four tool cards (Writing Aid, Translator, Transcriber, Image Generator).
 * - Row of utility chips (Scam Detector, Vibe Coder, Creator Generation, VibeVoice).
 * - No emojis or dash characters.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToFeature: (String) -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToPremium: () -> Unit,
    isPremium: Boolean = false
) {
    val tools = listOf(
        FeatureItem(R.string.feature_writing_aid, R.string.feature_writing_aid_desc, Icons.Filled.Edit, "writing_aid"),
        FeatureItem(R.string.feature_translator, R.string.feature_translator_desc, Icons.Filled.Language, "translator"),
        FeatureItem(R.string.feature_transcriber, R.string.feature_transcriber_desc, Icons.Filled.Mic, "transcriber"),
        FeatureItem(R.string.feature_image_generator, R.string.feature_image_generator_desc, Icons.Filled.Palette, "image_generator")
    )

    val utilities = listOf(
        FeatureItem(R.string.feature_scam_detector, R.string.feature_scam_detector_desc, Icons.Filled.Security, "scam_detector"),
        FeatureItem(R.string.feature_vibe_coder, R.string.feature_vibe_coder_desc, Icons.Filled.Code, "vibe_coder"),
        FeatureItem(R.string.feature_creator_generation, R.string.feature_creator_generation_desc, Icons.Filled.AutoAwesome, "creator_generation"),
        FeatureItem(R.string.feature_vibevoice, R.string.feature_vibevoice_desc, Icons.Filled.GraphicEq, "vibevoice")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_launcher_foreground),
                            contentDescription = null,
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stringResource(R.string.app_name),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* placeholder for notifications */ }) {
                        Icon(imageVector = Icons.Filled.Notifications, contentDescription = null)
                    }
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(imageVector = Icons.Filled.Settings, contentDescription = null)
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            // Primary "Chat Now" button
            Button(
                onClick = { onNavigateToFeature("chat") },
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF233E88))
            ) {
                Text(text = stringResource(R.string.chat_now), color = Color.White)
            }
            Spacer(modifier = Modifier.height(32.dp))
            // Tools grid (2 columns)
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(tools) { item ->
                    FeatureCard(item, isPremium, onNavigateToFeature, onNavigateToPremium)
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            // Utilities chips row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                utilities.forEach { item ->
                    UtilityChip(item, isPremium, onNavigateToFeature, onNavigateToPremium)
                }
            }
        }
    }
}

private data class FeatureItem(
    val titleRes: Int,
    val descRes: Int,
    val icon: ImageVector,
    val route: String
)

@Composable
private fun FeatureCard(
    item: FeatureItem,
    isPremium: Boolean,
    navigate: (String) -> Unit,
    premiumNav: () -> Unit
) {
    // Premium lock condition for specific routes
    val locked = !isPremium && item.route in setOf("image_generator", "vibe_coder")
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clickable { if (locked) premiumNav() else navigate(item.route) },
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(
            modifier = Modifier
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF667eea), Color(0xFF764ba2))
                    )
                )
                .padding(12.dp)
        ) {
            Column(
                modifier = Modifier.align(Alignment.CenterStart),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(imageVector = item.icon, contentDescription = null, tint = Color.White)
                Text(text = stringResource(item.titleRes), color = Color.White, style = MaterialTheme.typography.titleMedium)
                Text(text = stringResource(item.descRes), color = Color.White.copy(alpha = 0.8f), style = MaterialTheme.typography.bodySmall)
            }
            if (locked) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .background(Color(0xFFFFD700), shape = RoundedCornerShape(bottomStart = 8.dp))
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                ) {
                    Icon(imageVector = Icons.Default.Lock, contentDescription = null, tint = Color.Black, modifier = Modifier.size(12.dp))
                }
            }
        }
    }
}

@Composable
private fun UtilityChip(
    item: FeatureItem,
    isPremium: Boolean,
    navigate: (String) -> Unit,
    premiumNav: () -> Unit
) {
    val locked = !isPremium && item.route in setOf("image_generator", "vibe_coder")
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = if (locked) Color.Gray.copy(alpha = 0.3f) else Color(0xFFf0f0f0),
        tonalElevation = 2.dp,
        modifier = Modifier.clickable { if (locked) premiumNav() else navigate(item.route) }
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(imageVector = item.icon, contentDescription = null, tint = Color.Black, modifier = Modifier.size(16.dp))
            Text(text = stringResource(item.titleRes), style = MaterialTheme.typography.labelMedium)
        }
    }
}
