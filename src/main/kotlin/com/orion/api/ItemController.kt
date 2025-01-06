package com.orion.api

import com.orion.entity.ItemCategory
import com.orion.entity.User
import com.orion.errors.ResultWithError
import com.orion.model.ItemForm
import com.orion.service.ItemService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import com.orion.errors.respondWithErrorProcessing

fun Route.itemRouting(itemService: ItemService) {
    route("/items") {

        get("categories") {
            val user = call.principal<User>() ?: run {
                call.respond(HttpStatusCode.Unauthorized, "com.orion.entity.User not authenticated")
                return@get
            }

            val result = ResultWithError.Success(ItemCategory.all().toList())
            call.respondWithErrorProcessing(result)
        }

        get("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid item ID")
                return@get
            }
            val user = call.principal<User>() ?: run {
                call.respond(HttpStatusCode.Unauthorized, "com.orion.entity.User not authenticated")
                return@get
            }

            val result = itemService.findById(id, user)
            call.respondWithErrorProcessing(result)
        }

        get("/ownedByUser/{id}") {
            val userId = call.parameters["id"]?.toIntOrNull()
            if (userId == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid user ID")
                return@get
            }

            val result = itemService.findAllUserItems(userId)
            call.respondWithErrorProcessing(result)
        }

        post {
            val itemForm = call.receive<ItemForm>()
            val user = call.principal<User>() ?: run {
                call.respond(HttpStatusCode.Unauthorized, "com.orion.entity.User not authenticated")
                return@post
            }

            val result = itemService.create(itemForm, user)
            call.respondWithErrorProcessing(result)
        }

        put("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid item ID")
                return@put
            }

            val itemForm = call.receive<ItemForm>()
            val user = call.principal<User>()
            val result = itemService.update(id, itemForm, user!!)
            call.respondWithErrorProcessing(result)
        }

        delete("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            val user = call.principal<User>()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid item ID")
                return@delete
            }

            val result = itemService.delete(id, user!!)
            call.respondWithErrorProcessing(result)
        }
    }
}