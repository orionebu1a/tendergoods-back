package com.orion.table

import org.jetbrains.exposed.dao.id.IntIdTable

object PromotionTypeTable : IntIdTable("promotion_types") {
    val name = varchar("name", length = 20)
    val promotionClass = varchar("promotion_class", length = 20)
    val promotionPlus = double("promotion_plus").default(0.0)
    val durationDays = long("duration_days").default(0)
    val price = double("price").default(0.0)
}