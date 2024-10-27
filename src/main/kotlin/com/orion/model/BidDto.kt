package com.orion.model

import com.orion.serializer.InstantSerializer
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class BidDto (
    val id: Int,
    var userId: Int,
    val startingPrice: Double,
    val currentPrice: Double,
    val priceIncrement: Double,
    val latitude: Double,
    val longitude: Double,
    val location: String,
    @Serializable(with = InstantSerializer::class)
    val startTime: Instant,
    @Serializable(with = InstantSerializer::class)
    val endTime: Instant,
    val promotionRating: Int? = null,
    val items: List<Int>,
    @Serializable(with = InstantSerializer::class)
    val createdAt: Instant? = null,
    @Serializable(with = InstantSerializer::class)
    val updatedAt: Instant? = null,
    val lastUserBet: Int? = null,
)

@Serializable
data class BidForm (
    val startingPrice: Double,
    val priceIncrement: Double,
    val location: String,
    val latitude: Double,
    val longitude: Double,
    @Serializable(with = InstantSerializer::class)
    val startTime: Instant,
    @Serializable(with = InstantSerializer::class)
    val endTime: Instant,
    val promotionRating: Int? = null,
    val items: List<Int>,
)