package com.orion.model

import com.orion.serializer.InstantSerializer
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class ReviewDto (
    val reviewer: String,
    val reviewed: String,
    var rating: Double,
    var reviewText: String?,
    @Serializable(with = InstantSerializer::class)
    var createdAt: Instant,
)

@Serializable
data class ReviewForm (
    val reviewed: Int,
    var rating: Double,
    var reviewText: String,
)