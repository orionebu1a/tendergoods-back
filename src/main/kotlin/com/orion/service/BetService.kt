package com.orion.service

import com.orion.entity.Bid
import org.jetbrains.exposed.sql.transactions.transaction

//class BetService {
//    fun doBet(bidId: Int): Boolean = transaction {
//        val bid = Bid.findById(bidId)
//        if (bid == null) {
//            return
//        }
//        else {
//            return@transaction true
//        }
//    }
//}