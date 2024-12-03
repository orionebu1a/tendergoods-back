package com.orion.converter

import com.orion.entity.Review
import com.orion.entity.getFullName
import com.orion.model.ReviewDto

fun Review.toDto(): ReviewDto {
    return ReviewDto(
        reviewer = reviewer.getFullName(),
        reviewed = reviewed.getFullName(),
        rating = rating,
        reviewText = reviewText,
        createdAt = createdAt,
    )
}