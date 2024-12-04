package com.orion.api

import com.orion.model.UserForm
import com.orion.service.UserService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import com.orion.errors.respondWithErrorProcessing

fun Route.userRouting(userService: UserService) {
    route("/users") {
        get {
            val result = userService.findAll()
            call.respondWithErrorProcessing(result)
        }

        get("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid user ID")
                return@get
            }

            val result = userService.findById(id)
            call.respondWithErrorProcessing(result)
        }

        put("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid user ID")
                return@put
            }

            val userForm = call.receive<UserForm>()
            val result = userService.update(id, userForm)
            call.respondWithErrorProcessing(result)
        }

        delete("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid user ID")
                return@delete
            }

            val result = userService.delete(id)
            call.respondWithErrorProcessing(result)
        }
    }
}