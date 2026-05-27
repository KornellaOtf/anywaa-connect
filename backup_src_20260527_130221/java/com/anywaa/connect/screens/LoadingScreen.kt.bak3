package com.anywaa.connect.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.anywaa.connect.data.DownloadStatus
import com.anywaa.connect.data.LLMModel
import com.anywaa.connect.data.ModelData
import com.anywaa.connect.data.ModelDownloader
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun LoadingScreen(
    onNavigateNext: () -> Unit
) {
    val context = LocalContext.current
    var statusText by remember { mutableStateOf("Checking AI model...") }
    var progressPercent by remember { mutableStateOf(0f) }
    var downloadSpeed by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch(Dispatchers.IO) {
            // Get the default chat model or whichever is required to start
            val requiredModel = ModelData.models.find { it.modelFormat == "gguf" } ?: return@launch
            val httpClient = HttpClient(Android)
            val downloader = ModelDownloader(httpClient, context)

            try {
                // Try to ensure the model is available via assets or PAD
                withContext(Dispatchers.Main) { statusText = "Preparing AI engine..." }
                downloader.ensureModelAvailable(requiredModel)
                
                // If it succeeds, we are ready
                withContext(Dispatchers.Main) { onNavigateNext() }
                
            } catch (e: Exception) {
                // Model not found locally, trigger download flow
                withContext(Dispatchers.Main) { statusText = "Downloading AI model for offline use..." }
                
                downloader.downloadModel(requiredModel)
                    .catch { err ->
                        withContext(Dispatchers.Main) {
                            statusText = "Error: ${err.localizedMessage}"
                        }
                    }
                    .collect { status ->
                        withContext(Dispatchers.Main) {
                            if (status.totalBytes > 0) {
                                progressPercent = status.downloadedBytes.toFloat() / status.totalBytes.toFloat()
                                val downloadedMb = status.downloadedBytes / (1024 * 1024)
                                val totalMb = status.totalBytes / (1024 * 1024)
                                val speedMb = if (status.downloadSpeedBytesPerSec > 0) {
                                    "${String.format("%.1f", status.downloadSpeedBytesPerSec / (1024.0 * 1024.0))} MB/s"
                                } else ""
                                
                                statusText = if (status.isExtracting) "Extracting model files..." 
                                             else "Downloading ($downloadedMb MB / $totalMb MB)"
                                downloadSpeed = speedMb
                            }
                            
                            // Check if completed
                            if (status.downloadedBytes >= status.totalBytes && status.totalBytes > 0 && !status.isExtracting) {
                                statusText = "Ready."
                                onNavigateNext()
                            }
                        }
                    }
            } finally {
                httpClient.close()
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            Text(
                text = "Anywaa Connect",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp
            )
            
            Spacer(modifier = Modifier.height(48.dp))
            
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(64.dp)
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Text(
                text = statusText,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            
            if (progressPercent > 0f) {
                Spacer(modifier = Modifier.height(16.dp))
                LinearProgressIndicator(
                    progress = { progressPercent },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )
                
                if (downloadSpeed.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Speed: $downloadSpeed",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
