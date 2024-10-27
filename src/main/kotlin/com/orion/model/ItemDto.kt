package com.orion.model

import com.orion.serializer.InstantSerializer
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class ItemDto (
    val id: Int,
    val userId: Int,
    val title: String,
    val description: String?,
    val totalAmount: Int,
    val categoryId: Int,
    val imageUrl: String? = null,
    @Serializable(with = InstantSerializer::class)
    val createdAt: Instant? = null,
    @Serializable(with = InstantSerializer::class)
    val updatedAt: Instant? = null,
)

@Serializable
data class ItemForm (
    val title: String,
    val description: String?,
    val totalAmount: Int,
    val categoryId: Int,
    val imageUrl: String? = null,
)