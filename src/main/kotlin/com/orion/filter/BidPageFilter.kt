package com.orion.filter

import com.orion.enums.BidState
import java.time.Instant

data class BidPageFilter (
    val from: Int,
    val to: Int,

    val itemCategory: Int?,
    val startingPriceFrom: Double?,
    val startingPriceTo: Double?,
    val currentPriceFrom: Double?,
    val currentPriceTo: Double?,
    val priceIncrementFrom: Double?,
    val priceIncrementTo: Double?,
    val location: String?,
    val locationRadius: Double?,
    val state: BidState
)