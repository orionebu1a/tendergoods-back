package com.orion.api

import User
import com.orion.filter.BidPageFilter
import com.orion.model.BidForm
import com.orion.service.BidService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.bidRouting(bidService: BidService) {
    route("/bids") {
        get {
            val bids = bidService.findAll()
            call.respond(bids)
        }

        get("paged") {
            val bidPageFilter = call.receive<BidPageFilter>()
            val bids = bidService.findPagedByFilter(bidPageFilter)
            call.respond(bids)
        }

        get("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id != null) {
                val bid = bidService.findById(id)
                if (bid != null) {
                    call.respond(bid)
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }
            } else {
                call.respond(HttpStatusCode.BadRequest, "Invalid bid ID")
            }
        }

        post {
            val bid = call.receive<BidForm>()
            val principal = call.principal<User>()
            val createdBid = bidService.create(bid, principal!!)
            call.respond(HttpStatusCode.Created, createdBid)
        }

        put("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            val bid = call.receive<BidForm>()
            if (id != null) {
                val existing = bidService.findById(id)
                val principal = call.principal<User>()
                if (existing?.userId == principal?.id?.value) {
                    val updatedBid = bidService.update(id, bid)
                    call.respond(updatedBid)
                }
                else {
                    call.respond(HttpStatusCode.BadRequest, "Impossible to update bid you are not own")
                }
            } else {
                call.respond(HttpStatusCode.BadRequest, "Invalid bid ID")
            }
        }

        delete("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id != null) {
                val bid = bidService.findById(id)
                val principal = call.principal<User>()
                if (bid?.userId == principal?.id?.value) {
                    if (bidService.delete(id)) {
                        call.respond(HttpStatusCode.NoContent)
                    } else {
                        call.respond(HttpStatusCode.NotFound)
                    }
                }
                else{
                    call.respond(HttpStatusCode.BadRequest, "Impossible to delete bid you are not own")
                }
            } else {
                call.respond(HttpStatusCode.BadRequest, "Invalid bid ID")
            }
        }
    }
}