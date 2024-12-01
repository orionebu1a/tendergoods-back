package com.orion.api

import com.orion.entity.User
import com.orion.model.BetForm
import com.orion.service.BetService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import com.orion.errors.respondWithErrorProcessing

fun Route.betRouting(betService: BetService) {
    route("/bet") {
        post("doBet") {
            val betForm = call.receive<BetForm>()
            val user = call.principal<User>()
            if (user == null) {
                call.respond(HttpStatusCode.Unauthorized, "com.orion.entity.User not authenticated")
            }
            val result = betService.doBet(betForm.bidId, betForm.newPrice, user!!)
            call.respondWithErrorProcessing(result)
        }
    }
}