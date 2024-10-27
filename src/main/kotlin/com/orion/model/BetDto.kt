package com.orion.model

import kotlinx.serialization.Serializable

@Serializable
data class BetForm (
    val newPrice: Double,
    val bidId: Int
)