package com.orion.converter

import com.orion.entity.Item
import com.orion.model.ItemDto

fun Item.toDto(): ItemDto {
    return ItemDto(
        categoryId = this.category.value,
        description = this.description,
        title = this.title,
        totalAmount = this.totalAmount,
        userId = this.userId.value,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}