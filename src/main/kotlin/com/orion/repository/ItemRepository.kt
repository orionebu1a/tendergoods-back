package com.orion.repository

import com.orion.entity.Item
import com.orion.form.ItemDto
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant

class ItemRepository {

    fun findAll(): List<Item> = transaction {
        Item.all().toList()
    }

    fun findById(id: Int): Item? = transaction {
        Item.findById(id)
    }

    fun create(itemDto: ItemDto): Item = transaction {
        Item.new {
            title = itemDto.title
            description = itemDto.description
            totalAmount = itemDto.totalAmount
            category = itemDto.category
            imageUrl = itemDto.imageUrl
            createdAt = Instant.now()
            updatedAt = Instant.now()
        }
    }

    fun update(id: Int, itemDto: ItemDto): Boolean = transaction {
        val item = Item.findById(id) ?: return@transaction false

        item.title = itemDto.title
        item.description = itemDto.description
        item.totalAmount = itemDto.totalAmount
        item.category = itemDto.category
        item.imageUrl = itemDto.imageUrl
        item.updatedAt = Instant.now()

        return@transaction true
    }

    fun delete(id: Int): Boolean = transaction {
        val item = Item.findById(id)
        if (item == null) {
            return@transaction false
        } else {
            item.delete()
            return@transaction true
        }
    }
}