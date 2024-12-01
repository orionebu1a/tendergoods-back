package com.orion.entity

import com.orion.table.MessageTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class Message(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Message>(MessageTable)
    var receiver by User referencedOn MessageTable.receiver
    var sender by User referencedOn MessageTable.sender
    var bid by MessageTable.bid
    var text by MessageTable.text

    var createdAt by MessageTable.createdAt
}