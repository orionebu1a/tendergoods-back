package com.orion.converter

import com.orion.entity.Item
import com.orion.form.ItemDto

//fun ItemDto.toModel(): Item {
//    return Item.new {
//        createdAt = this@toModel.createdAt!!
//        updatedAt = this@toModel.updatedAt!!
//    }
//}
//TODO

fun Item.toDto(): ItemDto {
    return ItemDto(
        category = this.category,
        description = this.description,
        title = this.title,
        totalAmount = this.totalAmount,
        userId = this.userId.value,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}