package com.orion.entity

import com.orion.table.ActionTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class Action(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Action>(ActionTable)
    var user by User referencedOn ActionTable.user
    var item by Item optionalReferencedOn ActionTable.item
    var actionType by ActionTable.actionType
    var bidPrice by ActionTable.bidPrice
    var itemCategory by ActionTable.itemCategory
    var actionTime by ActionTable.actionTime
}