package com.orion.filter

import java.time.Instant

data class BidItemFilter (
    val from: Int,
    val to: Int,

    val itemCategory: Int?,
    val startingPrice: Double?,
    val currentPrice: Double?,
    val priceIncrement: Double?,
    val location: String?,
    val startTime: Instant?,
    val endTime: Instant?,
)