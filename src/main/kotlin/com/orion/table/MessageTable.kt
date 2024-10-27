package com.orion.table

import UserTable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.CurrentTimestamp
import org.jetbrains.exposed.sql.javatime.timestamp

object MessageTable : IntIdTable("messages") {
    val sender = reference("sender_id", UserTable)
    val receiver = reference("receiver_id", UserTable)
    val bid = reference("bid_id", BidTable)
    val text = text("message_text")
    val createdAt = timestamp("created_at").defaultExpression(CurrentTimestamp())
}