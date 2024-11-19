package com.orion.model

import kotlinx.serialization.Serializable

@Serializable
data class PromotionDto (
    val name: String,
    val price: Double,
    val promotionClass: PromotionClassDto,
    val durationDays: Int,
)