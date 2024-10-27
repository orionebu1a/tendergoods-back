package com.orion.model

import java.time.Instant

data class BidDto (
    val id: Int,
    var userId: Int,
    val startingPrice: Double,
    val currentPrice: Double,
    val priceIncrement: Double,
    val latitude: Double,
    val longitude: Double,
    val location: String,
    val startTime: Instant,
    val endTime: Instant,
    val promotionRating: Int? = null,
    val items: List<Int>,
    val createdAt: Instant? = null,
    val updatedAt: Instant? = null,
)

data class BidForm (
    val startingPrice: Double,
    val priceIncrement: Double,
    val location: String,
    val latitude: Double,
    val longitude: Double,
    val startTime: Instant,
    val endTime: Instant,
    val promotionRating: Int? = null,
    val items: List<Int>,
)