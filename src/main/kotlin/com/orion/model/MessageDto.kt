package com.orion.model

import com.orion.serializer.InstantSerializer
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class MessageDto (
    val text: String,
    @Serializable(with = InstantSerializer::class)
    val createdAt: Instant,
    val sender: Int
)

@Serializable
data class ChatsDto (
    val userNameLastMessage: List<BidChatPreviewDto>,
)

@Serializable
data class MessageForm (
    val bidId: Int,
    val receiverId: Int,
    val text: String,
)

@Serializable
data class BidChatPreviewDto (
    val bidId: Int,
    val userName: String,
    val lastMessage: String?,
)