package com.orion.entity

import PromotionType
import User
import com.orion.table.PromotionTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class Promotion(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Promotion>(PromotionTable)
    var promotionType by PromotionType referencedOn PromotionTable.promotionType
    var user by User referencedOn PromotionTable.user
    var bid by Bid referencedOn PromotionTable.bid
    var startTime by PromotionTable.startTime
    var endTime by PromotionTable.endTime
}