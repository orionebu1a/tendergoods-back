package com.orion.table

import UserTable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.CurrentTimestamp
import org.jetbrains.exposed.sql.javatime.timestamp
object ItemTable : IntIdTable("items") {
    val bidId = reference("bid_id", BidTable).nullable()
    val userId = reference("user_id", UserTable)
    val title = varchar("title", 255)
    val description = text("description").nullable()
    val totalAmount = integer("total_amount")
    val category = varchar("category", 20).nullable()
    val imageUrl = varchar("image_url", 255).nullable()
    val createdAt = timestamp("created_at").defaultExpression(CurrentTimestamp())
    val updatedAt = timestamp("updated_at").defaultExpression(CurrentTimestamp())
}