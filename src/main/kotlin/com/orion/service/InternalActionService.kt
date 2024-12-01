package com.orion.service

import com.orion.entity.User
import com.orion.entity.Action
import com.orion.entity.Bid
import com.orion.entity.Item
import com.orion.entity.ItemCategory
import com.orion.enums.ActionType
import com.orion.errors.ResultWithError
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant

class InternalActionService {
    private fun doActionBySelf(user: User, actionType: ActionType, bidPrice: Double?, itemCategory: ItemCategory?, item: Item?): ResultWithError<Action> = transaction {
        val newAction = Action.new {
            this.user = user
            this.actionType = actionType.name
            this.actionTime = Instant.now()
            this.bidPrice = bidPrice
            this.itemCategory = itemCategory?.id
            this.item = item
        }
        ResultWithError.Success(newAction)
    }

    fun doBidActionBySelf(user: User, actionType: ActionType, bid: Bid): ResultWithError<Action> = transaction {
        val mostPopularCategory = bid.items
            .groupingBy { it.category }
            .eachCount()
            .maxByOrNull { it.value }
            ?.key
        val foundCategory = if (mostPopularCategory != null) {
            ItemCategory.findById(mostPopularCategory)
        } else null
        doActionBySelf(user, actionType, bid.currentPrice, foundCategory, null)
    }

    fun doItemActionBySelf(user: User, actionType: ActionType, item: Item): ResultWithError<Action> = transaction {
        val currentBid = if (item.bidId != null) {
            Bid.findById(item.bidId!!)
        } else null
        doActionBySelf(user, actionType, currentBid?.currentPrice, ItemCategory.findById(item.category), item)
    }
}