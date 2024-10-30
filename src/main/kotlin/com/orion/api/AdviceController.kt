package com.orion.api

import User
import com.orion.model.BetForm
import com.orion.service.AdviceService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import respondWithErrorProcessing

fun Route.adviceRouting(adviceService: AdviceService) {
    route("/advice") {
        get("") {
            val principal = call.principal<User>()
            if (principal == null) {
                call.respond(HttpStatusCode.Unauthorized, "User not authenticated")
            }
            val result = adviceService.getAdvice(principal!!)
            call.respondWithErrorProcessing(result)
        }
    }
}