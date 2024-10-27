package com.orion.entity

import User
import com.orion.table.ActionTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class Action(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Action>(ActionTable)
    val user by User referencedOn ActionTable.user
    val item by Item referencedOn ActionTable.item
    val actionType by ActionTable.actionType
    val bidPrice by ActionTable.bidPrice
    val itemCategory by ActionTable.itemCategory
    val actionTime by ActionTable.actionTime
}