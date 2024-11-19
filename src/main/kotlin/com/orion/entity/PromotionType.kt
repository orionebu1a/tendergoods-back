package com.orion.entity

import com.orion.table.PromotionTypeTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class PromotionType(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<PromotionType>(PromotionTypeTable)

    var name by PromotionTypeTable.name
    var promotionClass by PromotionTypeTable.promotionClass
    var promotionPlus by PromotionTypeTable.promotionPlus
    var durationDays by PromotionTypeTable.durationDays
    var price by PromotionTypeTable.price
}