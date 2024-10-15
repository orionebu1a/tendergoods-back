package com.orion.form

import java.time.Instant

data class ItemDto (
    val userId: Int,
    val title: String,
    val description: String,
    val totalAmount: Int,
    val category: String,
    //TODO emun
    val imageUrl: String? = null,
    val createdAt: Instant? = null,
    val updatedAt: Instant? = null,
)