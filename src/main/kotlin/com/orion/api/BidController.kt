package com.orion.api

import com.orion.converter.toDto
import com.orion.form.BidDto
import com.orion.form.UserDto
import com.orion.service.BidService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.bidRouting(bidService: BidService) {
    route("/bids") {
        get {
            val bids = bidService.getAllBids()
            call.respond(bids)
        }

        get("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id != null) {
                val bid = bidService.getBidById(id)
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
            val bid = call.receive<BidDto>()
            val createdBid = bidService.createBid(bid)
            call.respond(HttpStatusCode.Created, createdBid)
        }

        put("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            val bid = call.receive<BidDto>()
            if (id != null) {
                val updatedBid = bidService.updateBid(id, bid)
                call.respond(updatedBid)
            } else {
                call.respond(HttpStatusCode.BadRequest, "Invalid bid ID")
            }
        }

        delete("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id != null) {
                if (bidService.deleteBid(id)) {
                    call.respond(HttpStatusCode.NoContent)
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }
            } else {
                call.respond(HttpStatusCode.BadRequest, "Invalid bid ID")
            }
        }
    }
}