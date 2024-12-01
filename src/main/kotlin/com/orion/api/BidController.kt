package com.orion.api

import com.orion.entity.User
import com.orion.filter.BidPageFilter
import com.orion.model.BidForm
import com.orion.service.BidService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import com.orion.errors.respondWithErrorProcessing

fun Route.bidRouting(bidService: BidService) {
    route("/bids") {

        post("paged") {
            val bidPageFilter = call.receive<BidPageFilter>()
            val user = call.principal<User>()
            if (user == null) {
                call.respond(HttpStatusCode.Unauthorized, "com.orion.entity.User not authenticated")
                return@post
            }
            val bids = bidService.findPagedByFilter(bidPageFilter, user)
            call.respond(bids)
        }

        get("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid bid ID")
                return@get
            }
            val user = call.principal<User>()
            if (user == null) {
                call.respond(HttpStatusCode.Unauthorized, "com.orion.entity.User not authenticated")
                return@get
            }

            val result = bidService.findById(id, user)
            call.respondWithErrorProcessing(result)
        }

        post {
            val bid = call.receive<BidForm>()

            val result = bidService.create(bid, call.principal<User>()!!)
            call.respondWithErrorProcessing(result)
        }

        put("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid bid ID")
                return@put
            }

            val bidForm = call.receive<BidForm>()

            val result = bidService.update(id, bidForm, call.principal<User>()!!)
            call.respondWithErrorProcessing(result)
        }

        delete("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid bid ID")
                return@delete
            }

            val user = call.principal<User>()
            if (user == null) {
                call.respond(HttpStatusCode.Unauthorized, "com.orion.entity.User not authenticated")
                return@delete
            }
            val result = bidService.delete(id, user)
            call.respondWithErrorProcessing(result)
        }

    }
}