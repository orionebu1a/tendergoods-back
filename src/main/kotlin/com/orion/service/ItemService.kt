package com.orion.service

import User
import com.orion.converter.toDto
import com.orion.entity.Item
import com.orion.entity.ItemCategory
import com.orion.errors.ResultWithError
import com.orion.errors.ServiceError
import com.orion.model.ItemDto
import com.orion.model.ItemForm
import com.orion.table.ItemTable
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant

class ItemService {
    fun findAllUserItems(userId: Int): ResultWithError<List<ItemDto>> = transaction {
        try {
            val items = Item.find { ItemTable.user eq userId }.toList()
            ResultWithError.Success(items.map { it.toDto() })
        } catch (e: Exception) {
            ResultWithError.Failure(ServiceError.DatabaseError(e.message ?: "Database error"))
        }
    }

    fun findAll(): ResultWithError<List<ItemDto>> = transaction {
        try {
            val items = Item.all().toList()
            ResultWithError.Success(items.map { it.toDto() })
        } catch (e: Exception) {
            ResultWithError.Failure(ServiceError.DatabaseError(e.message ?: "Database error"))
        }
    }

    fun findById(id: Int): ResultWithError<ItemDto> = transaction {
        val item = Item.findById(id)
        if (item != null) {
            ResultWithError.Success(item.toDto())
        } else {
            ResultWithError.Failure(ServiceError.NotFound)
        }
    }

    fun create(itemDto: ItemForm, principal: User): ResultWithError<ItemDto> = transaction {
        try {
            val itemCategory = ItemCategory.findById(itemDto.categoryId)
                ?: return@transaction ResultWithError.Failure(ServiceError.NotFound)

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
            ResultWithError.Success(item.toDto())
        } catch (e: Exception) {
            ResultWithError.Failure(ServiceError.DatabaseError(e.message ?: "Creation failed"))
        }
    }

    fun update(id: Int, itemDto: ItemForm, user: User): ResultWithError<Boolean> = transaction {
        val itemCategory = ItemCategory.findById(itemDto.categoryId)
            ?: return@transaction ResultWithError.Failure(ServiceError.NotFound)

        val oldItem = Item.findById(id) ?: return@transaction ResultWithError.Failure(ServiceError.NotFound)
        if (oldItem.user.id.value != user.id.value) {
            return@transaction ResultWithError.Failure(ServiceError.NotOwn)
        }

        try {
            oldItem.title = itemDto.title
            oldItem.description = itemDto.description
            oldItem.totalAmount = itemDto.totalAmount
            oldItem.category = itemCategory.id
            oldItem.imageUrl = itemDto.imageUrl
            oldItem.updatedAt = Instant.now()
            ResultWithError.Success(true)
        } catch (e: Exception) {
            ResultWithError.Failure(ServiceError.DatabaseError(e.message ?: "Update failed"))
        }
    }

    fun delete(id: Int, user: User): ResultWithError<Unit> = transaction {
        val item = Item.findById(id) ?: return@transaction ResultWithError.Failure(ServiceError.NotFound)

        if (item.user.id.value != user.id.value) {
            return@transaction ResultWithError.Failure(ServiceError.NotOwn)
        }
        try {
            item.delete()
            ResultWithError.Success(Unit)
        } catch (e: Exception) {
            ResultWithError.Failure(ServiceError.DatabaseError(e.message ?: "Delete failed"))
        }
    }
}