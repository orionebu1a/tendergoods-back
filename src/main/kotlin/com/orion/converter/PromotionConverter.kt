package com.orion.converter

import com.orion.entity.Promotion
import com.orion.entity.getFullName
import com.orion.model.PromotionDto

fun Promotion.toDto(): PromotionDto {
    return PromotionDto(
        promotionType = promotionType.name,
        user = user.getFullName(),
        bid = bid.id.value,
        startTime = startTime,
        endTime = endTime,
    )
}