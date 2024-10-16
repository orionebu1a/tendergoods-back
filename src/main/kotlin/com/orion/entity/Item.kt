package com.orion.entity

import User
import com.orion.table.ItemTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
class Item(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Item>(ItemTable)

    var user by User referencedOn  UserTable.id
    var bidId by ItemTable.bidId
    var userId by ItemTable.userId
    var title by ItemTable.title
    var description by ItemTable.description
    var totalAmount by ItemTable.totalAmount
    var category by ItemTable.category
    var imageUrl by ItemTable.imageUrl
    var createdAt by ItemTable.createdAt
    var updatedAt by ItemTable.updatedAt
}