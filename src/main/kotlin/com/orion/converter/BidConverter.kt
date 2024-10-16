package com.orion.converter

import com.orion.entity.Bid
import com.orion.form.BidDto

//fun BidDto.toModel(): Bid {
//    return Bid.new {
//        user = this@toModel.userId
//        currentPrice = this@toModel.currentPrice.toBigDecimal()
//        items = this@toModel.items
//        endTime = this@toModel.endTime
//        location = this@toModel.location
//        priceIncrement = this@toModel.priceIncrement.toBigDecimal()
//        startingPrice = this@toModel.startingPrice.toBigDecimal()
//        startTime = this@toModel.startTime
//        createdAt = this@toModel.createdAt!!
//        updatedAt = this@toModel.updatedAt!!
//    }
//}
//TODO

fun Bid.toDto(): BidDto {
    return BidDto(
        user = this.user.id.value,
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