package com.anywaa.connect.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.anywaa.connect.data.ChatEntity
import com.anywaa.connect.data.AnywaaConnectDatabase
import com.anywaa.connect.repository.ChatRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ChatDrawerViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ChatRepository

    val allChats: StateFlow<List<ChatEntity>>
    val allCreators: StateFlow<List<com.anywaa.connect.data.CreatorEntity>>

    init {
        val database = AnywaaConnectDatabase.getDatabase(application)
        repository = ChatRepository(database.chatDao(), database.messageDao(), database.creatorDao())

        allChats = repository.getActiveChats()
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                emptyList()
            )
            
        allCreators = repository.getAllCreators()
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                emptyList()
            )
    }

    fun deleteChat(chatId: String) = viewModelScope.launch {
        repository.deleteChat(chatId)
    }

    fun renameChat(chatId: String, newTitle: String) = viewModelScope.launch {
        repository.updateChatTitle(chatId, newTitle.trim())
    }

    fun renameCreator(creatorId: String, newName: String) = viewModelScope.launch {
        val creator = repository.getCreatorById(creatorId)
        if (creator != null) {
            repository.insertCreator(creator.copy(name = newName.trim()))
        }
    }

    fun updateCreator(creator: com.anywaa.connect.data.CreatorEntity) = viewModelScope.launch {
        try {
            repository.insertCreator(
                creator.copy(
                    name = creator.name.trim(),
                    description = creator.description.trim(),
                    pctfPrompt = creator.pctfPrompt.trim(),
                    icon = creator.icon.trim().ifBlank { "🤖" }
                )
            )
        } catch (e: Exception) {
            Log.e("ChatDrawerViewModel", "Failed to update creator ${creator.id}: ${e.message}", e)
        }
    }

    fun deleteAllChats() = viewModelScope.launch {
        repository.deleteAllChats()
    }
    
    fun deleteCreator(creator: com.anywaa.connect.data.CreatorEntity) = viewModelScope.launch {
        try {
            repository.deleteCreator(creator)
        } catch (e: Exception) {
            Log.e("ChatDrawerViewModel", "Failed to delete creator ${creator.id}: ${e.message}", e)
        }
    }
} 