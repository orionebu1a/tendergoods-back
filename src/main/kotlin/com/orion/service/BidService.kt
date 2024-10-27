package com.orion.service

import User
import com.orion.converter.toDto
import com.orion.entity.Bid
import com.orion.entity.Item
import com.orion.enums.BidState
import com.orion.errors.ResultWithError
import com.orion.errors.ServiceError
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

    fun findPagedByFilter(filter: BidPageFilter): List<BidDto> = transaction {
        val matchingItems = Item.find { filter.itemCategories.let {
            if (it == null) {
                return@let Op.TRUE
            }
            else {
                return@let ItemTable.category inList filter.itemCategories!!
            }
        } }.toList()
        val bidsWithMatchingItems = matchingItems.mapNotNull { it.bidId?.value }.distinct()

        val stateCondition = when (filter.state) {
            BidState.ALL -> Op.TRUE
            BidState.ACTIVE -> (BidTable.startTime lessEq Instant.now()) and (BidTable.endTime greaterEq Instant.now())
            BidState.OVER -> BidTable.endTime less Instant.now()
            BidState.FUTURE -> BidTable.startTime greater Instant.now()
        }
        val bids =
            Bid.find {
                filter.itemCategories.let {
                    if (it != null) {
                        return@let BidTable.id inList bidsWithMatchingItems
                    }
                    else {
                        return@let Op.TRUE
                    }
                } and
                        (BidTable.startingPrice lessEq (filter.startingPriceTo ?: Double.MAX_VALUE)) and
                        (BidTable.startingPrice greaterEq (filter.startingPriceFrom ?: Double.MIN_VALUE)) and
                        (BidTable.priceIncrement lessEq (filter.priceIncrementTo ?: Double.MAX_VALUE)) and
                        (BidTable.priceIncrement greaterEq (filter.priceIncrementFrom ?: Double.MIN_VALUE)) and
                        (BidTable.currentPrice lessEq (filter.currentPriceTo ?: Double.MAX_VALUE)) and
                        (BidTable.currentPrice greaterEq (filter.currentPriceFrom ?: Double.MIN_VALUE)) and
                        filter.locations.let {
                            if (filter.locations != null) {
                                return@let BidTable.location inList filter.locations
                            }
                            else {
                                return@let Op.TRUE
                            }
                        } and
                        stateCondition
            }.toList()

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

    fun findById(id: Int): ResultWithError<BidDto> = transaction {
        val bid = Bid.findById(id) ?: return@transaction ResultWithError.Failure(ServiceError.NotFound)
        ResultWithError.Success(bid.toDto())
    }

    fun create(bid: BidForm, principal: User): ResultWithError<BidDto> = transaction {
        try {
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
            itemsFound.forEach { it.bidId = newBid.id }

            ResultWithError.Success(newBid.toDto())
        } catch (e: Exception) {
            ResultWithError.Failure(ServiceError.DatabaseError(e.message ?: "Unknown error"))
        }
    }

    fun update(id: Int, bid: BidForm, user: User): ResultWithError<BidDto> = transaction {
        val oldBid = Bid.findById(id) ?: return@transaction ResultWithError.Failure(ServiceError.NotFound)

        if (oldBid.user.id.value != user.id.value) {
            return@transaction ResultWithError.Failure(ServiceError.NotOwn)
        }

        try {
            oldBid.apply {
                startingPrice = bid.startingPrice
                priceIncrement = bid.priceIncrement
                latitude = bid.latitude
                longitude = bid.longitude
                location = bid.location
                startTime = bid.startTime
                endTime = bid.endTime
                promotionRating = bid.promotionRating ?: 0
                updatedAt = Instant.now()
            }
            ResultWithError.Success(oldBid.toDto())
        } catch (e: Exception) {
            ResultWithError.Failure(ServiceError.DatabaseError(e.message ?: "Update failed"))
        }
    }

    fun delete(id: Int, user: User): ResultWithError<Unit> = transaction {
        val bid = Bid.findById(id) ?: return@transaction ResultWithError.Failure(ServiceError.NotFound)

        if (bid.user.id.value != user.id.value) {
            return@transaction ResultWithError.Failure(ServiceError.NotOwn)
        }
        try {
            bid.delete()
            ResultWithError.Success(Unit)
        } catch (e: Exception) {
            ResultWithError.Failure(ServiceError.DatabaseError(e.message ?: "Delete failed"))
        }
    }
}