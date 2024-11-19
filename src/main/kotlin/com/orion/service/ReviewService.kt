package com.orion.service;

import User
import com.orion.entity.Review;
import com.orion.errors.ResultWithError
import com.orion.errors.ServiceError
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant

class ReviewService() {
    fun createReview(user: User, userToReviewId: Int, rating: Double, reviewText: String): ResultWithError<Review> = transaction {
        if (rating < 0.0 || rating > 5.0) {
            return@transaction ResultWithError.Failure(ServiceError.Custom("Rating should be between 0.0 and 5.0"))
        }
        val userToReview = User.findById(userToReviewId)
            ?: return@transaction ResultWithError.Failure(ServiceError.NotFound)
        return@transaction ResultWithError.Success(Review.new {
            this.reviewer = user
            this.reviewed = userToReview
            this.rating = rating
            this.reviewText = reviewText
            this.createdAt = Instant.now()
        })
    }
}