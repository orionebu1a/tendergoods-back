package com.orion.table

import UserTable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.CurrentTimestamp
import org.jetbrains.exposed.sql.javatime.timestamp
object BidTable : IntIdTable("bids") {
    val lastUserBet = reference("last_user_bet", UserTable)
    val user = reference("user_id", UserTable)
    val latitude = double("latitude")
    val longitude = double("longitude")
    val startingPrice = double("starting_price")
    val currentPrice = double("current_price")
    val priceIncrement = double("price_increment")
    val location = varchar("location", 255)
    val startTime = timestamp("start_time")
    val endTime = timestamp("end_time")
    val createdAt = timestamp("created_at").defaultExpression(CurrentTimestamp())
    val updatedAt = timestamp("updated_at").defaultExpression(CurrentTimestamp())
}