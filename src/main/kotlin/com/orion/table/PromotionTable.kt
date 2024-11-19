package com.orion.table

import UserTable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.timestamp

object PromotionTable : IntIdTable("promotions") {
    val promotionType = reference("promotion_type_id", PromotionTypeTable)
    val user = reference("user_id", UserTable)
    val bid = reference("bid_id", BidTable)
    val name = varchar("name", 20)
    val startTime = timestamp("start_time")
    val endTime = timestamp("end_time")
}