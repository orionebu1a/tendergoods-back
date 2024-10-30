package com.orion.filter

import com.orion.enums.BidState
import kotlinx.serialization.Serializable

@Serializable
data class BidPageFilter (
    val from: Int,
    val to: Int,

    val itemCategories: List<Int>? = null,
    val startingPriceFrom: Double? = null,
    val startingPriceTo: Double? = null,
    val currentPriceFrom: Double? = null,
    val currentPriceTo: Double? = null,
    val priceIncrementFrom: Double? = null,
    val priceIncrementTo: Double? = null,
    var latitude: Double? = null,
    var longitude: Double? = null,
    val locations: List<String>? = null,
    val locationRadius: Double? = null,
    val state: BidState
)