package com.orion.entity

import com.orion.table.MessageTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class Message(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Message>(MessageTable)
    val receiver by MessageTable.receiver
    val sender by MessageTable.sender
    val bid by MessageTable.bid
    val text by MessageTable.text

    val createdAt by MessageTable.createdAt
}