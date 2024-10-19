package com.orion.api

import com.orion.model.UserDto
import com.orion.service.UserService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.userRouting(userService: UserService) {
    route("/users") {
        get {
            val users = userService.findAll()
            call.respond(users)
        }

        get("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id != null) {
                val user = userService.findById(id)
                if (user != null) {
                    call.respond(user)
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }
            } else {
                call.respond(HttpStatusCode.BadRequest, "Invalid user ID")
            }
        }

        post {
            val user = call.receive<UserDto>()
            val createdUser = userService.create(user)
            call.respond(HttpStatusCode.Created, createdUser)
        }

        put("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            val user = call.receive<UserDto>()
            if (id != null) {
                val updatedUser = userService.update(id, user)
                call.respond(updatedUser)
            } else {
                call.respond(HttpStatusCode.BadRequest, "Invalid user ID")
            }
        }

        delete("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id != null) {
                if (userService.delete(id)) {
                    call.respond(HttpStatusCode.NoContent)
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }
            } else {
                call.respond(HttpStatusCode.BadRequest, "Invalid user ID")
            }
        }
    }
}