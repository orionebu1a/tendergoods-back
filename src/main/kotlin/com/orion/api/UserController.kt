package com.orion.api

import User
import com.orion.service.UserService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.userRouting(userService: UserService) {
    routing {
        route("/users") {
            get {
                val users = userService.getAllUsers()
                call.respond(users)
            }

            get("{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                if (id != null) {
                    val user = userService.getUserById(id)
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
                val user = call.receive<User>()
                val createdUser = userService.registerUser(user)
                call.respond(HttpStatusCode.Created, createdUser)
            }

            put("{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                val user = call.receive<User>()
                if (id != null && user.id == id) {
                    val updatedUser = userService.updateUser(user)
                    if (updatedUser != null) {
                        call.respond(updatedUser)
                    } else {
                        call.respond(HttpStatusCode.NotFound)
                    }
                } else {
                    call.respond(HttpStatusCode.BadRequest, "Invalid user ID")
                }
            }

            delete("{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                if (id != null) {
                    if (userService.deleteUser(id)) {
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
}