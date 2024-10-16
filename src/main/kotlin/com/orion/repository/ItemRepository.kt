package com.orion.repository

import com.orion.converter.toDto
import com.orion.entity.Item
import com.orion.form.ItemDto
import com.orion.table.ItemTable
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant

class ItemRepository {

    fun findAllUserItems(userId: Int): List<ItemDto> = transaction {
        val items = Item.find { ItemTable.userId eq userId}.toList()
        return@transaction items.map { it.toDto() }
    }

    fun findAll(): List<ItemDto> = transaction {
        val items = Item.all().toList()
        return@transaction items.map { it.toDto() }
    }

    fun findById(id: Int): ItemDto? = transaction {
        val item = Item.findById(id)
        return@transaction item?.toDto()
    }

    fun create(itemDto: ItemDto): ItemDto = transaction {
        val item = Item.new {
            title = itemDto.title
            description = itemDto.description
            totalAmount = itemDto.totalAmount
            category = itemDto.category
            imageUrl = itemDto.imageUrl
            createdAt = Instant.now()
            updatedAt = Instant.now()
        }
        return@transaction item.toDto()
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