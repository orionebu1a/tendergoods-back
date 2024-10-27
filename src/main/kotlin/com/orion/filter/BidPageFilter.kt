package com.orion.filter

import com.orion.enums.BidState

data class BidPageFilter (
    val from: Int,
    val to: Int,

    val itemCategories: List<Int>?,
    val startingPriceFrom: Double?,
    val startingPriceTo: Double?,
    val currentPriceFrom: Double?,
    val currentPriceTo: Double?,
    val priceIncrementFrom: Double?,
    val priceIncrementTo: Double?,
    var latitude: Double?,
    var longitude: Double?,
    val locations: List<String>?,
    val locationRadius: Double?,
    val state: BidState
)