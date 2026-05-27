import sys

def patch():
    file_path = "/home/kornella/LLM-Hub/android/app/src/main/java/com/anywaa/connect/components/ChatComponents.kt"
    with open(file_path, "r") as f:
        lines = f.readlines()
        
    start_idx = 713
    end_idx = 1154
    
    new_bubble = """@Composable
fun MessageBubble(
    message: MessageEntity,
    streamingContent: String = "",
    onRegenerateResponse: (() -> Unit)? = null,
    onEditUserMessage: (() -> Unit)? = null,
    onEditAssistantMessage: ((String) -> Unit)? = null,
    onTtsSpeak: ((String) -> Unit)? = null,
    onTtsStop: (() -> Unit)? = null,
    isTtsSpeaking: Boolean = false
) {
    var showFullScreenImage by remember { mutableStateOf(false) }
    var isEditingAssistantMessage by remember(message.id) { mutableStateOf(false) }
    var editedAssistantText by remember(message.id) { mutableStateOf("") }
    val context = LocalContext.current
    val isUser = message.isFromUser
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
    ) {
        if (isUser) {
            Surface(
                modifier = Modifier
                    .wrapContentWidth()
                    .widthIn(max = 300.dp),
                shape = RoundedCornerShape(
                    topStart = 20.dp,
                    topEnd = 20.dp,
                    bottomStart = 20.dp,
                    bottomEnd = 4.dp
                ),
                color = Color.Transparent, // We'll draw gradient in Modifier
                shadowElevation = 0.dp
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            brush = androidx.compose.ui.graphics.Brush.horizontalGradient(
                                colors = listOf(com.anywaa.connect.ui.theme.UserBubbleGradientStart, com.anywaa.connect.ui.theme.UserBubbleGradientEnd)
                            )
                        )
                        .padding(16.dp)
                ) {
                    Column {
                        // Attachments
                        if (message.attachmentPath != null && message.attachmentType != null) {
                            when (message.attachmentType.lowercase()) {
                                "image" -> {
                                    AsyncImage(
                                        model = ImageRequest.Builder(context).data(Uri.parse(message.attachmentPath)).crossfade(true).build(),
                                        contentDescription = "Attached image",
                                        modifier = Modifier.fillMaxWidth().heightIn(max = 200.dp).clip(RoundedCornerShape(12.dp)).clickable { showFullScreenImage = true },
                                        contentScale = ContentScale.Crop
                                    )
                                }
                                "audio" -> {
                                    AudioMessageCard(audioPath = message.attachmentPath, fileName = message.attachmentFileName ?: "Audio", fileSize = message.attachmentFileSize, isFromUser = message.isFromUser)
                                }
                                else -> {
                                    FileAttachmentCard(attachmentPath = message.attachmentPath, attachmentType = message.attachmentType, attachmentFileName = message.attachmentFileName, attachmentFileSize = message.attachmentFileSize, isFromUser = message.isFromUser)
                                }
                            }
                            if (message.content.isNotEmpty() && message.content != "Shared a file") {
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                        
                        if (message.content.isNotEmpty() && message.content != "Shared a file") {
                            SelectableMarkdownText(
                                markdown = message.content,
                                color = Color.White,
                                fontSize = MaterialTheme.typography.bodyLarge.fontSize
                            )
                        }
                    }
                }
            }
            // User actions
            Row(modifier = Modifier.padding(top = 4.dp), horizontalArrangement = Arrangement.End) {
                IconButton(onClick = {
                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    clipboard.setPrimaryClip(ClipData.newPlainText("Message", message.content))
                }, modifier = Modifier.size(24.dp)) {
                    Icon(Icons.Outlined.ContentCopy, contentDescription = "Copy", modifier = Modifier.size(14.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                if (onEditUserMessage != null) {
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(onClick = onEditUserMessage, modifier = Modifier.size(24.dp)) {
                        Icon(Icons.Outlined.Edit, contentDescription = "Edit", modifier = Modifier.size(14.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        } else {
            // AI Bubble
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            modifier = Modifier.size(24.dp),
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primary
                        ) {
                            Icon(Icons.Default.SmartToy, contentDescription = "AI", tint = Color.White, modifier = Modifier.padding(4.dp))
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Anywaa", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold), color = MaterialTheme.colorScheme.onSurface)
                        Spacer(modifier = Modifier.weight(1f))
                        Text("Just now", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant) // Mock timestamp
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    val displayContent = if (streamingContent.isNotEmpty()) streamingContent else message.content
                    val (thinkingPart, answerPart) = parseThinkingAndAnswer(displayContent)
                    
                    if (thinkingPart.isNotEmpty()) {
                        var thinkingExpanded by remember(message.id) { mutableStateOf(true) }
                        LaunchedEffect(message.id, answerPart) {
                            if (answerPart.isNotEmpty()) thinkingExpanded = false
                        }
                        
                        Surface(
                            modifier = Modifier.fillMaxWidth().clickable { thinkingExpanded = !thinkingExpanded }.padding(bottom = 8.dp),
                            color = MaterialTheme.colorScheme.background,
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Text(
                                    text = if (thinkingExpanded) "▼ Thought for X seconds" else "▶ Thought for X seconds",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                if (thinkingExpanded) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = thinkingPart,
                                        style = MaterialTheme.typography.bodyMedium.copy(fontFamily = FontFamily.Monospace),
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                    
                    if (answerPart.isNotEmpty() || (!displayContent.contains(SENTINEL_THINK) && !displayContent.contains(RAW_OPEN_THINK))) {
                        val mainContent = if (answerPart.isNotEmpty()) answerPart else displayContent
                        SelectableMarkdownText(
                            markdown = mainContent,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = MaterialTheme.typography.bodyLarge.fontSize
                        )
                    }
                    
                    // AI Actions
                    Row(modifier = Modifier.padding(top = 8.dp).fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
                        IconButton(onClick = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clipText = if (answerPart.isNotEmpty()) answerPart else (if (streamingContent.isNotEmpty()) streamingContent else message.content)
                            clipboard.setPrimaryClip(ClipData.newPlainText("Message", clipText))
                        }, modifier = Modifier.size(24.dp)) {
                            Icon(Icons.Outlined.ContentCopy, contentDescription = "Copy", modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        if (onRegenerateResponse != null && !message.isStreaming && streamingContent.isEmpty()) {
                            Spacer(modifier = Modifier.width(8.dp))
                            IconButton(onClick = onRegenerateResponse, modifier = Modifier.size(24.dp)) {
                                Icon(Icons.Outlined.Refresh, contentDescription = "Regenerate", modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                        if (onTtsSpeak != null) {
                            Spacer(modifier = Modifier.width(8.dp))
                            IconButton(onClick = {
                                if (isTtsSpeaking) onTtsStop?.invoke()
                                else {
                                    val ttsText = if (answerPart.isNotEmpty()) answerPart else (if (streamingContent.isNotEmpty()) streamingContent else message.content)
                                    onTtsSpeak(ttsText)
                                }
                            }, modifier = Modifier.size(24.dp)) {
                                Icon(if (isTtsSpeaking) Icons.Default.Stop else Icons.Default.VolumeUp, contentDescription = "TTS", modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
            }
        }
    }
}
"""
    
    new_lines = lines[:start_idx] + [new_bubble + "\n"] + lines[end_idx:]
    with open(file_path, "w") as f:
        f.writelines(new_lines)

patch()
