package com.orion.api

import User
import com.orion.entity.Review
import com.orion.errors.respondWithErrorProcessing
import com.orion.model.BetForm
import com.orion.model.ReviewForm
import com.orion.service.ReviewService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.reviewRouting(reviewService: ReviewService) {
    route("/review") {
        post {
            val reviewForm = call.receive<ReviewForm>()
            val principal = call.principal<User>()
            if (principal == null) {
                call.respond(HttpStatusCode.Unauthorized, "User not authenticated")
            }
            val result = reviewService.createReview(principal!!, reviewForm.reviewed, reviewForm.rating, reviewForm.reviewText)
            call.respondWithErrorProcessing(result)
        }
    }
}