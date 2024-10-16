package com.orion.form

import java.time.Instant

data class BidDto (
    var user: Int,
    val startingPrice: Double,
    val currentPrice: Double,
    val priceIncrement: Double,
    val location: String,
    val startTime: Instant,
    val endTime: Instant,
    val promotionRating: Int? = null,
    val items: List<Int>,
    val createdAt: Instant? = null,
    val updatedAt: Instant? = null,
)