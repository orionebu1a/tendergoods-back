package com.orion.api

import User
import com.orion.model.ItemForm
import com.orion.service.ItemService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import respondWithError

fun Route.itemRouting(itemService: ItemService) {
    route("/items") {

        get("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid item ID")
                return@get
            }

            val result = itemService.findById(id)
            call.respondWithError(result)
        }

        get("/ownedByUser/{id}") {
            val userId = call.parameters["id"]?.toIntOrNull()
            if (userId == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid user ID")
                return@get
            }

            val result = itemService.findAllUserItems(userId)
            call.respondWithError(result)
        }

        post {
            val itemForm = call.receive<ItemForm>()
            val principal = call.principal<User>() ?: run {
                call.respond(HttpStatusCode.Unauthorized, "User not authenticated")
                return@post
            }

            val result = itemService.create(itemForm, principal)
            call.respondWithError(result)
        }

        put("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid item ID")
                return@put
            }

            val itemForm = call.receive<ItemForm>()
            val principal = call.principal<User>()
            val result = itemService.update(id, itemForm, principal!!)
            call.respondWithError(result)
        }

        delete("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            val principal = call.principal<User>()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid item ID")
                return@delete
            }

            val result = itemService.delete(id, principal!!)
            call.respondWithError(result)
        }
    }
}