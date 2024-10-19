package com.orion.converter

import com.orion.entity.Bid
import com.orion.model.BidDto

fun Bid.toDto(): BidDto {
    return BidDto(
        id = this.id.value,
        userId = this.user.id.value,
        currentPrice = this.currentPrice.toDouble(),
        items = this.items.toList().map { it.id.value },
        endTime = this.endTime,
        location = this.location,
        priceIncrement = this.priceIncrement.toDouble(),
        startingPrice = this.startingPrice.toDouble(),
        startTime = this.startTime,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
    )
}