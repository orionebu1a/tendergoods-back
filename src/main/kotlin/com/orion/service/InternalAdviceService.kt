package com.orion.service

import com.orion.entity.User
import com.orion.entity.Action
import com.orion.model.Advice
import com.orion.table.ActionTable
import org.jetbrains.exposed.sql.transactions.transaction

class InternalAdviceService {
    fun getAdvice(user: User): Advice = transaction {
        val actions = Action.find { ActionTable.user eq user.id }
        val mostPopularCategory = actions
            .groupingBy { it.itemCategory }
            .eachCount()
            .maxByOrNull { it.value }
            ?.key
        val avgBidPrice = actions
            .mapNotNull { it.bidPrice }
            .average()
            .takeUnless { it.isNaN() } ?: 0.0
        val advice = Advice(mostPopularCategory?.value, avgBidPrice)
        advice
    }
}