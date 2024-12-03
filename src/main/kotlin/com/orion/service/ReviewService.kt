package com.orion.service;

import UserTable
import com.orion.converter.toDto
import com.orion.entity.User
import com.orion.entity.Review;
import com.orion.errors.ResultWithError
import com.orion.errors.ServiceError
import com.orion.model.ReviewDto
import com.orion.table.ReviewTable
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant

class ReviewService() {
    fun createReview(user: User, userToReviewId: Int, rating: Double, reviewText: String): ResultWithError<ReviewDto> = transaction {
        if (rating < 0.0 || rating > 5.0) {
            return@transaction ResultWithError.Failure(ServiceError.Custom("Rating should be between 0.0 and 5.0"))
        }
        val userToReview = User.findById(userToReviewId)
            ?: return@transaction ResultWithError.Failure(ServiceError.NotFound)
        val review = Review.new {
            this.reviewer = user
            this.reviewed = userToReview
            this.rating = rating
            this.reviewText = reviewText
            this.createdAt = Instant.now()
        }

        userToReview.rating = ReviewTable
            .join(UserTable, JoinType.INNER, onColumn = ReviewTable.reviewed, otherColumn = UserTable.id)
            .select {
                (ReviewTable.reviewed eq userToReviewId)
            }.map { it[ReviewTable.rating] }.average()
        ResultWithError.Success(review.toDto())
    }
}