package com.orion.filter

import java.time.Instant

data class BidFilter (
    val itemCategory: Int?,
    val startingPrice: Double?,
    val currentPrice: Double?,
    val priceIncrement: Double?,
    val location: String?,
    val startTime: Instant?,
    val endTime: Instant?,
    //TODO category enum
)