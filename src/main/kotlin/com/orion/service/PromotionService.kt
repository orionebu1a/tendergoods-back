package com.orion.service

import com.orion.converter.toDto
import com.orion.entity.PromotionType
import com.orion.entity.User
import com.orion.entity.Bid
import com.orion.entity.Promotion
import com.orion.enums.PromotionClass
import com.orion.errors.ResultWithError
import com.orion.errors.ServiceError
import com.orion.model.PromotionDto
import com.orion.table.PromotionTable
import com.orion.table.PromotionTypeTable
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant
import javax.management.Query.and

class PromotionService(
    private val moneyTransactionService: InternalMoneyTransactionService,
) {
    fun findAllPromotionTypes(): ResultWithError<List<PromotionType>> = transaction {
        ResultWithError.Success(PromotionType.all()
            .orderBy(PromotionTypeTable.price to SortOrder.ASC)
            .toList())
    }

    fun maxPromotionsForBid(bid: Bid): Double = transaction {
        val res = PromotionTable
            .join(PromotionTypeTable, JoinType.INNER, onColumn = PromotionTable.promotionType, otherColumn = PromotionTypeTable.id)
            .select {
                (PromotionTable.bid eq bid.id) or (PromotionTable.user eq bid.user.id)
            }
            .orderBy(PromotionTypeTable.promotionPlus to SortOrder.DESC)
            .map { it[PromotionTypeTable.promotionPlus] }
            .firstOrNull() ?: 0.0
        return@transaction res
    }

    fun buyPromotion(user: User, promotionTypeId: Int, bidId: Int?): ResultWithError<PromotionDto> = transaction {
        val promotionTypeToBuy = PromotionType.findById(promotionTypeId)
            ?: return@transaction ResultWithError.Failure(ServiceError.NotFound)
        when (promotionTypeToBuy.promotionClass) {
            PromotionClass.BID.name -> {
                buyBidPromotion(user, promotionTypeToBuy, bidId)
            }
            PromotionClass.ALL.name -> {
                buyAllPromotion(user, promotionTypeToBuy)
            }
            else -> {
                ResultWithError.Failure(ServiceError.Custom("Unknown class of promotion"))
            }
        }
    }

    private fun buyBidPromotion(user: User, promotionType: PromotionType, bidId: Int?): ResultWithError<PromotionDto> = transaction {
        if (bidId == null) {
            return@transaction ResultWithError.Failure(ServiceError.Custom("You need to specify bid to buy this promotion"))
        }
        val bid = Bid.findById(bidId) ?: return@transaction ResultWithError.Failure(ServiceError.Custom("Bid not found"))

        val promotion = Promotion.new {
            this.promotionType = promotionType
            this.user = user
            this.bid = bid
            this.startTime = Instant.now()
            this.endTime = Instant.now().plusSeconds(promotionType.durationDays * 24 * 3600)
        }
        moneyTransactionService.payForPromotion(user, promotion)
        ResultWithError.Success(promotion.toDto())
    }

    private fun buyAllPromotion(user: User, promotionType: PromotionType): ResultWithError<PromotionDto> = transaction {
        val promotion = Promotion.new {
            this.promotionType = promotionType
            this.user = user
            this.startTime = Instant.now()
            this.endTime = Instant.now().plusSeconds(promotionType.durationDays * 24 * 3600)
        }
        moneyTransactionService.payForPromotion(user, promotion)
        ResultWithError.Success(promotion.toDto())
    }
}