package com.orion.service

import com.orion.entity.User
import com.orion.entity.Bid
import com.orion.entity.MoneyTransaction
import com.orion.entity.Promotion
import com.orion.enums.TransactionType
import com.orion.errors.ResultWithError
import com.orion.errors.ServiceError
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant

class InternalMoneyTransactionService {
    fun payForBet(user: User, bid: Bid): ResultWithError<MoneyTransaction> = transaction {
        if (user.walletBalance < bid.currentPrice) {
            ResultWithError.Failure(ServiceError.Custom("Not enough balance"))
        }
        ResultWithError.Success(MoneyTransaction.new {
            this.sender = user.id
            this.transactionType = TransactionType.BID.name
            this.money = bid.currentPrice
            this.time = Instant.now()
        })
    }

    fun getPaymentForBid(bid: Bid): MoneyTransaction = transaction {
        MoneyTransaction.new {
            this.receiver = bid.user.id
            this.transactionType = TransactionType.BID.name
            this.money = bid.currentPrice
            this.time = Instant.now()
        }
    }

    fun payForPromotion(user: User, promotion: Promotion): ResultWithError<MoneyTransaction> = transaction {
        val promotionPrice = promotion.promotionType.price
        if (user.walletBalance < promotionPrice) {
            ResultWithError.Failure(ServiceError.Custom("Not enough balance to buy promotion"))
        }
        ResultWithError.Success(MoneyTransaction.new {
            this.sender = user.id
            this.transactionType = TransactionType.PROMOTION.name
            this.money = promotionPrice
            this.time = Instant.now()
        })
    }
}