package com.orion.repository

import com.orion.converter.toDto
import com.orion.entity.Bid
import com.orion.entity.Item
import com.orion.form.BidDto
import com.orion.table.ItemTable
import io.ktor.server.plugins.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant

class BidRepository {
    fun findAll(): List<BidDto> = transaction {
        val bids = Bid.all().toList()
        return@transaction bids.map { it.toDto() }
    }

    fun findById(id: Int): BidDto? = transaction {
        val bid = Bid.findById(id)
        return@transaction bid?.toDto()
    }

    fun create(bid: BidDto): BidDto = transaction {
        var items: List<Item> = emptyList()
        if (bid.items != null){
            items = Item.find { ItemTable.id inList bid.items }.toList()
        }
        var foundUser = User.findById(bid.user)
        if (foundUser == null) {
            //TODO
            //throw BadRequestException
        }
        val newBid = Bid.new {
            user = foundUser!!
            startingPrice = bid.startingPrice.toBigDecimal()
            currentPrice = bid.currentPrice.toBigDecimal()
            priceIncrement = bid.priceIncrement.toBigDecimal()
            location = bid.location
            startTime = bid.startTime
            endTime = bid.endTime
            promotionRating = bid.promotionRating ?: 0
            createdAt = Instant.now()
            updatedAt = Instant.now()
        }

        for (item in items){
            item.bidId = newBid.id
        }

        return@transaction newBid.toDto()
    }


    fun update(id: Int, bid: BidDto): Boolean = transaction {
        val items = Item.find { ItemTable.id inList bid.items }.toList()

        val oldBid = Bid.findById(id) ?: return@transaction false

        oldBid.startingPrice = bid.startingPrice.toBigDecimal()
        oldBid.currentPrice = bid.currentPrice.toBigDecimal()
        oldBid.priceIncrement = bid.priceIncrement.toBigDecimal()
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