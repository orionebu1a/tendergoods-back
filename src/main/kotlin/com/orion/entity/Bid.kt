package com.orion.entity

import User
import com.orion.table.BidTable
import com.orion.table.ItemTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class Bid(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Bid>(BidTable)

    var latitude by BidTable.latitude
    var longitude by BidTable.longitude
    var user by User referencedOn BidTable.user
    var startingPrice by BidTable.startingPrice
    var currentPrice by BidTable.currentPrice
    var priceIncrement by BidTable.priceIncrement
    var location by BidTable.location
    var startTime by BidTable.startTime
    var endTime by BidTable.endTime
    var promotionRating by BidTable.promotionRating
    var createdAt by BidTable.createdAt
    var updatedAt by BidTable.updatedAt
    val items by Item optionalReferrersOn ItemTable.bid
}