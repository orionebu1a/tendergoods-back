package com.orion.service

import User
import com.orion.converter.toDto
import com.orion.entity.Bid
import com.orion.entity.Item
import com.orion.filter.BidPageFilter
import com.orion.model.BidDto
import com.orion.model.BidForm
import com.orion.table.BidTable
import com.orion.table.ItemTable
import org.jetbrains.exposed.sql.transactions.transaction
import java.math.BigDecimal
import java.time.Instant

class BidService {
    fun findAll(): List<BidDto> = transaction {
        val bids = Bid.all().toList()
        return@transaction bids.map { it.toDto() }
    }

    fun findPagedByFilter(filter: BidPageFilter): List<BidDto> = transaction {
        val matchingItems = Item.find { ItemTable.category eq filter.itemCategory }.toList()

        val bidsWithMatchingItems = matchingItems.mapNotNull { it.bidId?.value }.distinct()

        val bids = if (bidsWithMatchingItems.isNotEmpty()) {
            Bid.find {
                BidTable.id inList bidsWithMatchingItems
//                BidTable.startingPrice lessEq filter.startingPriceTo ?: Double.MAX_VALUE
                //TODO

            }.toList()
        } else {
            emptyList()
        }

        return@transaction bids.map { it.toDto() }
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
            currentPrice = bid.currentPrice
            priceIncrement = bid.priceIncrement
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
        oldBid.currentPrice = bid.currentPrice
        oldBid.priceIncrement = bid.priceIncrement
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