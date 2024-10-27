package com.orion.entity

import com.orion.table.ItemCategoryTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class ItemCategory(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ItemCategory>(ItemCategoryTable)

    val name by ItemCategoryTable.name
}