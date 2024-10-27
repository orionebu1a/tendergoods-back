package com.orion.service

import User
import com.orion.entity.Bid
import com.orion.errors.ResultWithError
import com.orion.errors.ServiceError
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant

class BetService {
    fun doBet(bidId: Int, newBet: Double, user: User): ResultWithError<String> = transaction {
        val bid = Bid.findById(bidId) ?: return@transaction ResultWithError.Failure(ServiceError.NotFound)
        if (bid.endTime < Instant.now()) {
            return@transaction ResultWithError.Failure(ServiceError.Custom("Bid is over"))
        }
        if (bid.currentPrice > newBet) {
            return@transaction ResultWithError.Failure(ServiceError.Custom("Your bet should be greater than current"))
        }
        if (newBet - bid.currentPrice != bid.priceIncrement) {
            return@transaction ResultWithError.Failure(ServiceError.Custom("Your bet should follow increment"))
        }
        bid.currentPrice = newBet
        bid.lastUserBet = user.id
        bid.updatedAt = Instant.now()
        bid.endTime = if (bid.endTime.minusSeconds(Instant.now().epochSecond) > Instant.ofEpochSecond(10 * 60)) {
            bid.endTime
        } else {
            Instant.now().plusSeconds(10 * 60)
        }
        ResultWithError.Success("Bet placed successfully")
    }
}