package com.orion.table

import UserTable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.CurrentTimestamp
import org.jetbrains.exposed.sql.javatime.timestamp

object ActionTable : IntIdTable("actions"){
    val user = reference("user_id", UserTable)
    val item = reference("item_id", ItemTable).nullable()
    val itemCategory = reference("item_category_id", ItemCategoryTable).nullable()
    val bidPrice = double("bid_price").nullable()
    val actionType = varchar("action_type", 20).nullable()
    val actionTime = timestamp("action_time").defaultExpression(CurrentTimestamp())
}