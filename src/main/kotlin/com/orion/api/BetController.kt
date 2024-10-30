package com.orion.api

import User
import com.orion.model.BetForm
import com.orion.service.BetService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import respondWithErrorProcessing

fun Route.betRouting(betService: BetService) {
    route("/bet") {
        post("doBet") {
            val betForm = call.receive<BetForm>()
            val principal = call.principal<User>()
            if (principal == null) {
                call.respond(HttpStatusCode.Unauthorized, "User not authenticated")
            }
            val result = betService.doBet(betForm.bidId, betForm.newPrice, principal!!)
            call.respondWithErrorProcessing(result)
        }
    }
}