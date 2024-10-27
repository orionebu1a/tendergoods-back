package com.orion.model

import com.orion.serializer.InstantSerializer
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class MessageDto (
    val text: String,
    @Serializable(with = InstantSerializer::class)
    val createdAt: Instant
)

@Serializable
data class ChatsDto (
    val chats: List<Pair<Int, String?>>,
)

@Serializable
data class MessageForm (
    val bidId: Int,
    val receiverId: Int,
    val text: String,
)