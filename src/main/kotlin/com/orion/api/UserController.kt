package com.orion.api

import com.orion.errors.ResultWithError
import com.orion.errors.ServiceError
import com.orion.model.UserForm
import com.orion.service.UserService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.userRouting(userService: UserService) {
    route("/users") {

        get {
            when (val result = userService.findAll()) {
                is ResultWithError.Success -> call.respond(result.data)
                is ResultWithError.Failure -> call.respond(HttpStatusCode.InternalServerError, result.error.message)
            }
        }

        get("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid user ID")
                return@get
            }

            when (val result = userService.findById(id)) {
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

        post {
            val userForm = call.receive<UserForm>()
            when (val result = userService.create(userForm)) {
                is ResultWithError.Success -> call.respond(HttpStatusCode.Created, result.data)
                is ResultWithError.Failure -> call.respond(HttpStatusCode.InternalServerError, result.error.message)
            }
        }

        put("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid user ID")
                return@put
            }

            val userForm = call.receive<UserForm>()
            when (val result = userService.update(id, userForm)) {
                is ResultWithError.Success -> call.respond(result.data)
                is ResultWithError.Failure -> {
                    val error = result.error
                    when (error) {
                        ServiceError.NotFound -> call.respond(HttpStatusCode.NotFound, error.message)
                        is ServiceError.DatabaseError -> call.respond(HttpStatusCode.InternalServerError, error.message)
                        else -> call.respond(HttpStatusCode.BadRequest, error.message)
                    }
                }
            }
        }

        delete("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid user ID")
                return@delete
            }

            when (val result = userService.delete(id)) {
                is ResultWithError.Success -> call.respond(HttpStatusCode.NoContent)
                is ResultWithError.Failure -> {
                    val error = result.error
                    when (error) {
                        ServiceError.NotFound -> call.respond(HttpStatusCode.NotFound, error.message)
                        is ServiceError.DatabaseError -> call.respond(HttpStatusCode.InternalServerError, error.message)
                        else -> call.respond(HttpStatusCode.BadRequest, error.message)
                    }
                }
            }
        }
    }
}