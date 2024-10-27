package com.orion.api

import User
import com.orion.errors.ResultWithError
import com.orion.errors.ServiceError
import com.orion.model.ItemForm
import com.orion.service.ItemService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.itemRouting(itemService: ItemService) {
    route("/items") {

        get("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid item ID")
                return@get
            }

            when (val result = itemService.findById(id)) {
                is ResultWithError.Success -> call.respond(result.data)
                is ResultWithError.Failure -> {
                    when (val error = result.error) {
                        ServiceError.NotFound -> call.respond(HttpStatusCode.NotFound, error.message)
                        is ServiceError.DatabaseError -> call.respond(HttpStatusCode.InternalServerError, error.message)
                        else -> call.respond(HttpStatusCode.BadRequest, error.message)
                    }
                }
            }
        }

        get("/ownedByUser/{id}") {
            val userId = call.parameters["id"]?.toIntOrNull()
            if (userId == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid user ID")
                return@get
            }

            when (val result = itemService.findAllUserItems(userId)) {
                is ResultWithError.Success -> call.respond(result.data)
                is ResultWithError.Failure -> {
                    val error = result.error
                    call.respond(HttpStatusCode.InternalServerError, error.message)
                }
            }
        }

        post {
            val itemForm = call.receive<ItemForm>()
            val principal = call.principal<User>() ?: run {
                call.respond(HttpStatusCode.Unauthorized, "User not authenticated")
                return@post
            }

            when (val result = itemService.create(itemForm, principal)) {
                is ResultWithError.Success -> call.respond(HttpStatusCode.Created, result.data)
                is ResultWithError.Failure -> call.respond(HttpStatusCode.InternalServerError, result.error.message)
            }
        }

        put("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid item ID")
                return@put
            }

            val itemForm = call.receive<ItemForm>()
            val principal = call.principal<User>()
            when (val result = itemService.update(id, itemForm, principal!!)) {
                is ResultWithError.Success -> call.respond(HttpStatusCode.OK, result.data)
                is ResultWithError.Failure -> {
                    val error = result.error
                    when (error) {
                        ServiceError.NotFound -> call.respond(HttpStatusCode.NotFound, error.message)
                        ServiceError.NotOwn -> call.respond(HttpStatusCode.Forbidden, error.message)
                        else -> call.respond(HttpStatusCode.BadRequest, error.message)
                    }
                }
            }
        }

        delete("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            val principal = call.principal<User>()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid item ID")
                return@delete
            }

            when (val result = itemService.delete(id, principal!!)) {
                is ResultWithError.Success -> call.respond(HttpStatusCode.NoContent)
                is ResultWithError.Failure -> {
                    val error = result.error
                    when (error) {
                        ServiceError.NotFound -> call.respond(HttpStatusCode.NotFound, error.message)
                        ServiceError.NotOwn -> call.respond(HttpStatusCode.Forbidden, error.message)
                        else -> call.respond(HttpStatusCode.BadRequest, error.message)
                    }
                }
            }
        }
    }
}