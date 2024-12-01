package com.orion.model

import com.orion.serializer.InstantSerializer
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class PromotionDto (
    val promotionType: String,
    val user: String,
    val bid: Int,
    @Serializable(with = InstantSerializer::class)
    val startTime: Instant,
    @Serializable(with = InstantSerializer::class)
    val endTime: Instant,
)

@Serializable
data class BuyPromotionForm (
    val id: Int,
    val bidId: Int?,
)