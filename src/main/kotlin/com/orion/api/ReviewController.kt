package com.orion.api

import com.orion.entity.User
import com.orion.errors.respondWithErrorProcessing
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
            val user = call.principal<User>()
            if (user == null) {
                call.respond(HttpStatusCode.Unauthorized, "com.orion.entity.User not authenticated")
            }
            val result = reviewService.createReview(user!!, reviewForm.reviewed, reviewForm.rating, reviewForm.reviewText)
            call.respondWithErrorProcessing(result)
        }
    }
}