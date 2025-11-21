package com.example.muconnect.models

data class Chat(
    val chatId: String = "",
    val participants: List = emptyList(),
    val lastMessage: String = "",
    val lastMessageTime: Long = System.currentTimeMillis(),
    val lastMessageSenderId: String = "",
    val unreadCount: Map = emptyMap()
)