package com.orion.service

import com.orion.entity.Item
import com.orion.form.BidDto
import com.orion.form.ItemDto
import com.orion.repository.ItemRepository

class ItemService(private val itemRepository: ItemRepository) {
    fun getAllItems(): List<ItemDto> = itemRepository.findAll()

    fun getAllUserItems(userId: Int): List<ItemDto> = itemRepository.findAllUserItems(userId)

    fun createItem(itemDto: ItemDto) = itemRepository.create(itemDto)
    fun getItemById(id: Int): ItemDto? = itemRepository.findById(id)

    fun updateItem(id: Int, item: ItemDto): Boolean = itemRepository.update(id, item)

    fun deleteItem(id: Int): Boolean = itemRepository.delete(id)
}