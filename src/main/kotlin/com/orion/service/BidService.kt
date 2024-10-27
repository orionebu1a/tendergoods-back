package com.orion.service

import User
import com.orion.converter.toDto
import com.orion.entity.Bid
import com.orion.entity.Item
import com.orion.enums.BidState
import com.orion.filter.BidPageFilter
import com.orion.model.BidDto
import com.orion.model.BidForm
import com.orion.table.BidTable
import com.orion.table.ItemTable
import com.orion.util.haversine
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder.greater
import org.jetbrains.exposed.sql.SqlExpressionBuilder.greaterEq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.less
import org.jetbrains.exposed.sql.SqlExpressionBuilder.lessEq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant

class BidService {
    fun findAll(): List<BidDto> = transaction {
        val bids = Bid.all().toList()
        return@transaction bids.map { it.toDto() }
    }

    fun findPagedByFilter(filter: BidPageFilter): List<BidDto> = transaction {
        val matchingItems = Item.find { ItemTable.category eq filter.itemCategory }.toList()
        val bidsWithMatchingItems = matchingItems.mapNotNull { it.bidId?.value }.distinct()

        val stateCondition = when (filter.state) {
            BidState.ALL -> Op.TRUE
            BidState.ACTIVE -> (BidTable.startTime lessEq Instant.now()) and (BidTable.endTime greaterEq Instant.now())
            BidState.OVER -> BidTable.endTime less Instant.now()
            BidState.FUTURE -> BidTable.startTime greater Instant.now()
        }

        val bids = if (bidsWithMatchingItems.isNotEmpty()) {
            Bid.find {
                (BidTable.id inList bidsWithMatchingItems) and
                (BidTable.startingPrice lessEq (filter.startingPriceTo ?: Double.MAX_VALUE)) and
                (BidTable.startingPrice greaterEq (filter.startingPriceFrom ?: Double.MIN_VALUE)) and
                (BidTable.priceIncrement lessEq (filter.priceIncrementTo ?: Double.MAX_VALUE)) and
                (BidTable.priceIncrement greaterEq (filter.priceIncrementFrom ?: Double.MIN_VALUE)) and
                (BidTable.currentPrice lessEq (filter.currentPriceTo ?: Double.MAX_VALUE)) and
                (BidTable.currentPrice greaterEq (filter.currentPriceFrom ?: Double.MIN_VALUE)) and
                (BidTable.location inList filter.locations) and
                stateCondition
            }.toList()
        } else {
            emptyList()
        }

        return@transaction bids
            .filter {
                if (filter.latitude != null && filter.longitude != null) {
                    return@filter haversine(it.latitude, it.longitude, filter.latitude!!, filter.longitude!!) <=
                            (filter.locationRadius ?: Double.MAX_VALUE)
                } else {
                    return@filter true
                }
            }
            .map { it.toDto() }
    }


    fun findById(id: Int): BidDto? = transaction {
        val bid = Bid.findById(id)
        return@transaction bid?.toDto()
    }

    fun create(bid: BidForm, principal: User): BidDto = transaction {
        val itemsFound = Item.find { ItemTable.id inList bid.items }.toList()
        val newBid = Bid.new {
            user = principal
            startingPrice = bid.startingPrice
            currentPrice = bid.startingPrice
            priceIncrement = bid.priceIncrement
            latitude = bid.latitude
            longitude = bid.longitude
            location = bid.location
            startTime = bid.startTime
            endTime = bid.endTime
            promotionRating = bid.promotionRating ?: 0
            createdAt = Instant.now()
            updatedAt = Instant.now()
        }

        for (item in itemsFound){
            item.bidId = newBid.id
        }

        return@transaction newBid.toDto()
    }


    fun update(id: Int, bid: BidForm): Boolean = transaction {
        val items = Item.find { ItemTable.id inList bid.items }.toList()

        val oldBid = Bid.findById(id) ?: return@transaction false

        oldBid.startingPrice = bid.startingPrice
        oldBid.priceIncrement = bid.priceIncrement
        oldBid.latitude = bid.latitude
        oldBid.longitude = bid.longitude
        oldBid.location = bid.location
        oldBid.startTime = bid.startTime
        oldBid.endTime = bid.endTime
        oldBid.promotionRating = bid.promotionRating ?: 0
        oldBid.updatedAt = Instant.now()

        for (item in items){
            item.bidId = oldBid.id
        }

        for (oldItem in oldBid.items){
            if (!items.contains(oldItem)) {
                oldItem.bidId = null
            }
        }

        return@transaction true
    }

    fun delete(id: Int): Boolean = transaction {
        val oldBid = Bid.findById(id)
        if (oldBid == null) {
            return@transaction false
        }
        else {
            oldBid.delete()
            return@transaction true
        }
    }

}