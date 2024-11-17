package com.orion.service

import User
import com.orion.entity.Action
import com.orion.model.Advice
import com.orion.table.ActionTable

class InternalAdviceService {
    fun getAdvice(user: User): Advice {
        val actions = Action.find { ActionTable.user eq user.id }
        val mostPopularCategory = actions
            .groupingBy { it.itemCategory }
            .eachCount()
            .maxByOrNull { it.value }
            ?.key
        val avgBidPrice = actions
            .mapNotNull { it.bidPrice }
            .average()
        val advice = Advice(mostPopularCategory?.value, avgBidPrice)
        return advice
    }
}