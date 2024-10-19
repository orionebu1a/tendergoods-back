package com.orion.service

import User
import com.orion.converter.toDto
import com.orion.entity.Item
import com.orion.entity.ItemCategory
import com.orion.model.ItemDto
import com.orion.table.ItemTable
import io.ktor.server.plugins.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant

class ItemService {
    fun findAllUserItems(userId: Int): List<ItemDto> = transaction {
        val items = Item.find { ItemTable.user eq userId}.toList()
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

    fun create(itemDto: ItemDto, principal: User): ItemDto = transaction {
        val itemCategory = ItemCategory.findById(itemDto.categoryId)
            ?: throw NotFoundException("Category not found")

        val item = Item.new {
            user = principal
            title = itemDto.title
            description = itemDto.description
            totalAmount = itemDto.totalAmount
            category = itemCategory.id
            imageUrl = itemDto.imageUrl
            createdAt = Instant.now()
            updatedAt = Instant.now()
        }
        return@transaction item.toDto()
    }

    fun update(id: Int, itemDto: ItemDto): Boolean = transaction {
        val itemCategory = ItemCategory.findById(itemDto.categoryId)
            ?: throw NotFoundException("Category not found")

        val item = Item.findById(id) ?: return@transaction false

        item.title = itemDto.title
        item.description = itemDto.description
        item.totalAmount = itemDto.totalAmount
        item.category = itemCategory.id
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