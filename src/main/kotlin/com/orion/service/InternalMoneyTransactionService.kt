package com.orion.service

import User
import com.orion.entity.Bid
import com.orion.entity.MoneyTransaction
import com.orion.entity.Promotion
import com.orion.enums.TransactionType
import com.orion.errors.ResultWithError
import com.orion.errors.ServiceError
import java.time.Instant

class InternalMoneyTransactionService {
    fun payForBet(user: User, bid: Bid): ResultWithError<MoneyTransaction> {
        if (user.walletBalance < bid.currentPrice) {
            ResultWithError.Failure(ServiceError.Custom("Not enough balance"))
        }
        return ResultWithError.Success(MoneyTransaction.new {
            this.sender = user.id
            this.transactionType = TransactionType.BID.name
            this.money = bid.currentPrice
            this.time = Instant.now()
        })
    }

    fun getPaymentForBid(bid: Bid): MoneyTransaction {
        return MoneyTransaction.new {
            this.receiver = bid.user.id
            this.transactionType = TransactionType.BID.name
            this.money = bid.currentPrice
            this.time = Instant.now()
        }
    }

    fun payForPromotion(user: User, promotion: Promotion): ResultWithError<MoneyTransaction> {
        val promotionPrice = promotion.promotionType.price
        if (user.walletBalance < promotionPrice) {
            return ResultWithError.Failure(ServiceError.Custom("Not enough balance to buy promotion"))
        }
        return ResultWithError.Success(MoneyTransaction.new {
            this.sender = user.id
            this.transactionType = TransactionType.PROMOTION.name
            this.money = promotionPrice
            this.time = Instant.now()
        })
    }
}