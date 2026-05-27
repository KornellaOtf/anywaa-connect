package com.anywaa.connect.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class Message(val text: String, val isUser: Boolean)

@Composable
fun ChatScreen() {
    var messages by remember { mutableStateOf(listOf<Message>()) }
    var inputText by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {
        // Header
        TopAppBar(title = { Text("Anywaa Connect") })

        // Messages
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp),
            reverseLayout = true
        ) {
            items(messages.reversed()) { msg ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = if (msg.isUser) Arrangement.End else Arrangement.Start
                ) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = if (msg.isUser) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Text(
                            text = msg.text,
                            modifier = Modifier.padding(12.dp),
                            color = if (msg.isUser) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }

        // Input Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = inputText,
                onValueChange = { inputText = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Type a message...") }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                if (inputText.isNotBlank()) {
                    val userMsg = Message(inputText, true)
                    messages = messages + userMsg
                    
                    // Simple stub reply
                    val replyText = when {
                        inputText.contains("hello", ignoreCase = true) -> "Hello! How can I help you today?"
                        inputText.contains("how are you", ignoreCase = true) -> "I'm running locally as a minimal version."
                        else -> "This is a stub response. Real AI will be added soon."
                    }
                    
                    messages = messages + Message(replyText, false)
                    inputText = ""
                }
            }) {
                Text("Send")
            }
        }
    }
}
