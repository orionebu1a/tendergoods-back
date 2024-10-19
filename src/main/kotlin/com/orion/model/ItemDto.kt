package com.orion.model

import java.time.Instant

data class ItemDto (
    val id: Int,
    val userId: Int,
    val title: String,
    val description: String?,
    val totalAmount: Int,
    val categoryId: Int,
    val imageUrl: String? = null,
    val createdAt: Instant? = null,
    val updatedAt: Instant? = null,
)

data class ItemForm (
    val userId: Int,
    val title: String,
    val description: String?,
    val totalAmount: Int,
    val categoryId: Int,
    val imageUrl: String? = null,
    val createdAt: Instant? = null,
    val updatedAt: Instant? = null,
)