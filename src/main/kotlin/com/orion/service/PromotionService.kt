package com.orion.service

import com.orion.entity.PromotionType
import User
import com.orion.entity.Bid
import com.orion.entity.Promotion
import com.orion.enums.PromotionClass
import com.orion.errors.ResultWithError
import com.orion.errors.ServiceError
import com.orion.table.PromotionTable
import com.orion.table.PromotionTypeTable
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.select
import java.time.Instant

class PromotionService(
    private val moneyTransactionService: InternalMoneyTransactionService,
) {
    fun findAllPromotionTypes(): ResultWithError<List<PromotionType>> {
        return ResultWithError.Success(PromotionType.all()
            .orderBy(PromotionTypeTable.price to SortOrder.ASC)
            .toList())
    }

    fun maxPromotionsForBid(bid: Bid): Double {
        return PromotionTable
            .join(PromotionTypeTable, JoinType.INNER, onColumn = PromotionTable.promotionType, otherColumn = PromotionTypeTable.id)
            .select {
                (PromotionTable.bid eq bid.id) or (PromotionTable.user eq bid.user.id)
            }
            .orderBy(PromotionTypeTable.promotionPlus to SortOrder.DESC)
            .map { it[PromotionTypeTable.promotionPlus] }
            .first()
    }

    fun buyPromotions(user: User, promotionTypeId: Int, bid: Bid?): ResultWithError<Promotion> {
        val promotionTypeToBuy = PromotionType.findById(promotionTypeId)
            ?: return ResultWithError.Failure(ServiceError.NotFound)
        if (promotionTypeToBuy.promotionClass == PromotionClass.BID.name) {
            return buyBidPromotion(user, promotionTypeToBuy, bid)
        } else if (promotionTypeToBuy.promotionClass == PromotionClass.ALL.name) {
            return buyAllPromotion(user, promotionTypeToBuy)
        } else {
            return ResultWithError.Failure(ServiceError.Custom("Unknown class of promotion"))
        }
    }

    private fun buyBidPromotion(user: User, promotionType: PromotionType, bid: Bid?): ResultWithError<Promotion> {
        if (bid == null) {
            return ResultWithError.Failure(ServiceError.Custom("You need to specify bid to buy this promotion"))
        }

        val promotion = Promotion.new {
            this.promotionType = promotionType
            this.user = user
            this.bid = bid
            this.startTime = Instant.now()
            this.endTime = Instant.now().plusSeconds(promotionType.durationDays * 24 * 3600)
        }
        moneyTransactionService.payForPromotion(user, promotion)
        return ResultWithError.Success(promotion)
    }

    private fun buyAllPromotion(user: User, promotionType: PromotionType): ResultWithError<Promotion> {
        val promotion = Promotion.new {
            this.promotionType = promotionType
            this.user = user
            this.startTime = Instant.now()
            this.endTime = Instant.now().plusSeconds(promotionType.durationDays * 24 * 3600)
        }
        moneyTransactionService.payForPromotion(user, promotion)
        return ResultWithError.Success(promotion)
    }
}