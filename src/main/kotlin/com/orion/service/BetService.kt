package com.orion.service

import com.orion.entity.User
import com.orion.entity.Bid
import com.orion.enums.ActionType
import com.orion.errors.ResultWithError
import com.orion.errors.ServiceError
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant

class BetService(
    private val actionService: InternalActionService,
    private val moneyTransactionService: InternalMoneyTransactionService,
) {
    fun doBet(bidId: Int, newBet: Double, user: User): ResultWithError<String> = transaction {
        val bid = Bid.findById(bidId) ?: return@transaction ResultWithError.Failure(ServiceError.NotFound)
        val currentTime = Instant.now()
        if (bid.endTime < currentTime) {
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
            //TODO fix
            Instant.now().plusSeconds(5)
        }
        actionService.doBidActionBySelf(user, ActionType.BET, bid)
        moneyTransactionService.payForBet(user, bid)
        ResultWithError.Success("Bet placed successfully")
    }
}